package com.example.bismapp;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class NavigationActivity extends AppCompatActivity{

    private BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        //navView.setBackgroundResource(R.drawable.tabbar_background);
        navView.setSelectedItemId(R.id.nav_production);
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        //navView.getMenu().findItem(R.id.nav_production).setChecked(true);
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
                            return true;
                        case R.id.nav_production:
                            openFragment(ProductionDashboard.newInstance("", ""));
                            return true;
                        case R.id.navigation_ask:
                            openFragment(AskForHelp.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };

}


