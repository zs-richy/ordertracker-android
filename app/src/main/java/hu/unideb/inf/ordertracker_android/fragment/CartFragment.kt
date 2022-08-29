package hu.unideb.inf.ordertracker_android.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import hu.unideb.inf.ordertracker_android.R
import hu.unideb.inf.ordertracker_android.adapter.CartAdapter
import hu.unideb.inf.ordertracker_android.adapter.CartAdapterListener
import hu.unideb.inf.ordertracker_android.databinding.FragmentCartBinding
import hu.unideb.inf.ordertracker_android.dialog.CustomProgressDialogHandler
import hu.unideb.inf.ordertracker_android.model.api.Product
import hu.unideb.inf.ordertracker_android.util.WidgetUtils
import hu.unideb.inf.ordertracker_android.viewmodel.CartViewModel

class CartFragment: Fragment() {

    val cartViewModel: CartViewModel by activityViewModels()

    lateinit var binding: FragmentCartBinding

    lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.cartViewModel = cartViewModel

        setupObservers()

        initCartAdapter()
        binding.rvCart.adapter = adapter
        binding.rvCart.itemAnimator?.changeDuration = 0

        return binding.root
    }

    private fun setupObservers() {
        cartViewModel.cart.observe(this, { cart ->
            if (cart != null) {
                adapter.products = cart.toList().sortedBy { it.id }
                adapter.notifyDataSetChanged()
            }
        })

        cartViewModel.placingOrderStatus.observe(this, { placingStatus ->
            when(placingStatus) {
                CartViewModel.PlacingOrderStatus.WAITING -> return@observe
                CartViewModel.PlacingOrderStatus.PLACING -> CustomProgressDialogHandler.createProgressDialog(childFragmentManager, "Placing order")
                CartViewModel.PlacingOrderStatus.FINISHED -> {
                    CustomProgressDialogHandler.dismissDialog()
                    cartViewModel.placingOrderStatus.value = CartViewModel.PlacingOrderStatus.WAITING
                    cartViewModel.placedOrderId?.let { orderId ->
                        val action = CartFragmentDirections.actionCartFragmentToOrderDetailFragment().setOrderId(orderId)
                        findNavController().navigate(action)
                    } ?: run {
                        WidgetUtils.createMessageDialog(requireContext(), "Failed", "Order ID unknown.")
                    }

                    }
                CartViewModel.PlacingOrderStatus.ERROR -> {
                    CustomProgressDialogHandler.dismissDialog()
                }
            }
        })
    }

    private fun initCartAdapter() {
        adapter = CartAdapter(requireContext(), viewLifecycleOwner, object: CartAdapterListener {
            override fun onClick(product: Product) {
            }

            override fun onIncreaseClick(product: Product, position: Int) {
                if (product.count < 9) {
                    product.count++
                    adapter.notifyItemChanged(position)
                    cartViewModel.cart.value = adapter.products?.toHashSet()
                }
            }

            override fun onDecreaseClick(product: Product, position: Int) {
                if (product.count > 1) {
                    product.count--
                    adapter.notifyItemChanged(position)
                    cartViewModel.cart.value = adapter.products?.toHashSet()
                }
            }

            override fun onDeleteClick(product: Product, position: Int) {
                val dialogInterfaceListener = DialogInterface.OnClickListener { dialogInterface, position ->
                    when (position) {
                        DialogInterface.BUTTON_POSITIVE -> { cartViewModel.deleteItemFromCart(product) }
                        else -> dialogInterface.dismiss()
                    }
                }

                val dialog = WidgetUtils.createConfirmDialog(requireContext(), "Are You sure?",
                    "Delete the selected item?", dialogInterfaceListener, true)

                dialog.show()
            }
        })
    }

}