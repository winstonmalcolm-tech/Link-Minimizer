package com.example.urlshortner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.example.urlshortner.Adapters.TabsAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TabLayout menuTabLayout;
    ViewPager2 viewPager2;

    TabsAdapter tabsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });




        ActionBar actionBar = getSupportActionBar();

        Objects.requireNonNull(actionBar).setBackgroundDrawable(new ColorDrawable(getColor(R.color.light_blue)));
        actionBar.setElevation(0);

        menuTabLayout = findViewById(R.id.menu_tabs);
        viewPager2 = findViewById(R.id.fragment_holder);

        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            Objects.requireNonNull(actionBar).setBackgroundDrawable(new ColorDrawable(getColor(R.color.nightmode_actionbar_bg)));
            menuTabLayout.setSelectedTabIndicatorColor(getColor(R.color.white));

        }




        tabsAdapter = new TabsAdapter(this);
        viewPager2.setAdapter(tabsAdapter);


        menuTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(menuTabLayout.getTabAt(position)).select();
            }
        });
    }
}