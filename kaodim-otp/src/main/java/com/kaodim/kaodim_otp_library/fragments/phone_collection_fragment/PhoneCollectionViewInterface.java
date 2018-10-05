package com.kaodim.kaodim_otp_library.fragments.phone_collection_fragment;

public interface PhoneCollectionViewInterface {
    void enableLoginButton();

    void disableLoginButton();

    void setMobileNumber(String mobileNumber);

    void onCountryFormatReady(String countryISOCode);
}
