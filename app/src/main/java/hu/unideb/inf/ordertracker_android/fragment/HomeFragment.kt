package hu.unideb.inf.ordertracker_android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import hu.unideb.inf.ordertracker_android.R
import hu.unideb.inf.ordertracker_android.databinding.FragmentHomeBinding
import hu.unideb.inf.ordertracker_android.handler.SharedPreferencesHandler
import hu.unideb.inf.ordertracker_android.viewmodel.UserViewModel

class HomeFragment: Fragment() {

    val userViewModel: UserViewModel by activityViewModels()

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        lifecycleScope.launchWhenStarted {
            userViewModel.loginState.collect({ state ->
                Snackbar.make(binding.root, "Welcome! $state :)", 1500).show()
            })
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.updateSessionState()

        if (userViewModel.user.value?.isTokenValid == true) {
//            Snackbar.make(binding.root, "Welcome! :)", 1500).show()
        } else {
            handleUserNavigation()
        }
    }

    private fun handleUserNavigation() {
        if (SharedPreferencesHandler.getUsername(requireContext()) != null) {
            val args = bundleOf("redirectToLogin" to true)
            findNavController().navigate(R.id.action_home_fragment_to_authentication_fragment, args)
        } else {
            findNavController().navigate(R.id.action_home_fragment_to_authentication_fragment)
        }
    }

}