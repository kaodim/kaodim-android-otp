package com.kaodim.kaodim_otp_library.fragments.notp_verification_fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.kaodim.kaodim_otp_library.R
import kotlinx.android.synthetic.main.fragment_notp_verification.*

class NOTPVerificationFragment : Fragment() {
    var listener: NOTPVerificationListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notp_verification, container, false)
    }

    interface NOTPVerificationListener {
        fun onFragmentReady()
        fun onChangeNumberRequested()
        fun onSendCodeViaSMSRequested()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onGetInputData()
        onSetupViewModel()
        onBindData(view)
        listener?.onFragmentReady()
        setupUI()
    }

    private fun onGetInputData() {}

    private fun onSetupViewModel() {}

    private fun onBindData(view: View) {
        tvEditPhoneNumber.setOnClickListener { listener?.onChangeNumberRequested() }
        tvNotpSMSCode.setOnClickListener { listener?.onSendCodeViaSMSRequested() }
    }

    private fun setupUI() {
        val target = DrawableImageViewTarget(ivHeader)
        Glide.with(this)
                .load(R.drawable.ripple_animation)
                .into(target)

//        setVerifyingText(verifyingText)
    }

    fun setVerifyingText(text: String) {
        tvVerifying.text = text
    }

    fun setErrorText(text: String) {
        tvNotpErrorMessage.text = text
    }

    fun setSMSCodeText(text: String) {
        tvNotpSMSCode.text = text
    }

    fun setLoadingVisibility(visible: Int) {
        tvNotpSubtitle.visibility = visible
        ivHeader.visibility = visible
    }

    fun setSubtitleText(text: String) {
        tvNotpSubtitle.text = text
    }

    fun setEditNumberText(text: String) {
        tvEditPhoneNumber.text = text
    }


    fun setErrorVisibility(visible: Int) {
        tvNotpErrorMessage.visibility = visible
    }

    fun setSMSCodeVisibility(visible: Int) {
        tvNotpSMSCode.visibility = visible
    }

    companion object {

        fun newInstance(): NOTPVerificationFragment {
            val fragment = NOTPVerificationFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
