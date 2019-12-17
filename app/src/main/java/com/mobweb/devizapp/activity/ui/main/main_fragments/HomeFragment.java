package com.mobweb.devizapp.activity.ui.main.main_fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobweb.devizapp.R;
import com.mobweb.devizapp.activity.SharedViewModel;
import com.mobweb.devizapp.activity.ui.main.CurrencyAdapter;
import com.mobweb.devizapp.model.AvailableCurrencies;
import com.mobweb.devizapp.model.Currency;
import com.mobweb.devizapp.model.CurrencyData;
import com.mobweb.devizapp.network.NetworkManager;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class HomeFragment extends Fragment {


    private ImageButton select_button;

    private ImageView currency_flag;

    private TextView selected_currency;

    private RecyclerView recyclerView;

    private CurrencyAdapter adapter;


    private CurrencyPicker picker;

    public FabListener listener;

    private SharedViewModel viewModel;

    private SwipeRefreshLayout swipeContainer;


    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void initRecyclerView(View view) {
        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadCurrencyData();
            }
        });
        recyclerView = view.findViewById(R.id.MainRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new CurrencyAdapter(this.getContext());
        recyclerView.setAdapter(adapter);

    }

    private void initFab() {
        FloatingActionButton fab = listener.getFab();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                        Currency c = new Currency(code, flagDrawableResID, symbol);
                        if (!adapter.getCurrencyCodes().contains(code)) {
                            adapter.addCurrency(c);
                        }
                        loadCurrencyData();
                        picker.dismiss();
                    }
                });
                picker.show(getFragmentManager(), "CURRENCY_PICKER");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerView = null;
        picker = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        selected_currency = view.findViewById(R.id.base_selected);
        select_button = view.findViewById(R.id.currency_selector_btn);
        currency_flag = view.findViewById(R.id.currency_flag);
        picker = CurrencyPicker.newInstance("Select Currency");
        picker.setCurrenciesList(AvailableCurrencies.currencies);
        initRecyclerView(view);
        initFab();

        selected_currency.setText(adapter.getBase().getCode());
        currency_flag.setImageResource(adapter.getBase().getImg_src());

        select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {
                        adapter.setBase(code);
                        viewModel.setBase(code);
                        loadCurrencyData();
                        selected_currency.setText(code);
                        currency_flag.setImageResource(flagDrawableResID);

                        picker.dismiss();
                    }
                });
                picker.show(getFragmentManager(), "CURRENCY_PICKER");
            }
        });
        loadCurrencyData();
        return view;
    }


    public interface FabListener {
        FloatingActionButton getFab();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        viewModel.setBase(adapter.getBase().getCode());
    }

    private void loadCurrencyData() {
        NetworkManager.getInstance().getCurrency(adapter.getBase().getCode()).enqueue(new Callback<CurrencyData>() {
            @Override
            public void onResponse(@NonNull Call<CurrencyData> call,
                                   @NonNull Response<CurrencyData> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    swipeContainer.setRefreshing(false);
                    refreshCurrencyData(response.body());
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

    private void refreshCurrencyData(CurrencyData currencyData) {
        Map<String, Double> map = currencyData.rates;
        for (Currency c : adapter.getCurrencies()) {
            c.setRate(map.get(c.getCode()));
        }
        adapter.notifyDataSetChanged();
    }
}
