package com.kaodim.kaodim_otp_library.fragments.otp_verification_fragment_new

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kaodim.kaodim_otp_library.R
import com.kaodim.kaodim_otp_library.helpers.KaodimPinEntryEditText
import kotlinx.android.synthetic.main.fragment_new_otp_verification.*

class OTPVerificationFragment : Fragment() {
    private var listener: OTPVerificationListener? = null

    interface OTPVerificationListener {
        fun onFragmentReady()
        fun onOTPResendRequested()
        fun onOTPResendRequestPhone()
        fun onOTPVerifyRequested(code: String?)
        fun onChangeNumberRequested()
        fun onTextChanged(otpInput: String?)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_new_otp_verification, container, false)
        setEvents()
        listener?.onFragmentReady()
        return view
    }

    private fun setEvents() {
        tvResendCode.setOnClickListener { v: View? -> requestOTP() }
        tvResendCodeViaCall.setOnClickListener { v: View? -> requestOTPViaPhone() }
        etOTPInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val otpInput: String = etOTPInput.text.toString()
                listener?.onTextChanged(otpInput)
                if (otpInput.length >= 6) {
                    listener?.onOTPVerifyRequested(otpInput)
                }
            }
        })
        tvChangeNumber.setOnClickListener { v: View? -> changeNumberRequested() }
    }

    private fun changeNumberRequested() {
        listener?.onChangeNumberRequested()
    }

    private fun requestOTP() {
        listener?.onOTPResendRequested()
    }

    private fun requestOTPViaPhone() {
        listener?.onOTPResendRequestPhone()
    }

    fun setListener(listener: OTPVerificationListener?) {
        this.listener = listener
    }

    fun clearListener() {
        listener = null
    }

    fun setChangeNumberText(text: String) {
        tvChangeNumber.text = text
    }

    fun setErrorText(text: String) {
        tvError.text = text
    }

    fun setErrorVisibility(visibility: Int) {
        tvError.visibility = visibility
    }

    fun setVerifyingText(text: String) {
        tvVerifying.text = text
    }

    fun setOTPText(text: String) {
        etOTPInput.setText(text)
    }

    fun setResendCodeText(text: String) {
        tvResendCode.text = text
    }

    fun setResendCodeTextVisibility(visibility: Int) {
        tvResendCode.visibility = visibility
    }

    fun setResendCodeCallText(text: String) {
        tvResendCodeViaCall.text = text
    }

    fun setResendCodeCallTextVisibility(visibility: Int) {
        tvResendCodeViaCall.visibility = visibility
    }

    fun setCountdownText(text: String) {
        tvResendTimeRemaining.text = text
    }

    fun setCountdownTextVisibility(visibility: Int) {
        tvResendTimeRemaining.visibility = visibility
    }

    companion object {
        fun newInstance(): OTPVerificationFragment {
            val fragment = OTPVerificationFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}