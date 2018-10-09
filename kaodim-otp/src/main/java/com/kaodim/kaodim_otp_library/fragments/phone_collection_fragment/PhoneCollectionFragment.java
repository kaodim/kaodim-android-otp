package com.kaodim.kaodim_otp_library.fragments.phone_collection_fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaodim.design.components.MobileInputLayout;
import com.kaodim.design.components.models.CountryCodeRowItem;
import com.kaodim.kaodim_otp_library.R;

import java.util.ArrayList;

public class PhoneCollectionFragment extends Fragment implements MobileInputLayout.MobileInputEventListener, PhoneCollectionViewInterface {

    TextView tvViewTitle, tvViewDescription;
    MobileInputLayout etMobileNumber;
    RelativeLayout rlProgressView;
    Button btnNext;

    private static final String ARG_MOBILE_NUMBER = "mobileNumber";
    private static final String ARG_TITLE_TEXT = "titleText";
    private static final String ARG_DESCRIPTION_TEXT = "descriptionText";
    private static final String ARG_HINT_TITLE_TEXT = "hintTitle";
    private static final String ARG_COUNTRY_NAME_TEXT = "countryName";
    private static final String ARG_FLAVOR_TEXT = "flavor";
    private String mobileNumber;
    public PhoneCollectionPresenter presenter;
    PhoneDataCollectionListener listener;
    private String titleText = "";
    private String descriptionText = "";
    private String hintTitle = "";
    Context context;
    String countryName, flavor, countryCode;
    ArrayList<CountryCodeRowItem> countryCodes = new ArrayList<>();
    public boolean valid = false;

    public interface PhoneDataCollectionListener {
        void onFragmentReady();

        void onNextButtonPress();

        void onBackButtonPress();

        void onNumberChanged(String countryCode, String mobileNumber);

        void getPhoneFormatIsValid(String phoneNumber);
    }

    public PhoneCollectionFragment() {
        // Required empty public constructor
    }

    public static PhoneCollectionFragment newInstance(String mobileNumber, String titleText, String descriptionText, String hintTitle, String countryName, String flavor) {
        PhoneCollectionFragment fragment = new PhoneCollectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOBILE_NUMBER, mobileNumber);
        args.putString(ARG_TITLE_TEXT, titleText);
        args.putString(ARG_DESCRIPTION_TEXT, descriptionText);
        args.putString(ARG_HINT_TITLE_TEXT, hintTitle);
        args.putString(ARG_COUNTRY_NAME_TEXT, countryName);
        args.putString(ARG_FLAVOR_TEXT, flavor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mobileNumber = getArguments().getString(ARG_MOBILE_NUMBER);
            titleText = getArguments().getString(ARG_TITLE_TEXT);
            descriptionText = getArguments().getString(ARG_DESCRIPTION_TEXT);
            hintTitle = getArguments().getString(ARG_HINT_TITLE_TEXT);
            countryName = getArguments().getString(ARG_COUNTRY_NAME_TEXT);
            flavor = getArguments().getString(ARG_FLAVOR_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_collection, container, false);

        context = view.getContext();

        tvViewTitle = view.findViewById(R.id.tvViewTitle);
        tvViewDescription = view.findViewById(R.id.tvViewDescription);
        btnNext = view.findViewById(R.id.btnNext);
        etMobileNumber = view.findViewById(R.id.etMobileNumber);
        rlProgressView = view.findViewById(R.id.rlProgressView);

        presenter = new PhoneCollectionPresenter(this);

        setEvents();

        setupCountryCodes();

        presenter.setCountryFormat(countryName);

        etMobileNumber.setMobileInputEventListener(this);

        if (listener != null)
            listener.onFragmentReady();

        return view;
    }

    @Override
    public void onCountryFormatReady(String countryISOCode) {
        etMobileNumber.initialize(context, countryCodes, countryISOCode, mobileNumber);
    }

    private void setEvents() {

        disableLoginButton();

        if (!titleText.equals(""))
            tvViewTitle.setText(titleText);

        if (!descriptionText.equals(""))
            tvViewDescription.setText(descriptionText);

        if (!hintTitle.equals(""))
            etMobileNumber.setHintTitle(hintTitle);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onNextButtonPress();
            }
        });
    }

    private void setupCountryCodes() {
        countryCodes.clear();
        if (flavor.contains("kaodim")) {
            countryCodes.add(new CountryCodeRowItem("(+60)", R.drawable.ic_flag_my));
            countryCodes.add(new CountryCodeRowItem("(+65)", R.drawable.ic_flag_sg));
        }
        if (flavor.contains("beres")) {
            countryCodes.add(new CountryCodeRowItem("(+62)", R.drawable.ic_flag_id));
        }
        if (flavor.contains("gawin")) {
            countryCodes.add(new CountryCodeRowItem("(+63)", R.drawable.ic_flag_ph));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PhoneDataCollectionListener) {
            listener = (PhoneDataCollectionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PhoneDataCollectionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void enableLoginButton() {
        btnNext.setEnabled(true);
    }

    @Override
    public void disableLoginButton() {
        btnNext.setEnabled(false);
    }

    @Override
    public void setMobileNumber(String mobileNumber) {
        //boolean value is just a dummy value, is not being used
        onMobileValueChanged(countryCode, mobileNumber, false);
    }

    @Override
    public String getMobileNumber() {
        return etMobileNumber.getText();
    }

    public void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void onCountrySelected(CountryCodeRowItem item) {
        switch (item.code) {
            case "+60":
                etMobileNumber.setValidationCountry("MY");
                break;
            case "+65":
                etMobileNumber.setValidationCountry("SG");
                break;
        }

        if (listener != null)
            listener.onNumberChanged(item.code, mobileNumber);
    }

    @Override
    public void onMobileValueChanged(String countryCode, String value, boolean b) {
        disableLoginButton();
        this.mobileNumber = value;
        if (listener != null){
            listener.onNumberChanged(countryCode, mobileNumber);
        }

        //uses the API validation from the activity
        if (listener != null)
            Log.d("REGEXTEST", "VALIDATE PHONE : " + mobileNumber);
            listener.getPhoneFormatIsValid(mobileNumber);

    }


    public void showProgress() {

    }

    public void hideProgress() {

    }
}
