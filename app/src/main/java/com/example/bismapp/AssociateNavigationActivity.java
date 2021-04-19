package com.example.bismapp;


        import android.os.Bundle;
        import android.util.Log;
        import android.view.MenuItem;

        import com.google.android.material.bottomnavigation.BottomNavigationView;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentTransaction;


public class AssociateNavigationActivity extends AppCompatActivity {

    private static final String TAG = "AssocNav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.associate_navigation);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new AssociateProductionDash());

        //open to production dashboard

        Log.d(TAG, "OnCreate ");
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected( MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_give:
                            //openFragment(HomeFragment.newInstance("", ""));
                            return true;
                        case R.id.nav_production:
                            openFragment(AssociateProductionDash.newInstance("", ""));
                            return true;
                        case R.id.navigation_ask:
                            openFragment(AskForHelp.newInstance("", ""));
                            return true;
                    }
                    return false;
                }
            };

}


