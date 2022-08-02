package hu.unideb.inf.ordertracker_android.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import hu.unideb.inf.ordertracker_android.R
import hu.unideb.inf.ordertracker_android.databinding.FragmentOrderDetailsBinding
import hu.unideb.inf.ordertracker_android.util.WidgetUtils
import hu.unideb.inf.ordertracker_android.viewmodel.OrderDetailsViewModel
import hu.unideb.inf.ordertracker_android.viewmodel.OrdersViewModel

class OrderDetailsFragment: Fragment() {

    val args: OrderDetailsFragmentArgs by navArgs()

    val orderDetailsViewModel: OrderDetailsViewModel by viewModels()

    lateinit var binding: FragmentOrderDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_details, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.orderDetailsViewModel = orderDetailsViewModel

        binding.swipeRefresh.setColorSchemeColors(requireContext().getColor(R.color.design_default_color_primary))

        orderDetailsViewModel.initByOrderId(args.orderId)

        setupObservers()

        setupListeners()

        return binding.root
    }

    private fun setupObservers() {
        orderDetailsViewModel.isFetchingOrderData.observe(this) { isFetching ->
            binding.swipeRefresh.isRefreshing = isFetching
        }
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            orderDetailsViewModel.fetchOrderDetails()
        }
    }

    override fun onResume() {
        super.onResume()

//        orderDetailsViewModel.initByOrderId()
    }

}