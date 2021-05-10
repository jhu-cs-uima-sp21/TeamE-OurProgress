package com.example.bismapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class NavigationActivity extends AppCompatActivity{

    private BottomNavigationView bottomBar;
    private ImageView first_tab;
    private ImageView second_tab;
    private ImageView third_tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        first_tab = (ImageView) findViewById(R.id.first_tab);
        second_tab = (ImageView) findViewById(R.id.second_tab);
        third_tab = (ImageView) findViewById(R.id.third_tab);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.nav_production);
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new ProductionDashboard());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.nav_host_fragment, new ProductionDashboard());
        transaction.commit();

        //open to production dashboard
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }


    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected( MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_give:
                            openFragment(GiveHelp.newInstance("", ""));
                            first_tab.setImageDrawable(getResources().getDrawable(R.drawable.rectangle_tab_white));
                            second_tab.setImageDrawable(getResources().getDrawable(R.drawable.rectangle_tab_blue));
                            third_tab.setImageDrawable(getResources().getDrawable(R.drawable.rectangle_tab_blue));
                            return true;
                        case R.id.nav_production:
                            openFragment(ProductionDashboard.newInstance("", ""));
                            first_tab.setImageDrawable(getResources().getDrawable(R.drawable.rectangle_tab_blue));
                            second_tab.setImageDrawable(getResources().getDrawable(R.drawable.rectangle_tab_white));
                            third_tab.setImageDrawable(getResources().getDrawable(R.drawable.rectangle_tab_blue));
                            return true;
                        case R.id.navigation_ask:
                            openFragment(AskForHelp.newInstance("", ""));
                            first_tab.setImageDrawable(getResources().getDrawable(R.drawable.rectangle_tab_blue));
                            second_tab.setImageDrawable(getResources().getDrawable(R.drawable.rectangle_tab_blue));
                            third_tab.setImageDrawable(getResources().getDrawable(R.drawable.rectangle_tab_white));


                            return true;
                    }
                    return false;
                }
            };

}


