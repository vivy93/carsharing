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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import hu.uniobuda.nik.carsharing.model.Advertisement;
import hu.uniobuda.nik.carsharing.model.HandleJSON;
import hu.uniobuda.nik.carsharing.model.TravelMode;

public class AdsListFragment extends Fragment {
    private static final String TAG = "AdListFragment";

    public static Date travelDate;
    public static String travelFromID;
    static List<Advertisement> adListDB = new ArrayList<>();       // DB-ből kell
    final static List<String> advertisementIds = new ArrayList<>();    // lehet h csak a recycleviewhoz kéne?

    private HandleJSON obj;

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
                //relevantAdsOnFoot(travelDate, travelFromID);

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


        //final List<Advertisement> adList = adListDB;//relevantAdsOnFoot(travelDate, travelFromID, adListDB);// adListDB;

        final AdAdapter adapter = new AdAdapter(adListDB);
        ListView listView = (ListView) rootView.findViewById(R.id.ads_lstview);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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


    public Integer jsonParser(String finalUrl) {
        obj = new HandleJSON(finalUrl);
        obj.fetchJSON();
        while (obj.parsingComplete) ;
        return obj.getDistance();
    }

    private List<Advertisement> relevantAdsOnFoot(/*user id kell majd h a saját hirdetéseimet ne listázza,*/
                                                  Date date, String fromid/*, List<Advertisement> adListFull*/) {
        List<Advertisement> adListInt = new ArrayList<>();

        for (int i = 0; i < adListDB.size(); i++) {// kiválogatom azokat a hirdetéseket amik 24 órán belül indulnak

            if ((adListDB.get(i).getWhen().equals(date) || adListDB.get(i).getWhen().after(date)) && date.getTime() < (adListDB.get(i).getWhen().getTime() + 86400000))// aznap = akkor vagy utánna-> refactorálás később
            {
                adListInt.add(adListDB.get(i));
            }
        }
        String url = null;
        String urlStart = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
        String place_id = "place_id:";
        //place_id:ChIJyc_U0TTDQUcRYBEeDCnEAAQ&destinations=place_id:ChIJgyte_ioMR0cRcBEeDCnEAAQ|place_id:ChIJ04zIKBKFQUcRsFgeDCnEAAQ
        String urlEnd = "&mode=walk&language=hu-HU&key=AIzaSyB5YMQI8YQ8l8cj1F6aC1rIQ3pvQjmvz0s";
        for (int i = 0; i < adListInt.size(); i++) {
            // csekkolni kell h nincs null értékű id !
            url = "";
            url = urlStart + place_id + fromid +
                    "&destinations=" + place_id + adListInt.get(i).getFromID()
                    + "|" + place_id + adListInt.get(i).getNode1ID() +
                    "|" + place_id + adListInt.get(i).getNode2ID() + urlEnd;

            adListInt.get(i).setDistance(jsonParser(url));
            if (adListInt.get(i).getDistance() > 10000)
                adListInt.remove(i);

        }

        Collections.sort(adListInt, new Comparator<Advertisement>() {
            @Override
            public int compare(Advertisement o1, Advertisement o2) {
                return o1.getDistance().compareTo(o2.getDistance());
            }
        });

        return adListInt;
    }


    public void setTravelDatas(Date travelDate, String travelFromID) {
        this.travelDate = travelDate;
        this.travelFromID = travelFromID;
    }


    public void cleanUpListeners() {
        if (mChildEventListener != null) {
            adsReference.removeEventListener(mChildEventListener);

        }

    }
}
