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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hu.uniobuda.nik.carsharing.model.Advertisement;
import hu.uniobuda.nik.carsharing.model.HandleJSON;
import hu.uniobuda.nik.carsharing.model.TravelMode;

public class AdsListFragment extends Fragment {
    private static final String TAG = "AdListFragment";

    //---------------------------------------------------------------------------------
    private HandleJSON obj;

    public Integer jsonParser(String finalUrl) {
        obj = new HandleJSON(finalUrl);
        obj.fetchJSON();
        while (obj.parsingComplete) ;
        return obj.getDistance();
    }

    private List<Advertisement> relevantAdsOnFoot(/*user id kell majd h a saját hirdetéseimet ne listázza TravelMode travelMode,*/
                                                  Date date, String fromid, List<Advertisement> adListFull) {
        List<Advertisement> adList = new ArrayList<>();

        for (int i = 0; i < adListFull.size(); i++) {// kiválogatom a kocsis hirdetéseket akik aznap indulna mint én
            if (adListFull.get(i).getMode().equals(TravelMode.BY_CAR))
                if (adListFull.get(i).getWhen().equals(date) || adListFull.get(i).getWhen().after(date))// aznap = akkor vagy utánna-> refactorálás később
                {
                    adList.add(adListFull.get(i));
                }
        }
        String url = null;
        String urlStart = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
        String place_id = "place_id:";
        //place_id:ChIJyc_U0TTDQUcRYBEeDCnEAAQ&destinations=place_id:ChIJgyte_ioMR0cRcBEeDCnEAAQ|place_id:ChIJ04zIKBKFQUcRsFgeDCnEAAQ
        String urlEnd = "&mode=walk&language=hu-HU&key=AIzaSyB5YMQI8YQ8l8cj1F6aC1rIQ3pvQjmvz0s";
        for (int i = 0; i < adList.size(); i++) {
            // csekkolni kell h nincs null értékű id !
            url = "";
            url = urlStart + place_id + fromid +
                    "&destinations=" + place_id + adList.get(i).getFromID()
                    + "|" + place_id + adList.get(i).getNode1ID() +
                    "|" + place_id + adList.get(i).getNode2ID() + urlEnd;

            adList.get(i).setDistance(jsonParser(url));

        }

        Collections.sort(adList, new Comparator<Advertisement>() {
            @Override
            public int compare(Advertisement o1, Advertisement o2) {
                return o1.getDistance().compareTo(o2.getDistance());
            }
        });


        return adList;
    }

    //----------------------------------------------------------------------------------

    View rootView;

    private DatabaseReference adsReference;
    private ChildEventListener mChildEventListener;

    public static AdsListFragment newInstance(/*user id vagy dupla konstruktor újrafelhasználtó legyen*/) {
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

        final List<Advertisement> adListDB = new ArrayList<>();       // DB-ből kell
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
                adListDB.add(advertisement);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                Advertisement newAdvertisement = dataSnapshot.getValue(Advertisement.class);
                String advertisementKey = dataSnapshot.getKey();

                int advertisementIndex = advertisementIds.indexOf(advertisementKey);
                if (advertisementIndex > 1) {
                    // Replace with the new data
                    adListDB.set(advertisementIndex, newAdvertisement);

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
                if (advertisementIndex > 1) {
                    // Remove data from the list
                    advertisementIds.remove(advertisementIndex);
                    adListDB.remove(advertisementIndex);

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

        // a lista rendezéshez kell
       /* SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String dateInString = "30-04-2017 10:20:00";
        Date date;
        try { date = sdf.parse(dateInString); }
         catch (ParseException e) { e.printStackTrace();}*/
        final List<Advertisement> adList = adListDB;// relevantAdsOnFoot(date,"ChIJDS0Ugd7cQUcRf2iJF_ktiA0",adListDB);//lurdy

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
