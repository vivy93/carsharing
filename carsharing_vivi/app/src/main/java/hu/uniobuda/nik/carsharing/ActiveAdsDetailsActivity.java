package hu.uniobuda.nik.carsharing;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Vivi on 2017. 05. 14..
 */

public class ActiveAdsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_ads_details);

        ActiveAdsDetailsFragment fragment = ActiveAdsDetailsFragment.newInstance(getIntent().getExtras());
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
