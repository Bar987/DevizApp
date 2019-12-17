package com.mobweb.devizapp.activity.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobweb.devizapp.R;
import com.mobweb.devizapp.model.AvailableCurrencies;
import com.mobweb.devizapp.model.Currency;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.util.ArrayList;
import java.util.List;

public class AllCurrenciesAdapter extends RecyclerView.Adapter<AllCurrenciesAdapter.AllCurrenciesViewHolder> {

    private List<Currency> currencies;

    public AllCurrenciesAdapter() {
        currencies = new ArrayList<>();
        for (String s : AvailableCurrencies.currencies) {
            currencies.add(new Currency(s, ExtendedCurrency.getCurrencyByISO(s).getFlag(), ExtendedCurrency.getCurrencyByISO(s).getSymbol()));
        }
    }

    @NonNull
    @Override
    public AllCurrenciesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_item_currency, parent, false);
        return new AllCurrenciesAdapter.AllCurrenciesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllCurrenciesAdapter.AllCurrenciesViewHolder holder, int position) {
        Currency item = currencies.get(position);
        holder.nameTextView.setText(currencies.get(position).getCode());
        holder.imageView.setImageResource(currencies.get(position).getImg_src());
        holder.rateTextView.setText(String.format("%.3f", currencies.get(position).getRate()) + " " + currencies.get(position).getSymbol());
        holder.item = item;
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    class AllCurrenciesViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageView;
        TextView rateTextView;
        Currency item;

        AllCurrenciesViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.RecycleCurrencyFlag);
            nameTextView = itemView.findViewById(R.id.CurrencyItemNameTextView);
            rateTextView = itemView.findViewById(R.id.CurrencyItemRateTextView);
        }

    }

    public List<Currency> getCurrencies() {
        return currencies;
    }
}
