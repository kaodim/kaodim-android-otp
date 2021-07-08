package com.kaodim.kaodim_otp_library.fragments.phone_collection_fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.kaodim.design.components.MobileInputLayout;
import com.kaodim.design.components.models.CountryCodeRowItem;
import com.kaodim.kaodim_otp_library.R;

import java.util.ArrayList;

public class PhoneCollectionFragment extends Fragment implements MobileInputLayout.MobileInputEventListener, PhoneCollectionViewInterface {

    TextView tvViewTitle, tvViewDescription, tvInfo;
    MobileInputLayout etMobileNumber;
    LinearLayout llPhoneCollectionInfo;
    RelativeLayout rlProgressView;
    Button btnNext;

    private static final String ARG_ENABLE_SELECTION = "enableSelection";
    private static final String ARG_MOBILE_NUMBER = "mobileNumber";
    private static final String ARG_TITLE_TEXT = "titleText";
    private static final String ARG_DESCRIPTION_TEXT = "descriptionText";
    private static final String ARG_HINT_TITLE_TEXT = "hintTitle";
    private static final String ARG_COUNTRY_NAME_TEXT = "countryName";
    private static final String ARG_FLAVOR_TEXT = "flavor";
    private static final String ARG_INFO ="info";
    private static final String ARG_BUTTON_TEXT = "buttonText";

    private String mobileNumber;
    public PhoneCollectionPresenter presenter;
    PhoneDataCollectionListener listener;
    private String titleText = "";
    private String descriptionText = "";
    private String hintTitle = "";
    private String buttonText = "";
    private String infoText = "";
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

    public static PhoneCollectionFragment newInstance(String mobileNumber, String titleText, String descriptionText, String hintTitle, String buttonText, String countryName, String flavor, String info) {
        PhoneCollectionFragment fragment = new PhoneCollectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOBILE_NUMBER, mobileNumber);
        args.putString(ARG_TITLE_TEXT, titleText);
        args.putString(ARG_DESCRIPTION_TEXT, descriptionText);
        args.putString(ARG_HINT_TITLE_TEXT, hintTitle);
        args.putString(ARG_BUTTON_TEXT, buttonText);
        args.putString(ARG_COUNTRY_NAME_TEXT, countryName);
        args.putString(ARG_FLAVOR_TEXT, flavor);
        args.putString(ARG_INFO, info);
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
            buttonText = getArguments().getString(ARG_BUTTON_TEXT);
            countryName = getArguments().getString(ARG_COUNTRY_NAME_TEXT);
            flavor = getArguments().getString(ARG_FLAVOR_TEXT);
            infoText = getArguments().getString(ARG_INFO);
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
        llPhoneCollectionInfo = view.findViewById(R.id.llPhoneCollectionInfo);
        rlProgressView = view.findViewById(R.id.rlProgressView);
        tvInfo = view.findViewById(R.id.tvPhoneCollectionInfo);
        presenter = new PhoneCollectionPresenter(this);

        setEvents();

        setupCountryCodes();

        presenter.setCountryFormat(countryName);

        etMobileNumber.requestFocus();
        etMobileNumber.setEnableSelection(false);
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

        if(!buttonText.equals(""))
            btnNext.setText(buttonText);

        if(!infoText.equals("")) {
            llPhoneCollectionInfo.setVisibility(View.VISIBLE);
            tvInfo.setText(infoText);
        } else {
            llPhoneCollectionInfo.setVisibility(View.GONE);

        }

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
            countryCodes.add(new CountryCodeRowItem("+60", R.drawable.ic_flag_my));
            countryCodes.add(new CountryCodeRowItem("+65", R.drawable.ic_flag_sg));
        }
        if (flavor.contains("beres")) {
            countryCodes.add(new CountryCodeRowItem("+62", R.drawable.ic_flag_id));
        }
        if (flavor.contains("gawin")) {
            countryCodes.add(new CountryCodeRowItem("+63", R.drawable.ic_flag_ph));
        }

        //setting malaysia as the default selection to avoid null pointer
        onCountrySelected(countryCodes.get(0));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof PhoneDataCollectionListener) {
//            listener = (PhoneDataCollectionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement PhoneDataCollectionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        listener = null;
    }

    public void setListener(PhoneDataCollectionListener listener) {
        this.listener = listener;
    }

    public void clearListener(){
        this.listener = null;
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
        this.mobileNumber = mobileNumber;

        etMobileNumber.setText(mobileNumber);

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

        //update the current country code
        countryCode = item.code;

        if (listener != null)
            listener.onNumberChanged(item.code, mobileNumber);
    }

    @Override
    public void onMobileValueChanged(String countryCode, String value, boolean b) {
        disableLoginButton();
        this.mobileNumber = value;

        if (listener != null) {
            listener.onNumberChanged(countryCode, mobileNumber);
        }

        //uses the API validation from the activity
        if (listener != null)
            listener.getPhoneFormatIsValid(mobileNumber);
    }


    public void showProgress() {

    }

    public void hideProgress() {

    }

    /**
     * To be used when the country selection should be disabled and instead set a fixed choice
     */
    public void setFixedCountry(String countryCode) {

        switch (countryCode) {
            case "MY":
                etMobileNumber.setValidationCountry(countryCode, new CountryCodeRowItem("+60", R.drawable.ic_flag_my));
                break;
            case "SG":
                etMobileNumber.setValidationCountry(countryCode, new CountryCodeRowItem("+65", R.drawable.ic_flag_sg));
                break;
            case "ID":
                etMobileNumber.setValidationCountry(countryCode, new CountryCodeRowItem("+62", R.drawable.ic_flag_id));
                break;
            case "PH":
                etMobileNumber.setValidationCountry(countryCode, new CountryCodeRowItem("+63", R.drawable.ic_flag_ph));
                break;
        }
    }

}
