package com.kaodim.kaodim_otp_library.fragments.phone_collection_fragment;

public class PhoneCollectionPresenter implements PhoneCollectionPresenterInterface {

    PhoneCollectionViewInterface view;
    private boolean countrySelectedDisplayed = false;

    public PhoneCollectionPresenter(PhoneCollectionViewInterface view) {
        this.view = view;
    }

    @Override
    public void validateInput(String mobileNumber) {
        if (mobileNumber.length() > 0) {
            view.enableLoginButton();
        } else {
            view.disableLoginButton();
        }
    }
}
