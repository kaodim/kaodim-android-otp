package com.kaodim.kaodim_otp_library.fragments.otp_verification_fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaodim.design.components.toast.ToastBanner;
import com.kaodim.kaodim_otp_library.R;
import com.kaodim.kaodim_otp_library.helpers.KaodimPinEntryEditText;

public class OTPVerificationFragment extends Fragment implements OTPVerificationViewInterface {

    TextView tvResendTimeRemaining;
    LinearLayout llResendPanel;
    LinearLayout llResendCounterPanel;
    KaodimPinEntryEditText etOTPInput;
    Button btnVerify;
    TextView tvMobileNumber;
    RelativeLayout rlProgressView;
    LinearLayout topMessage;
    TextView tvChangeNumber;
    TextView tvError;

    private static final String ARG_MOBILE_NUMBER = "mobileNumber";
    private static final String ARG_OTP_EVENT_TYPE = "otp_event_type";

    private int counter = 30;
    private OTPVerificationPresenter presenter;
    private Context context;
    private String mobileNumber;
    private OTPVerificationListener listener;
    private String otpEvent;
    private String packageName;

    public interface OTPVerificationListener {
        void onOTPResendRequested();
        void onOTPVerifyRequested(String code);
    }

    public OTPVerificationFragment() {
        // Required empty public constructor
    }

    public static OTPVerificationFragment newInstance(String otpEvent, String mobileNumber, boolean requestOnStartup) {
        OTPVerificationFragment fragment = new OTPVerificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OTP_EVENT_TYPE, otpEvent);
        args.putString(ARG_MOBILE_NUMBER, mobileNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mobileNumber = getArguments().getString(ARG_MOBILE_NUMBER);
            otpEvent = getArguments().getString(ARG_OTP_EVENT_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otpverification, container, false);

        context = view.getContext();

        tvResendTimeRemaining = view.findViewById(R.id.tvResendTimeRemaining);
        llResendPanel = view.findViewById(R.id.llResendPanel);
        llResendCounterPanel = view.findViewById(R.id.llResendCounterPanel);
        etOTPInput = view.findViewById(R.id.etOTPInput);
        btnVerify = view.findViewById(R.id.btnVerify);
        tvMobileNumber = view.findViewById(R.id.tvMobileNumber);
        rlProgressView = view.findViewById(R.id.rlProgressView);
        topMessage = view.findViewById(R.id.lvTopMessage);
        tvChangeNumber = view.findViewById(R.id.tvChangeNumber);
        tvError = view.findViewById(R.id.tvError);

        presenter = new OTPVerificationPresenter(this);

        setEvents();

        setData();

        onOTPRequested();

        return view;
    }

    public void setCode(String otpCode) {
        if (otpCode != null) {
            etOTPInput.setText(otpCode);
        }
    }

    private void setData() {
        tvMobileNumber.setText(getActivity().getString(R.string.enter_the_code) + " " + mobileNumber);

//        if (requestOnStartup) {
//            //parent activity has not requested OTP. Fragment will request for OTP
//            requestOTP();
//        } else {
//            //parent activity has requested OTP. Counter will be started
//            resetCounter();
//        }
    }

    private void showProgress() {
        rlProgressView.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        rlProgressView.setVisibility(View.GONE);
    }

    private void setEvents() {

        llResendPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestOTP();
            }
        });

        etOTPInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String otpInput = etOTPInput.getText().toString();
                presenter.toggleVerifyButton(otpInput);
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onOTPVerifyRequested(etOTPInput.getText().toString());
//                presenter.verifyOTP(etOTPInput.getText().toString(), otpEvent, mobileNumber, getDeviceId());
            }
        });

        tvChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void requestOTP() {
        showProgress();
        listener.onOTPResendRequested();
    }

    private void resetCounter() {
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                hideResendCode();

                presenter.updateResetCounter(counter);

                if (counter > 0) {
                    counter--;
                }
            }

            public void onFinish() {
                displayResendCode();
            }
        }.start();
    }

    private void hideResendCode() {
        llResendCounterPanel.setVisibility(View.VISIBLE);
        llResendPanel.setVisibility(View.GONE);
    }

    private void displayResendCode() {
        llResendCounterPanel.setVisibility(View.GONE);
        llResendPanel.setVisibility(View.VISIBLE);
        counter = 30;
    }

    @Override
    public void enableVerifyButton() {
        btnVerify.setEnabled(true);
    }

    @Override
    public void disableVerifyButton() {
        btnVerify.setEnabled(false);
    }

    @Override
    public void updateResetCounterText(String value) {
        tvResendTimeRemaining.setText(value);
    }

    @Override
    public void onOTPRequested() {
        resetCounter();
        hideProgress();
    }

    @Override
    public void onOTPFilled() {
//        presenter.verifyOTP(etOTPInput.getText().toString(), otpEvent, mobileNumber, getDeviceId());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OTPVerificationListener) {
            listener = (OTPVerificationListener) context;
        }
    }

    @Override
    public void displayError(String errorMessage) {
        hideProgress();

        displayResendCode();

        tvError.setVisibility(View.VISIBLE);

        tvError.setText(errorMessage);
    }

    @Override
    public void hideError() {
        tvError.setVisibility(View.GONE);
        tvError.setText("");
    }

}
