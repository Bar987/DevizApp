package com.mobweb.devizapp.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AvailableCurrencies {
    private static String[] currencies_string = new String[]{"AED", "ARS", "AUD", "BGN", "BRL", "CAD", "CHF", "CLP", "CNY",
            "COP", "CZK", "DKK", "DOP", "EGP", "EUR",
            "GBP", "HKD", "HRK", "HUF", "IDR", "ILS", "INR", "ISK", "JPY", "KRW", "KZT", "MXN", "MYR", "NOK", "NZD", "PEN", "PHP", "PKR",
            "PLN", "PYG", "RON", "RUB", "SAR", "SEK", "SGD", "THB", "TRY", "TWD", "UAH", "USD", "UYU", "VND", "ZAR"};
    public static Set<String> currencies = new HashSet<String>(Arrays.asList(currencies_string));

}
