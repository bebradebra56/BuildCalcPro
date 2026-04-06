package com.buildcal.probuild.rgjof.presentation.ui.load

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.buildcal.probuild.MainActivity
import com.buildcal.probuild.R
import com.buildcal.probuild.databinding.FragmentLoadBuildCalcProBinding
import com.buildcal.probuild.rgjof.data.shar.BuildCalcProSharedPreference
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class BuildCalcProLoadFragment : Fragment(R.layout.fragment_load_build_calc_pro) {
    private lateinit var buildCalcProLoadBinding: FragmentLoadBuildCalcProBinding

    private val buildCalcProLoadViewModel by viewModel<BuildCalcProLoadViewModel>()

    private val buildCalcProSharedPreference by inject<BuildCalcProSharedPreference>()

    private var buildCalcProUrl = ""

    private val buildCalcProRequestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        buildCalcProSharedPreference.buildCalcProNotificationState = 2
        buildCalcProNavigateToSuccess(buildCalcProUrl)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buildCalcProLoadBinding = FragmentLoadBuildCalcProBinding.bind(view)

        buildCalcProLoadBinding.buildCalcProGrandButton.setOnClickListener {
            val buildCalcProPermission = Manifest.permission.POST_NOTIFICATIONS
            buildCalcProRequestNotificationPermission.launch(buildCalcProPermission)
        }

        buildCalcProLoadBinding.buildCalcProSkipButton.setOnClickListener {
            buildCalcProSharedPreference.buildCalcProNotificationState = 1
            buildCalcProSharedPreference.buildCalcProNotificationRequest =
                (System.currentTimeMillis() / 1000) + 259200
            buildCalcProNavigateToSuccess(buildCalcProUrl)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                buildCalcProLoadViewModel.buildCalcProHomeScreenState.collect {
                    when (it) {
                        is BuildCalcProLoadViewModel.BuildCalcProHomeScreenState.BuildCalcProLoading -> {

                        }

                        is BuildCalcProLoadViewModel.BuildCalcProHomeScreenState.BuildCalcProError -> {
                            requireActivity().startActivity(
                                Intent(
                                    requireContext(),
                                    MainActivity::class.java
                                )
                            )
                            requireActivity().finish()
                        }

                        is BuildCalcProLoadViewModel.BuildCalcProHomeScreenState.BuildCalcProSuccess -> {
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
                                val buildCalcProNotificationState = buildCalcProSharedPreference.buildCalcProNotificationState
                                when (buildCalcProNotificationState) {
                                    0 -> {
                                        buildCalcProLoadBinding.buildCalcProNotiGroup.visibility = View.VISIBLE
                                        buildCalcProLoadBinding.buildCalcProLoadingGroup.visibility = View.GONE
                                        buildCalcProUrl = it.data
                                    }
                                    1 -> {
                                        if (System.currentTimeMillis() / 1000 > buildCalcProSharedPreference.buildCalcProNotificationRequest) {
                                            buildCalcProLoadBinding.buildCalcProNotiGroup.visibility = View.VISIBLE
                                            buildCalcProLoadBinding.buildCalcProLoadingGroup.visibility = View.GONE
                                            buildCalcProUrl = it.data
                                        } else {
                                            buildCalcProNavigateToSuccess(it.data)
                                        }
                                    }
                                    2 -> {
                                        buildCalcProNavigateToSuccess(it.data)
                                    }
                                }
                            } else {
                                buildCalcProNavigateToSuccess(it.data)
                            }
                        }

                        BuildCalcProLoadViewModel.BuildCalcProHomeScreenState.BuildCalcProNotInternet -> {
                            buildCalcProLoadBinding.buildCalcProStateGroup.visibility = View.VISIBLE
                            buildCalcProLoadBinding.buildCalcProLoadingGroup.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }


    private fun buildCalcProNavigateToSuccess(data: String) {
        findNavController().navigate(
            R.id.action_buildCalcProLoadFragment_to_buildCalcProV,
            bundleOf(BUILD_CALC_PRO_D to data)
        )
    }

    companion object {
        const val BUILD_CALC_PRO_D = "buildCalcProData"
    }
}