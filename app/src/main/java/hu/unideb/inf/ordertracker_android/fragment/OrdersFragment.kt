package hu.unideb.inf.ordertracker_android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import hu.unideb.inf.ordertracker_android.R
import hu.unideb.inf.ordertracker_android.adapter.*
import hu.unideb.inf.ordertracker_android.databinding.FragmentOrdersBinding
import hu.unideb.inf.ordertracker_android.model.api.Order
import hu.unideb.inf.ordertracker_android.viewmodel.OrdersViewModel

class OrdersFragment: Fragment() {

    val ordersViewModel: OrdersViewModel by viewModels()

    lateinit var binding: FragmentOrdersBinding

    private var adapter: OrderAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false)
        binding.lifecycleOwner = viewLifecycleOwner


        binding.swipeRefresh.setColorSchemeColors(requireContext().getColor(R.color.design_default_color_primary))

        setupAdapter()

        setupObservers()

        setupListeners()

        return binding.root
    }

    private fun setupAdapter() {
        if (adapter == null) {
            adapter = OrderAdapter(requireContext(), object : OrderAdapterListener {
                override fun onClick(order: Order) {
                    val action = OrdersFragmentDirections.actionOrdersFragmentToOrderDetailFragment().setOrderId(order.id ?: -1)
                    findNavController().navigate(action)
                }
            })
        }

        binding.rvOrders.adapter = adapter
    }

    private fun setupObservers() {
        ordersViewModel.orders.observe(this) { orders ->
            if (orders != null) {
                adapter?.setItems(orders)
                adapter?.notifyDataSetChanged()
            }
        }

        ordersViewModel.isFetchingInProgress.observe(this, { inProgress ->
            binding.swipeRefresh.isRefreshing = inProgress
        })
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            ordersViewModel.fetchOrders()
        }
    }

    override fun onResume() {
        super.onResume()

        if (ordersViewModel.orders.value.isNullOrEmpty()) {
            ordersViewModel.fetchOrders()
        }
    }

}