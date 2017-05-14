package hu.uniobuda.nik.carsharing;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hu.uniobuda.nik.carsharing.model.Advertisement;
import hu.uniobuda.nik.carsharing.model.User;

/**
 * Created by Vivi on 2017. 05. 14..
 */

public class ActiveAdsFragment extends Fragment {

    private static final String TAG = "ActiveAdsFragment";

    View rootView;
    private ChildEventListener mChildEventListener;
    private DatabaseReference userReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private User myCurrentUser;

    public static ActiveAdsFragment newInstance(/*user id vagy dupla konstruktor újrafelhasználtó legyen*/) {
        Bundle args = new Bundle();

        ActiveAdsFragment fragment = new ActiveAdsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_active_ads, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
       
        // ebbe jön majd a szűrt hirdetéslista
        final List<Advertisement> adListDB = new ArrayList<>();

        
        // GET CURRENT USER (a mienk, nem a firebase-es)
        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        
        Log.d(TAG, "userReference is null: " + (String.valueOf(userReference == null))); // false, tehát idáig eljut

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { 
                myCurrentUser.setValue(dataSnapshot.getValue(User.class));
                Log.d(TAG, "read user from DB: success");
                Log.d(TAG, "adOwnerUser is null: " + String.valueOf(myCurrentUser == null));
                
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                Toast.makeText(getContext(), "Failed to load user.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        userReference.addValueEventListener(valueEventListener); // calls onDataChange()
        // GET CURRENT USER END

        DatabaseReference adReference;
        final Advertisement tempAd = new Advertisement();
        
        // 1. ITERÁCIÓ: A USER OWNADIDS-JA ALAPJÁN
        for ( String adId : myCurrentUser.getOwnAdIds() ) {

            adReference = FirebaseDatabase.getInstance().getReference().child("advertisements").child(adId);

            ValueEventListener valueEventListenerAd = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    tempAd.setValue(dataSnapshot.getValue(Advertisement.class));
                    Log.d(TAG, "read ad from DB: success");
                    adListDB.add(tempAd);
                    Log.d(TAG, "adding active ad to the list");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "getAd:onCancelled", databaseError.toException());
                    Toast.makeText(getContext(), "Failed to load ad.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            adReference.addValueEventListener(valueEventListenerAd);

        }

        DatabaseReference adReference2;
        final Advertisement tempAd2 = new Advertisement();

        // 2. ITERÁCIÓ: A USER ACCEPTEDADIDS-JA ALAPJÁN
        for ( String adId : myCurrentUser.getAcceptedAdIds() ) {

            adReference2 = FirebaseDatabase.getInstance().getReference().child("advertisements").child(adId);

            ValueEventListener valueEventListenerAd = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    tempAd2.setValue(dataSnapshot.getValue(Advertisement.class));
                    Log.d(TAG, "read ad from DB: success");
                    adListDB.add(tempAd2);
                    Log.d(TAG, "adding active ad to the list");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "getAd:onCancelled", databaseError.toException());
                    Toast.makeText(getContext(), "Failed to load ad.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            adReference2.addValueEventListener(valueEventListenerAd);

        }

        final List<Advertisement> adList = adListDB;// relevantAdsOnFoot(date,"ChIJDS0Ugd7cQUcRf2iJF_ktiA0",adListDB);//lurdy

        final AdAdapter adapter = new AdAdapter(adList);
        ListView listView = (ListView) rootView.findViewById(R.id.ads_lstview);
        listView.setAdapter(adapter);


        // klikk egy listaelemre: új DetailsActivity()
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Advertisement selectedAd = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), ActiveAdsDetailsActivity.class);
                intent.putExtra("selected_ad", selectedAd);
                startActivity(intent);
            }
        });

    }

    public void cleanUpListeners() {
        if (mChildEventListener != null) {
            userReference.removeEventListener(mChildEventListener);


        }

    }

}
