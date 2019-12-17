package com.mobweb.devizapp.activity.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobweb.devizapp.R;
import com.mobweb.devizapp.model.Currency;
import com.mynameismidori.currencypicker.ExtendedCurrency;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    private List<Currency> currencies;
    private Currency base;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    public CurrencyAdapter(Context context) {
        prefs = context.getSharedPreferences("USER", 0);
        editor = prefs.edit();
        List loadList = new ArrayList(prefs.getStringSet("FAVOURITE", new HashSet<String>()));
        setCurrencies(loadList);
        setBase(prefs.getString("BASE", "HUF"));
    }

    public void addCurrency(com.mobweb.devizapp.model.Currency currency) {
        currencies.add(currency);
        notifyItemInserted(currencies.size() - 1);

        List<String> savedCurrencies = new ArrayList<>();
        for (Currency c : currencies) {
            savedCurrencies.add(c.getCode());
        }
        editor.putStringSet("FAVOURITE", new HashSet<>(savedCurrencies));
        editor.commit();
    }

    public Currency getBase() {
        return base;
    }

    public void setBase(String base) {
        Currency c = new Currency(base, ExtendedCurrency.getCurrencyByISO(base).getFlag(), ExtendedCurrency.getCurrencyByISO(base).getSymbol());
        this.base = c;
        editor.putString("BASE", base);
        editor.commit();
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
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


    public void removeCurrency(int position) {
        currencies.remove(position);
        notifyItemRemoved(position);
        if (position < currencies.size()) {
            notifyItemRangeChanged(position, currencies.size() - position);
        }
        List<String> saveList = new ArrayList();
        for (Currency c : currencies) {
            saveList.add(c.getCode());
        }
        editor.putStringSet("FAVOURITE", new HashSet<>(saveList));
        editor.commit();
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public List<String> getCurrencyCodes() {
        List<String> codes = new ArrayList<>();
        for (Currency c : currencies) {
            codes.add(c.getCode());
        }
        return codes;
    }

    public void setCurrencies(List<String> new_currencies) {
        this.currencies = new ArrayList<>();
        for (String code : new_currencies) {
            Currency c = new Currency(code, ExtendedCurrency.getCurrencyByISO(code).getFlag(), ExtendedCurrency.getCurrencyByISO(code).getSymbol());
            this.currencies.add(c);
        }
    }

    class CurrencyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageView;
        ImageButton removeButton;
        TextView rateTextView;
        Currency item;

        CurrencyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.RecycleCurrencyFlag);
            nameTextView = itemView.findViewById(R.id.CurrencyItemNameTextView);
            removeButton = itemView.findViewById(R.id.CurrencyItemRemoveButton);
            rateTextView = itemView.findViewById(R.id.CurrencyItemRateTextView);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeCurrency(currencies.indexOf(item));
                }
            });
        }
    }
}