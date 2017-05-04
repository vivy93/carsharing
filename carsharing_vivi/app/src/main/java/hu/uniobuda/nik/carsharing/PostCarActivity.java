package hu.uniobuda.nik.carsharing;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
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

public class PostCarActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "PostCarActivity";
    /**
     * Request code for the autocomplete activity. This will be used to identify results from the
     * autocomplete activity in onActivityResult.
     */
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

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
        // Open the autocomplete activity when the button is clicked.
        Button openButton = (Button) findViewById(R.id.buttonFrom);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity();
            }
        });

//---------------------
        //--------------------------
        editTextFrom = (EditText) findViewById(R.id.editTextFrom);
        editTextFrom.setKeyListener(null);// ne lehessen módosítani
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

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);

                // Format the place's details and display them in the TextView.
                editTextFrom.setText(place.getAddress());

                // Display attributions if required.
                /*CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
                } else {
                    mPlaceAttribution.setText("");
                }*/
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
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
}
