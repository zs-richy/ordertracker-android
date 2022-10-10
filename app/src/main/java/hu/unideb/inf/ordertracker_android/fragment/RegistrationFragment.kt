package hu.unideb.inf.ordertracker_android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import hu.unideb.inf.ordertracker_android.NavigationDirections
import hu.unideb.inf.ordertracker_android.R
import hu.unideb.inf.ordertracker_android.databinding.FragmentRegistrationBinding
import hu.unideb.inf.ordertracker_android.dialog.CustomProgressDialogHandler
import hu.unideb.inf.ordertracker_android.util.WidgetUtils
import hu.unideb.inf.ordertracker_android.viewmodel.RegistrationViewModel

class RegistrationFragment: Fragment() {

    lateinit var binding: FragmentRegistrationBinding

    private val registrationViewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.registrationViewModel = registrationViewModel

        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            registrationViewModel.registrationStatus.collect { registrationStatus ->
                when(registrationStatus) {
                    RegistrationViewModel.RegistrationStatus.PROGRESS ->
                        CustomProgressDialogHandler.createProgressDialog(childFragmentManager, "Registration in progress")
                    RegistrationViewModel.RegistrationStatus.SUCCESS -> {
                        CustomProgressDialogHandler.dismissDialog()
                        findNavController().navigate(NavigationDirections.actionGlobalToLoginfragmentFragment())
                    }
                    RegistrationViewModel.RegistrationStatus.ERROR -> {
                        CustomProgressDialogHandler.dismissDialog()
                        WidgetUtils.createMessageDialog(requireContext(), "Alert", "Failed")
                    }
                }
            }
        }
    }

}