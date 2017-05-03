package hu.uniobuda.nik.carsharing;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hu.uniobuda.nik.carsharing.model.Advertisement;
import hu.uniobuda.nik.carsharing.model.TravelMode;

public class PostCarActivity extends AppCompatActivity implements  PlaceSelectionListener, View.OnClickListener{

    private static final String TAG = "PostCarActivity";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;

    private Button buttonPost;

    private EditText editTextFrom;
    private EditText editTextTo;
    private EditText node1;
    private EditText node2;
    private EditText showDate;
    private EditText editTextFreeSeats;
    private Date travelDate;
    private Integer seats;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_car);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
//-----------------------------
        // Retrieve the PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setUserVisibleHint(false);
        autocompleteFragment.setMenuVisibility(false);
        autocompleteFragment.setOnPlaceSelectedListener(this);

//---------------------
        //--------------------------
        editTextFrom = (EditText) findViewById(R.id.editTextFrom);
        //----------------
        editTextTo= (EditText) findViewById(R.id.editTextTo);
        node1 =(EditText) findViewById(R.id.node1);
        node2 =(EditText) findViewById(R.id.node2);
        showDate = (EditText) findViewById(R.id.showDate);
        editTextFreeSeats= (EditText) findViewById(R.id.editTextFreeSeats);

        buttonPost = (Button) findViewById(R.id.buttonPost);
        buttonPost.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if(view==buttonPost){
            postAd();
            //finish();

            //Log.d(TAG, "finishing " + TAG + ": success");

            startActivity(new Intent(this, ProfileActivity.class));
        }
    }

    public void postAd() {


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String dateInString = showDate.getText().toString().trim();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd/hh-mm-ss", Locale.getDefault());
        try {
            travelDate = format.parse(dateInString);
        } catch (ParseException e) {
            Toast.makeText(this,"Wrong date format!",Toast.LENGTH_SHORT).show();
        }

        String num= editTextFreeSeats.getText().toString().trim();
        try {
            seats=Integer.parseInt(num);
        } catch (Exception e){
            Toast.makeText(this,"Wrong number format!",Toast.LENGTH_SHORT).show();
        }

        Advertisement ad = new Advertisement(TravelMode.BY_CAR, travelDate, editTextFrom.getText().toString().trim(), editTextTo.getText().toString().trim(), node1.getText().toString().trim(), node2.getText().toString().trim(), seats);
        firebaseDatabase.child("advertisements").child(currentUser.getUid()).push().setValue(ad);

        Log.d(TAG, "saving real data: success");
    }

    @Override
    public void onPlaceSelected(Place place) {

        editTextFrom.setText(place.getAddress());

    }


    @Override
    public void onError(Status status) {
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }
}
