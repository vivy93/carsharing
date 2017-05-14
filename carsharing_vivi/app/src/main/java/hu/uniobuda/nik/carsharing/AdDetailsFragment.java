package hu.uniobuda.nik.carsharing;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

import hu.uniobuda.nik.carsharing.model.Advertisement;
import hu.uniobuda.nik.carsharing.model.User;

public class AdDetailsFragment extends Fragment {

    private static final String TAG = "AdDetailsFragment";
    View rootView;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()); //igy nincs benne a GTM... az idő kiírásban

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference userReference;


    public static AdDetailsFragment newInstance(Advertisement selectedAd) {

        Bundle args = new Bundle();
        args.putParcelable("selected_ad", selectedAd);

        return newInstance(args);
    }

    public static AdDetailsFragment newInstance(Bundle args) {

        AdDetailsFragment fragment = new AdDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ad_detail, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        final TextView nicNameView = (TextView) rootView.findViewById(R.id.name);
        final TextView rateView = (TextView) rootView.findViewById(R.id.rate);
        TextView fromView = (TextView) rootView.findViewById(R.id.from);
        TextView toView = (TextView) rootView.findViewById(R.id.to);
        TextView whenView = (TextView) rootView.findViewById(R.id.when);
        TextView freeSeatsView = (TextView) rootView.findViewById(R.id.freeSeats);
        //@Patrícia : azt mondtad majd még bővíted meg formázgatod az xls-t is

        // final kell
        final User adOwnerUser = new User();


        if (getArguments() != null) {
            Bundle args = getArguments();
            if (args.containsKey("selected_ad")) {
                Advertisement selectedAd = args.getParcelable("selected_ad");
                if (selectedAd != null) {


                    // user lekérése a DB-ből

                    userReference = FirebaseDatabase.getInstance().getReference().child("users").child(selectedAd.getUid());
                    Log.d(TAG, "userReference is null: " + (String.valueOf(userReference == null))); // false, tehát idáig eljut

                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) { // MÉRT NEM HÍVÓDIK MEG?
                            adOwnerUser.setValue(dataSnapshot.getValue(User.class));
                            Log.d(TAG, "read user from DB: success");
                            Log.d(TAG, "adOwnerUser is null: " + String.valueOf(adOwnerUser == null));

                            nicNameView.append(adOwnerUser.getName());
                            rateView.append(adOwnerUser.getRating().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            Toast.makeText(getContext(), "Failed to load user.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    };

                    userReference.addValueEventListener(valueEventListener); // calls onDataChange()

                    // -------------

                    fromView.append(String.valueOf(selectedAd.getFrom()));
                    toView.append(String.valueOf(selectedAd.getTo()));
                    whenView.append(String.valueOf(sdf.format(selectedAd.getWhen())));
                    freeSeatsView.append(String.valueOf(selectedAd.getSeats()));

                }
            }

        }
    }
}