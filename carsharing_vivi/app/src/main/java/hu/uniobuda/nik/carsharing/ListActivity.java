package hu.uniobuda.nik.carsharing;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;

public class ListActivity extends AppCompatActivity {

    public static Date travelDate;
    public static String travelFromID;
    AdsListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        fragment = AdsListFragment.newInstance();
        fragment.setTravelDatas(travelDate,travelFromID);
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
