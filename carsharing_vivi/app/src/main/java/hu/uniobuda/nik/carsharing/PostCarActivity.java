package hu.uniobuda.nik.carsharing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hu.uniobuda.nik.carsharing.model.Advertisement;
import hu.uniobuda.nik.carsharing.model.TravelMode;

enum FTN {From, To, Node1, Node2};

public class PostCarActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "PostCarActivity";
    /**
     * Request code for the autocomplete activity. This will be used to identify results from the
     * autocomplete activity in onActivityResult.
     */
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;

    private Button buttonPost;

    private FTN ftn;

    private EditText editTextFrom;
    private String fromID;
    private EditText editTextTo;
    private EditText node1;
    private String node1ID;
    private EditText node2;
    private String node2ID;
    private EditText showDate;
    private EditText editTextFreeSeats;
    private Date travelDate;
    private Integer seats;

    // MOCK DATA
   /* private String from;
    private String to;
    private String node1;
    private String node2;
    private Date when;
    private Integer seats;
    private String uid;
    */
    // END OF MOCK DATA


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_car);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    //pati
        /*
         textView-k feltöltése az xml-ből
         when parse-olása dátumra
         from, to, node1, node2 szép formátumra a google maps API-val
             ezek mellett kéne egy "check" button is sztem
          */

//IMi:-----------------------------
        // Open the autocomplete activity when the button is clicked.
        editTextFrom = (EditText) findViewById(R.id.editTextFrom);
        editTextFrom.setKeyListener(null);// ne lehessen módosítani
        editTextFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftn = FTN.From;
                openAutocompleteActivity();
            }
        });

        editTextTo = (EditText) findViewById(R.id.editTextTo);
        editTextTo.setKeyListener(null);// ne lehessen módosítani
        editTextTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ftn = FTN.To;
                openAutocompleteActivity();
            }
        });

        node1 = (EditText) findViewById(R.id.node1);
        node1.setKeyListener(null);
        node1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftn = FTN.Node1;
                openAutocompleteActivity();
                ;
            }
        });
        node2 = (EditText) findViewById(R.id.node2);
        node2.setKeyListener(null);
        node2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftn = FTN.Node2;
                openAutocompleteActivity();
                ;
            }
        });
        //----------------
        fromID = "";
        node1ID = "";
        node2ID = "";
        showDate = (EditText) findViewById(R.id.showDate);
        editTextFreeSeats = (EditText) findViewById(R.id.editTextFreeSeats);

        buttonPost = (Button) findViewById(R.id.buttonPost);
        buttonPost.setOnClickListener(this);
    }
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.arg1 == 1)
                Toast.makeText(getApplicationContext(),"Wrong date format!", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onClick(View view) {
        if (view == buttonPost) {
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
                // Which
                switch (ftn) {
                    case From:
                        editTextFrom.setText(place.getAddress());
                        fromID = place.getId();
                        break;
                    case To:
                        editTextTo.setText(place.getAddress());
                        break;
                    case Node1:
                        node1.setText(place.getAddress());
                        node1ID = place.getId();
                        break;
                    case Node2:
                        node2.setText(place.getAddress());
                        node2ID = place.getId();
                        break;
                }


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
 /*// SAVE MOCK DATA
          FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Advertisement ad = new Advertisement(uid, TravelMode.BY_CAR, when, from, to, node1, node2, seats);

          firebaseDatabase.child("advertisements").child(currentUser.getUid()).setValue(ad);

          Log.d(TAG, "saving mock data: success");
         // END OF SAVING MOCK DATA*/

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String dateInString = showDate.getText().toString().trim();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd/hh-mm-ss", Locale.getDefault());
        try {
            travelDate = format.parse(dateInString);
        } catch (ParseException e) {
            //Toast.makeText(this, "Wrong date format!", Toast.LENGTH_SHORT).show();
            Message msg = handler.obtainMessage();
            msg.arg1 = 1;
            handler.sendMessage(msg);
        }

        String num = editTextFreeSeats.getText().toString().trim();
        try {
            seats = Integer.parseInt(num);
        } catch (Exception e) {
            Toast.makeText(this, "Wrong number format!", Toast.LENGTH_SHORT).show();
        }

        Advertisement ad = new Advertisement(TravelMode.BY_CAR, travelDate, editTextFrom.getText().toString().trim(), fromID.trim(), editTextTo.getText().toString().trim(), node1.getText().toString().trim(), node1ID.trim(), node2.getText().toString().trim(), node2ID.trim(), seats);
        firebaseDatabase.child("advertisements").child(currentUser.getUid()).push().setValue(ad);

        Log.d(TAG, "saving real data: success");
    }
}
