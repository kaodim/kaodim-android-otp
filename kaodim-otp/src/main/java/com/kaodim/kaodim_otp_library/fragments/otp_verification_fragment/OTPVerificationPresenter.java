package com.kaodim.kaodim_otp_library.fragments.otp_verification_fragment;

public class OTPVerificationPresenter implements OTPVerificationPresenterInterface {

    OTPVerificationViewInterface view;

    public OTPVerificationPresenter(OTPVerificationViewInterface view) {
        this.view = view;
    }

    @Override
    public void toggleVerifyButton(String otpValue) {
        if (otpValue.length() == 6) {
            view.enableVerifyButton();
        } else {
            view.disableVerifyButton();
        }
    }

    @Override
    public void updateResetCounter(int counter) {
        if (counter < 10) {
            String value = "0:0" + counter;
            view.updateResetCounterText(value);
        } else {
            String value = "0:" + counter;
            view.updateResetCounterText(value);
        }
    }

}
