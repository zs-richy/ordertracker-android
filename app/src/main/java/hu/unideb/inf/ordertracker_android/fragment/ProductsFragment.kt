package hu.unideb.inf.ordertracker_android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import hu.unideb.inf.ordertracker_android.R
import hu.unideb.inf.ordertracker_android.adapter.ProductAdapter
import hu.unideb.inf.ordertracker_android.adapter.ProductAdapterListener
import hu.unideb.inf.ordertracker_android.databinding.FragmentProductsBinding
import hu.unideb.inf.ordertracker_android.model.api.Product
import hu.unideb.inf.ordertracker_android.viewmodel.CartViewModel
import hu.unideb.inf.ordertracker_android.viewmodel.ProductsViewModel

class ProductsFragment: Fragment() {

    lateinit var binding: FragmentProductsBinding

    val productsViewModel: ProductsViewModel by activityViewModels()

    val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        lifecycleScope.launchWhenStarted {
//            productsViewModel.sharedFlow.collect { value ->
//                Log.d("FLOW", "flow collected: $value")
//                if (value == "start") {
//                    CustomProgressDialogHandler.createProgressDialog(
//                        childFragmentManager,
//                        "STARTED"
//                    )
//                } else if (value == "finish") {
//                    CustomProgressDialogHandler.dismissDialog()
//                }
//            }
//        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_products, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.floatingCartLayout.lifecycleOwner = viewLifecycleOwner

        binding.swipeRefresh.setColorSchemeColors(requireContext().getColor(R.color.design_default_color_primary))

        val adapter = ProductAdapter(requireContext(), object: ProductAdapterListener {
            override fun onClick(product: Product) {
                cartViewModel.addItemToCart(product)
            }
        })
        binding.rvProducts.adapter = adapter

        productsViewModel.products.observe(this, { products ->
            if (products != null) {
                adapter.products = products.productList
                adapter.notifyDataSetChanged()
            }
        })

        productsViewModel.isRefreshing.observe(this, { refreshing ->
            binding.swipeRefresh.isRefreshing = refreshing
        })

        binding.swipeRefresh.setOnRefreshListener {
            productsViewModel.initProducts()
        }

        binding.floatingCartLayout.btnCart.setOnClickListener {
            if (!cartViewModel.cart.value.isNullOrEmpty()) {
                findNavController().navigate(R.id.action_products_fragment_to_cart_fragment)
            }
        }

        binding.floatingCartLayout.cartViewModel = cartViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
}

    override fun onResume() {
        super.onResume()

        if (productsViewModel.products.value?.productList.isNullOrEmpty()) {
            productsViewModel.initProducts()
        }
    }

}