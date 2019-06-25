package com.kaodim.kaodim_otp_library.fragments.otp_verification_fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    TextView tvTitle;
    private CountDownTimer countDownTimer;

    private static final String ARG_FRAGMENT_TITLE = "fragmentTitle";
    private static final String ARG_FRAGMENT_SUBTITLE = "fragmentSubtitle";
    private static final String ARG_MOBILE_NUMBER = "mobileNumber";
    private static final String ARG_OTP_EVENT_TYPE = "otp_event_type";
    private static final String ARG_ALLOW_NUMBER_CHANGE = "allow_number_change";
    private static final String ARG_BUTTON_TEXT = "buttonText";

    private OTPVerificationPresenter presenter;
    private Context context;
    private String mobileNumber;
    private OTPVerificationListener listener;
    private String otpEvent;
    private String packageName;
    private String fragmentTitle = "";
    private String fragmentSubtitle = "";
    private String buttonText = "";
    private boolean allowNumberChange = false;
    private int counter = 30;

    public interface OTPVerificationListener {
        void onFragmentReady();

        void onOTPResendRequested();

        void onOTPVerifyRequested(String code);

        void onChangeNumberRequested();
    }

    public OTPVerificationFragment() {
        // Required empty public constructor
    }

    public static OTPVerificationFragment newInstance(String fragmentTitle, String fragmentSubtitle, String otpEvent, String mobileNumber, String buttonText, boolean requestOnStartup, boolean allowNumberChange) {
        OTPVerificationFragment fragment = new OTPVerificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FRAGMENT_TITLE, fragmentTitle);
        args.putString(ARG_FRAGMENT_SUBTITLE, fragmentSubtitle);
        args.putString(ARG_OTP_EVENT_TYPE, otpEvent);
        args.putString(ARG_MOBILE_NUMBER, mobileNumber);
        args.putString(ARG_BUTTON_TEXT, buttonText);
        args.putBoolean(ARG_ALLOW_NUMBER_CHANGE, allowNumberChange);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fragmentTitle = getArguments().getString(ARG_FRAGMENT_TITLE);
            fragmentSubtitle = getArguments().getString(ARG_FRAGMENT_SUBTITLE);
            mobileNumber = getArguments().getString(ARG_MOBILE_NUMBER);
            buttonText = getArguments().getString(ARG_BUTTON_TEXT);
            otpEvent = getArguments().getString(ARG_OTP_EVENT_TYPE);
            allowNumberChange = getArguments().getBoolean(ARG_ALLOW_NUMBER_CHANGE);
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
        tvTitle = view.findViewById(R.id.tvTitle);

        if(otpEvent.equals("sign_up")) {
            tvTitle.setVisibility(View.VISIBLE);
        }
        else {
            tvTitle.setVisibility(View.GONE);
        }

        presenter = new OTPVerificationPresenter(this);

        setEvents();

        setData();

        if (listener != null)
            listener.onFragmentReady();

        return view;
    }

    public void setCode(String otpCode) {
        if (otpCode != null) {
            etOTPInput.setText(otpCode);
            counter = 0;
            countDownTimer.cancel();
            displayResendCode();
        }
    }

    public void clearInput() {
        etOTPInput.setText("");
    }

    private void setData() {
        tvTitle.setText(fragmentTitle);
        tvMobileNumber.setText(fragmentSubtitle);
        btnVerify.setText(buttonText);

        if(!allowNumberChange) {
            tvChangeNumber.setVisibility(View.GONE);
        }
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
                hideResendCode();
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
                if (listener != null)
                    listener.onOTPVerifyRequested(etOTPInput.getText().toString());
//                presenter.verifyOTP(etOTPInput.getText().toString(), otpEvent, mobileNumber, getDeviceId());
            }
        });

        tvChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onChangeNumberRequested();
            }
        });
    }

    private void requestOTP() {
        showProgress();
        Log.d("KAODEVBUG", "Showing progress");
        listener.onOTPResendRequested();
    }

    private void resetCounter() {
        counter = 31;
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                hideResendCode();

                if (counter > 0) {
                    counter--;
                }

                presenter.updateResetCounter(counter);
            }

            public void onFinish() {
                counter = 0;
                countDownTimer.cancel();
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
//        if (context instanceof OTPVerificationListener) {
//            listener = (OTPVerificationListener) context;
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setListener(OTPVerificationListener listener) {
        this.listener = listener;
    }

    public void clearListener(){
        this.listener = null;
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
