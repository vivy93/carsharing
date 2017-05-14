package hu.uniobuda.nik.carsharing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.PrivateKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hu.uniobuda.nik.carsharing.model.Advertisement;
import hu.uniobuda.nik.carsharing.model.TravelMode;

public class PostFootActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PostCarActivity";

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;


    private Button buttonSearch;

    private Boolean error;


    private EditText editTextFrom;
    private EditText dateOnFoot;
    private Date travelDate;
    private String fromID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_foot);

        error = true;

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        editTextFrom = (EditText) findViewById(R.id.editTextFrom);
        editTextFrom.setKeyListener(null);// ne lehessen módosítani
        editTextFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openAutocompleteActivity();
            }
        });

        dateOnFoot = (EditText) findViewById(R.id.dateOnFoot);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(this);
        fromID = "";

    }

    @Override
    public void onClick(View v) {
        if (v == buttonSearch) {
            SearchAd();
            //finish();
            //Log.d(TAG, "finishing " + TAG + ": success");

            startActivity(new Intent(this, ListActivity.class));

        }
    }

    private void openAutocompleteActivity() {
        try {

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {

            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {

            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            error=false;
            return;
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);

                editTextFrom.setText(place.getAddress());
                this.fromID = String.valueOf(place.getId());


            }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {

            }

    }


    private void SearchAd() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String dateInString = dateOnFoot.getText().toString().trim();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd/hh-mm-ss", Locale.getDefault());
        try {
            this.travelDate = format.parse(dateInString);
            ListActivity.travelDate = this.travelDate;
            ListActivity.travelFromID = this.fromID;
        } catch (ParseException e) {

            Toast.makeText(this,"Wrong date format!",Toast.LENGTH_SHORT).show();
            error=false;
            return;
        }

        if (editTextFrom.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this,"Please enter the address!",Toast.LENGTH_SHORT).show();
            error=false;
            return;
        }

        //Advertisement ad = new Advertisement(TravelMode.ON_FOOT, travelDate, editTextFrom.getText().toString().trim(), null,null,null,null, null, null, null);
        //firebaseDatabase.child("advertisements").child(currentUser.getUid()).push().setValue(ad);
        error = true;
        Log.d(TAG, "creating real data: success");

    }
}
