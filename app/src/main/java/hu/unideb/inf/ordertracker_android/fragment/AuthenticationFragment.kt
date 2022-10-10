package hu.unideb.inf.ordertracker_android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import hu.unideb.inf.ordertracker_android.NavigationDirections
import hu.unideb.inf.ordertracker_android.R
import hu.unideb.inf.ordertracker_android.databinding.FragmentAuthenticationBinding
import hu.unideb.inf.ordertracker_android.viewmodel.UserViewModel

class AuthenticationFragment: Fragment() {

    lateinit var binding: FragmentAuthenticationBinding

    val userViewModel: UserViewModel by activityViewModels()

    var argumentsProcessed = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_authentication, container, false)

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToLoginFragment())
        }

        binding.btnSignup.setOnClickListener {
            findNavController().navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToRegistrationFragment())
        }

        userViewModel.clearUserData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: AuthenticationFragmentArgs by navArgs()

        if (args.redirectToLogin && !argumentsProcessed) {
            argumentsProcessed = true
            findNavController().navigate(AuthenticationFragmentDirections.actionAuthenticationFragmentToLoginFragment())
        }
    }


}