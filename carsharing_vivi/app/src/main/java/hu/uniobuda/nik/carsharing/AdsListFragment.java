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
import android.widget.Toast;

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

    public static AdsListFragment newInstance() {
        Bundle args = new Bundle();

        AdsListFragment fragment = new AdsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ads_list, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        final List<Advertisement> adList = new ArrayList<>();       // DB-ből kell
        final List<String> advertisementIds = new ArrayList<>();    // lehet h csak a recycleviewhoz kéne?


        adsReference = FirebaseDatabase.getInstance().getReference().child("advertisements");
        // vagy csak simán databaseReference kell?

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new ad has been added, add it to the displayed list
                Advertisement advertisement = dataSnapshot.getValue(Advertisement.class);

                advertisementIds.add(dataSnapshot.getKey());
                adList.add(advertisement);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Advertisement newAdvertisement = dataSnapshot.getValue(Advertisement.class);
                String advertisementKey = dataSnapshot.getKey();

                int advertisementIndex = advertisementIds.indexOf(advertisementKey);
                if (advertisementIndex > -1) {
                    // Replace with the new data
                    adList.set(advertisementIndex, newAdvertisement);

                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + advertisementKey);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // An ad has changed, use the key to determine if we are displaying this
                // ad and if so remove it.
                String advertisementKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int advertisementIndex = advertisementIds.indexOf(advertisementKey);
                if (advertisementIndex > -1) {
                    // Remove data from the list
                    advertisementIds.remove(advertisementIndex);
                    adList.remove(advertisementIndex);

                } else {
                    Log.w(TAG, "onChildRemoved:unknown_child:" + advertisementKey);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // An ad has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                Advertisement movedComment = dataSnapshot.getValue(Advertisement.class);
                String advertisementKey = dataSnapshot.getKey();
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getAdvertisements:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), "Failed to load advertisements.",
                        Toast.LENGTH_SHORT).show();
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

    public void cleanUpListeners() {
        if (mChildEventListener != null) {
            adsReference.removeEventListener(mChildEventListener);
        }
    }
}
