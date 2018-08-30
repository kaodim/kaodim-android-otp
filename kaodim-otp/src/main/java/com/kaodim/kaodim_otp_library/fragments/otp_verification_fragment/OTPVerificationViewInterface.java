package com.kaodim.kaodim_otp_library.fragments.otp_verification_fragment;

public interface OTPVerificationViewInterface {
    void enableVerifyButton();
    void disableVerifyButton();
    void updateResetCounterText(String value);
    void onOTPRequested();
    void onOTPFilled();
    void displayError(String errorMessage);
    void hideError();
}
