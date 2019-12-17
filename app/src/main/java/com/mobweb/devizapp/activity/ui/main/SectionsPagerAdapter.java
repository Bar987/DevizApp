package com.mobweb.devizapp.activity.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mobweb.devizapp.R;
import com.mobweb.devizapp.activity.ui.main.main_fragments.AllCurrenciesFragment;
import com.mobweb.devizapp.activity.ui.main.main_fragments.ExchangeFragment;
import com.mobweb.devizapp.activity.ui.main.main_fragments.HomeFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.home, R.string.exchange_currencies, R.string.available_currencies};
    private static final int PAGE_NUM = TAB_TITLES.length;
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                return ExchangeFragment.newInstance();
            case 2:
                return AllCurrenciesFragment.newInstance();
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }

}