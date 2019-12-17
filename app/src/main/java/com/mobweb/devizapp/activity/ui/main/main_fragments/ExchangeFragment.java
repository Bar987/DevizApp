package com.mobweb.devizapp.activity.ui.main.main_fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobweb.devizapp.R;
import com.mobweb.devizapp.model.AvailableCurrencies;
import com.mobweb.devizapp.model.Currency;
import com.mobweb.devizapp.model.CurrencyData;
import com.mobweb.devizapp.network.NetworkManager;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ExchangeFragment extends Fragment {

    private Currency from;

    private Currency to;

    private CurrencyPicker picker;


    private ImageView fromImageView;
    private ImageView toImageView;
    private EditText fromCurrencyEditText;
    private TextView toCurrencyValueTextView;
    private TextView fromCurrencyTextView;
    private TextView toCurrencyTextView;
    private Button saveButton;
    private Button loadButton;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public ExchangeFragment() {
    }


    public static ExchangeFragment newInstance() {
        return new ExchangeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_exchange, container, false);
        picker = CurrencyPicker.newInstance("Select Currency");
        picker.setCurrenciesList(AvailableCurrencies.currencies);
        ImageButton fromSelect = view.findViewById(R.id.FromImageButton);
        ImageButton toSelect = view.findViewById(R.id.ToImageButton);
        fromImageView = view.findViewById(R.id.FromImageView);
        toImageView = view.findViewById(R.id.ToImageView);
        fromCurrencyTextView = view.findViewById(R.id.FromCurrencyTextView);
        toCurrencyTextView = view.findViewById(R.id.ToCurrencyTextView);
        fromCurrencyEditText = view.findViewById(R.id.FromCurrencyEditText);
        toCurrencyValueTextView = view.findViewById(R.id.ToCurrencyValueTextView);
        Button reverseButton = view.findViewById(R.id.reverseButton);
        saveButton = view.findViewById(R.id.saveButton);
        loadButton = view.findViewById(R.id.loadButton);
        fromSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                        from = new Currency(code, flagDrawableResID, symbol);
                        fromImageView.setImageResource(flagDrawableResID);
                        fromCurrencyTextView.setText(code);
                        if (from != null && to != null && !fromCurrencyEditText.getText().toString().equals("")) {
                            refreshCurrencyData(fromCurrencyEditText.getText().toString());
                        }

                        picker.dismiss();
                    }
                });
                picker.show(getFragmentManager(), "CURRENCY_PICKER");
            }
        });

        toSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                        to = new Currency(code, flagDrawableResID, symbol);
                        toImageView.setImageResource(flagDrawableResID);
                        toCurrencyTextView.setText(code);

                        if (from != null && to != null && !fromCurrencyEditText.getText().toString().equals("")) {
                            refreshCurrencyData(fromCurrencyEditText.getText().toString());
                        }
                        picker.dismiss();
                    }
                });
                picker.show(getFragmentManager(), "CURRENCY_PICKER");
            }
        });

        reverseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Currency temp = from;
                from = to;
                to = temp;
                refreshCurrencyData(fromCurrencyEditText.getText().toString());
            }
        });

        fromCurrencyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (from != null && to != null) {
                    refreshCurrencyData(fromCurrencyEditText.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from != null && to != null && !fromCurrencyEditText.getText().toString().equals("")) {
                    if (getActivity().getSharedPreferences("USER", 0) != null) {
                        prefs = getActivity().getSharedPreferences("USER", 0);
                        editor = prefs.edit();
                        editor.putString("FROM", from.getCode());
                        editor.putString("TO", to.getCode());
                        editor.putString("VALUE", fromCurrencyEditText.getText().toString());
                        editor.commit();
                    }
                } else {
                    Toast.makeText(getActivity(),
                            "No input can be empty!",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSharedPreferences("USER", 0) != null) {
                    prefs = getActivity().getSharedPreferences("USER", 0);
                    String from_code = prefs.getString("FROM", "");
                    String to_code = prefs.getString("TO", "");
                    from = new Currency(from_code, ExtendedCurrency.getCurrencyByISO(from_code).getFlag(),
                            ExtendedCurrency.getCurrencyByISO(from_code).getSymbol());
                    to = new Currency(to_code, ExtendedCurrency.getCurrencyByISO(to_code).getFlag(),
                            ExtendedCurrency.getCurrencyByISO(to_code).getSymbol());
                    fromCurrencyEditText.setText(prefs.getString("VALUE", ""));
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        picker = null;
    }


    private void refreshCurrencyData(String value) {
        if (value.isEmpty()) {
            return;
        }
        loadCurrencyData(value);
    }


    private void exchange(CurrencyData currencyData, String value) {
        Map<String, Double> map = currencyData.rates;
        Double calculated = Double.valueOf(value) * map.get(to.getCode());
        toCurrencyValueTextView.setText(String.format("%.3f", calculated) + " " + to.getSymbol());
        fromImageView.setImageResource(from.getImg_src());
        fromCurrencyTextView.setText(from.getCode());
        toImageView.setImageResource(to.getImg_src());
        toCurrencyTextView.setText(to.getCode());
    }

    private void loadCurrencyData(final String value) {
        if (value.equals(""))
            return;
        NetworkManager.getInstance().getCurrency(from.getCode()).enqueue(new Callback<CurrencyData>() {
            @Override
            public void onResponse(@NonNull Call<CurrencyData> call,
                                   @NonNull Response<CurrencyData> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    exchange(response.body(), value);
                } else {
                    Toast.makeText(getActivity(),
                            "Error: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CurrencyData> call, @NonNull Throwable throwable) {
                throwable.printStackTrace();
                Toast.makeText(getActivity(),
                        "Network request error occurred, check LOG",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

