package hu.uniobuda.nik.carsharing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import hu.uniobuda.nik.carsharing.model.Advertisement;
import hu.uniobuda.nik.carsharing.model.TravelMode;

public class AdsListFragment extends Fragment {

    private static final String TAG = "AdListFragment";

    View rootView;

    private DatabaseReference adsReference;
    private ChildEventListener mChildEventListener;

    public static AdsListFragment newInstance()
    {
        Bundle args = new Bundle();

        AdsListFragment fragment = new AdsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_ads_list,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        List<Advertisement> adList = new ArrayList<>(); // DB-ből kell





        adsReference = FirebaseDatabase.getInstance().getReference().child("advertisements");
        // vagy csak simán databaseReference kell?

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        adsReference.addChildEventListener(childEventListener);
        // Store reference to listener so it can be removed on app stop
        mChildEventListener = childEventListener;





        final AdAdapter adapter = new AdAdapter(adList);
        ListView listView = (ListView) rootView.findViewById(R.id.ads_lstview);
        listView.setAdapter(adapter);

        // klikk egy listaelemre: új DetailsActivity()
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Advertisement selectedAd = adapter.getItem(position);
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra("selected_ad", selectedAd);
                    startActivity(intent);
                }
            });



    }
}
