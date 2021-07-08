package com.kaodim.kaodim_android_otp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kaodim.kaodim_otp_library.fragments.otp_verification_fragment_new.OTPVerificationFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadOTPVerificationFragment();
    }

    private void loadOTPVerificationFragment() {
        OTPVerificationFragment fragment = OTPVerificationFragment.Companion.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.llContainer, fragment)
                .commit();
        fragment.setListener(new OTPVerificationFragment.OTPVerificationListener() {
            @Override
            public void onTextChanged(String otpInput) {

            }

            @Override
            public void onChangeNumberRequested() {

            }

            @Override
            public void onOTPVerifyRequested(String code) {

            }

            @Override
            public void onOTPResendRequestPhone() {

            }

            @Override
            public void onOTPResendRequested() {

            }

            @Override
            public void onFragmentReady() {

            }
        });
    }
}
