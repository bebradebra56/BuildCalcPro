package com.buildcal.probuild.rgjof.presentation.ui.view

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.buildcal.probuild.rgjof.presentation.app.BuildCalcProApplication
import com.buildcal.probuild.rgjof.presentation.ui.load.BuildCalcProLoadFragment
import org.koin.android.ext.android.inject

class BuildCalcProV : Fragment(){

    private lateinit var buildCalcProPhoto: Uri
    private var buildCalcProFilePathFromChrome: ValueCallback<Array<Uri>>? = null

    private val buildCalcProTakeFile: ActivityResultLauncher<PickVisualMediaRequest> = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        buildCalcProFilePathFromChrome?.onReceiveValue(arrayOf(it ?: Uri.EMPTY))
        buildCalcProFilePathFromChrome = null
    }

    private val buildCalcProTakePhoto: ActivityResultLauncher<Uri> = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            buildCalcProFilePathFromChrome?.onReceiveValue(arrayOf(buildCalcProPhoto))
            buildCalcProFilePathFromChrome = null
        } else {
            buildCalcProFilePathFromChrome?.onReceiveValue(null)
            buildCalcProFilePathFromChrome = null
        }
    }

    private val buildCalcProDataStore by activityViewModels<BuildCalcProDataStore>()


    private val buildCalcProViFun by inject<BuildCalcProViFun>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "Fragment onCreate")
        CookieManager.getInstance().setAcceptCookie(true)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (buildCalcProDataStore.buildCalcProView.canGoBack()) {
                        buildCalcProDataStore.buildCalcProView.goBack()
                        Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "WebView can go back")
                    } else if (buildCalcProDataStore.buildCalcProViList.size > 1) {
                        Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "WebView can`t go back")
                        buildCalcProDataStore.buildCalcProViList.removeAt(buildCalcProDataStore.buildCalcProViList.lastIndex)
                        Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "WebView list size ${buildCalcProDataStore.buildCalcProViList.size}")
                        buildCalcProDataStore.buildCalcProView.destroy()
                        val previousWebView = buildCalcProDataStore.buildCalcProViList.last()
                        buildCalcProAttachWebViewToContainer(previousWebView)
                        buildCalcProDataStore.buildCalcProView = previousWebView
                    }
                }

            })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (buildCalcProDataStore.buildCalcProIsFirstCreate) {
            buildCalcProDataStore.buildCalcProIsFirstCreate = false
            buildCalcProDataStore.buildCalcProContainerView = FrameLayout(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                id = View.generateViewId()
            }
            return buildCalcProDataStore.buildCalcProContainerView
        } else {
            return buildCalcProDataStore.buildCalcProContainerView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "onViewCreated")
        if (buildCalcProDataStore.buildCalcProViList.isEmpty()) {
            buildCalcProDataStore.buildCalcProView = BuildCalcProVi(requireContext(), object :
                BuildCalcProCallBack {
                override fun buildCalcProHandleCreateWebWindowRequest(buildCalcProVi: BuildCalcProVi) {
                    buildCalcProDataStore.buildCalcProViList.add(buildCalcProVi)
                    Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "WebView list size = ${buildCalcProDataStore.buildCalcProViList.size}")
                    Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "CreateWebWindowRequest")
                    buildCalcProDataStore.buildCalcProView = buildCalcProVi
                    buildCalcProVi.buildCalcProSetFileChooserHandler { callback ->
                        buildCalcProHandleFileChooser(callback)
                    }
                    buildCalcProAttachWebViewToContainer(buildCalcProVi)
                }

            }, buildCalcProWindow = requireActivity().window).apply {
                buildCalcProSetFileChooserHandler { callback ->
                    buildCalcProHandleFileChooser(callback)
                }
            }
            buildCalcProDataStore.buildCalcProView.buildCalcProFLoad(arguments?.getString(
                BuildCalcProLoadFragment.BUILD_CALC_PRO_D) ?: "")
//            ejvview.fLoad("www.google.com")
            buildCalcProDataStore.buildCalcProViList.add(buildCalcProDataStore.buildCalcProView)
            buildCalcProAttachWebViewToContainer(buildCalcProDataStore.buildCalcProView)
        } else {
            buildCalcProDataStore.buildCalcProViList.forEach { webView ->
                webView.buildCalcProSetFileChooserHandler { callback ->
                    buildCalcProHandleFileChooser(callback)
                }
            }
            buildCalcProDataStore.buildCalcProView = buildCalcProDataStore.buildCalcProViList.last()

            buildCalcProAttachWebViewToContainer(buildCalcProDataStore.buildCalcProView)
        }
        Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "WebView list size = ${buildCalcProDataStore.buildCalcProViList.size}")
    }

    private fun buildCalcProHandleFileChooser(callback: ValueCallback<Array<Uri>>?) {
        Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "handleFileChooser called, callback: ${callback != null}")

        buildCalcProFilePathFromChrome = callback

        val listItems: Array<out String> = arrayOf("Select from file", "To make a photo")
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                0 -> {
                    Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "Launching file picker")
                    buildCalcProTakeFile.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
                1 -> {
                    Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "Launching camera")
                    buildCalcProPhoto = buildCalcProViFun.buildCalcProSavePhoto()
                    buildCalcProTakePhoto.launch(buildCalcProPhoto)
                }
            }
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Choose a method")
            .setItems(listItems, listener)
            .setCancelable(true)
            .setOnCancelListener {
                Log.d(BuildCalcProApplication.BUILD_CALC_PRO_MAIN_TAG, "File chooser canceled")
                callback?.onReceiveValue(null)
                buildCalcProFilePathFromChrome = null
            }
            .create()
            .show()
    }

    private fun buildCalcProAttachWebViewToContainer(w: BuildCalcProVi) {
        buildCalcProDataStore.buildCalcProContainerView.post {
            (w.parent as? ViewGroup)?.removeView(w)
            buildCalcProDataStore.buildCalcProContainerView.removeAllViews()
            buildCalcProDataStore.buildCalcProContainerView.addView(w)
        }
    }


}