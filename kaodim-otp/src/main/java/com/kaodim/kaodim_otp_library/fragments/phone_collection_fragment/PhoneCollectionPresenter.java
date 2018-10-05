package com.kaodim.kaodim_otp_library.fragments.phone_collection_fragment;

public class PhoneCollectionPresenter implements PhoneCollectionPresenterInterface {

    PhoneCollectionViewInterface view;
    private boolean countrySelectedDisplayed = false;

    public PhoneCollectionPresenter(PhoneCollectionViewInterface view) {
        this.view = view;
    }

    @Override
    public void validateInput(String mobileNumber, boolean isValidNumber) {
        if (mobileNumber.length() > 0 && isValidNumber) {
            view.enableLoginButton();
        } else {
            view.disableLoginButton();
        }
    }

    @Override
    public void setCountryFormat(String country) {
        String countryISOCode = "";
        switch(country) {
            case "Malaysia":
                countryISOCode = "MY";
                break;
            case "Singapore":
                countryISOCode = "SG";
                break;
            case "Indonesia":
                countryISOCode = "ID";
                break;
            case "Philippines":
                countryISOCode = "PH";
                break;
        }
        view.onCountryFormatReady(countryISOCode);
    }
}
