package com.mobweb.devizapp.activity.ui.main.main_fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobweb.devizapp.R;
import com.mobweb.devizapp.activity.SharedViewModel;
import com.mobweb.devizapp.activity.ui.main.AllCurrenciesAdapter;
import com.mobweb.devizapp.model.Currency;
import com.mobweb.devizapp.model.CurrencyData;
import com.mobweb.devizapp.network.NetworkManager;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllCurrenciesFragment extends Fragment {

    private AllCurrenciesAdapter adapter;
    private RecyclerView recyclerView;
    private SharedViewModel viewModel;
    private String base;

    public AllCurrenciesFragment() {
        // Required empty public constructor
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.AllRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new AllCurrenciesAdapter();
        recyclerView.setAdapter(adapter);
    }

    public static AllCurrenciesFragment newInstance() {
        return new AllCurrenciesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_currencies, container, false);
        initRecyclerView(view);
        loadCurrencyData();
        return view;
    }

    private void loadCurrencyData() {
        if (base == null) return;
        NetworkManager.getInstance().getCurrency(base).enqueue(new Callback<CurrencyData>() {
            @Override
            public void onResponse(@NonNull Call<CurrencyData> call,
                                   @NonNull Response<CurrencyData> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        recyclerView = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        viewModel.getBase().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                base = s;
                loadCurrencyData();
            }
        });
    }
}
