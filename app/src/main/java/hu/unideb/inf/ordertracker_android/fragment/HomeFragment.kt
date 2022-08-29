package hu.unideb.inf.ordertracker_android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import hu.unideb.inf.ordertracker_android.R
import hu.unideb.inf.ordertracker_android.databinding.FragmentHomeBinding
import hu.unideb.inf.ordertracker_android.handler.SharedPreferencesHandler
import hu.unideb.inf.ordertracker_android.util.FileUtils
import hu.unideb.inf.ordertracker_android.viewmodel.HomeViewModel
import hu.unideb.inf.ordertracker_android.viewmodel.UserViewModel
import java.lang.Exception

class HomeFragment : Fragment() {

    val userViewModel: UserViewModel by activityViewModels()
    val homeViewModel: HomeViewModel by viewModels()

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.homeViewModel = homeViewModel

        binding.lifecycleOwner = viewLifecycleOwner

        setupObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.updateSessionState()

        if (userViewModel.user.value?.isTokenValid == true) {
//            Snackbar.make(binding.root, "Welcome back! :)", 1500).show()
        } else {
            handleUserNavigation()
        }
    }

    private fun setupObservers() {
        homeViewModel.welcomeImage.observe(this) { welcomeImage ->
            welcomeImage?.let {
                try {
                    val imageFile =
                        FileUtils.findPath(requireContext(), FileUtils.Directory.IMAGES, it)
                    Glide.with(requireContext())
                        .load(imageFile)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(false)
                        .placeholder(
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_burger)
                                ?.also { it.setTint(ContextCompat.getColor(requireContext(), R.color.black)) })
                        .into(binding.ivWelcome)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
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