package com.kaodim.kaodim_otp_library.fragments.otp_verification_fragment;

public interface OTPVerificationPresenterInterface {
    void toggleVerifyButton(String otpValue);
    void updateResetCounter(int counter);
}
