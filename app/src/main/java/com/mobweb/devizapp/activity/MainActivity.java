package com.mobweb.devizapp.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.mobweb.devizapp.R;
import com.mobweb.devizapp.activity.ui.main.SectionsPagerAdapter;
import com.mobweb.devizapp.activity.ui.main.main_fragments.HomeFragment;


public class MainActivity extends AppCompatActivity implements HomeFragment.FabListener {

    private FloatingActionButton fab;

    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position != 0) {
                    fab.hide();
                } else fab.show();
            }
        });
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        fab = findViewById(R.id.fab);
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof HomeFragment) {
            ((HomeFragment) fragment).listener = this;
        }
    }

    @Override
    public FloatingActionButton getFab() {
        return fab;
    }


}