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
import hu.unideb.inf.ordertracker_android.databinding.FragmentAuthenticationBinding
import hu.unideb.inf.ordertracker_android.viewmodel.UserViewModel

class AuthenticationFragment: Fragment() {

    lateinit var binding: FragmentAuthenticationBinding

    val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_authentication, container, false)

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.login_fragment)
        }

        binding.btnSignup.setOnClickListener {
            findNavController().navigate(R.id.registration_fragment)
        }

        userViewModel.clearUserData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments?.get("redirectToLogin") == true) {
            arguments?.clear()
            findNavController().navigate(R.id.action_authentication_fragment_to_login_fragment)
        }
    }


}