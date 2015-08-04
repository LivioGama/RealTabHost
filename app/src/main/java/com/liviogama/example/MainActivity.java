package com.liviogama.example;

import com.liviogama.fragmenttabhost.FragmentTabHost;
import com.liviogama.realtabhost.example.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager());

        tabHost.addTab(tabHost.newTabSpec("TAB TITLE 1", R.drawable.campaign_logo, R.layout.custom_tab, MainFragment.newInstance(Color.RED)));
        tabHost.addTab(tabHost.newTabSpec("TAB TITLE 2", R.drawable.agency_logo, R.layout.custom_tab, MainFragment.newInstance(Color.GREEN)));
        tabHost.addTab(tabHost.newTabSpec("TAB TITLE 3", R.drawable.advertiser_logo, R.layout.custom_tab, MainFragment.newInstance(Color.BLUE)));
        tabHost.addTab(tabHost.newTabSpec("TAB TITLE 4", R.drawable.creative_logo, R.layout.custom_tab, MainFragment.newInstance(Color.GRAY)));

        // Customization
        tabHost.setHeight(230);
        tabHost.setBackgroundColor(getResources().getColor(R.color.darkblue));
        tabHost.setNormalTintColor(getResources().getColor(R.color.white));
        tabHost.setSelectedTintColor(getResources().getColor(R.color.yellow));

        tabHost.build();
    }
}