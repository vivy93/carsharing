package hu.uniobuda.nik.carsharing;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Vivi on 2017. 05. 14..
 */

public class ActiveAdsActivity extends AppCompatActivity {

    ActiveAdsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_ads);

        fragment = ActiveAdsFragment.newInstance();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
    @Override
    protected void onStop() {
        super.onStop();
        fragment.cleanUpListeners();
    }


}
