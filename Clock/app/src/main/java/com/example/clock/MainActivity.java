package com.example.clock;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements Tab1.OnFragmentInteractionListener, Tab2.OnFragmentInteractionListener, Tab3.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager vp = findViewById(R.id.pager);
        MyPagerAdapter mpa = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mpa);

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(vp);

        int[] icons = {
                R.drawable.alarm,
                R.drawable.timer,
                R.drawable.stopwatch
        };

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
