package hu.uniobuda.nik.carsharing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import junit.framework.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hu.uniobuda.nik.carsharing.model.Advertisement;
import hu.uniobuda.nik.carsharing.model.TravelMode;
import hu.uniobuda.nik.carsharing.model.User;

public class PostCarActivity extends AppCompatActivity implements View.OnClickListener {

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


    // MOCK DATA
    /*private String from;
    private String to;
    private String node1;
    private String node2;
    private Date when;
    private Integer seats;
    private String uid;
    // END OF MOCK DATA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_car);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        /*
        textView-k feltöltése az xml-ből
        when parse-olása dátumra
        from, to, node1, node2 szép formátumra a google maps API-val
            ezek mellett kéne egy "check" button is sztem
         */

        editTextFrom = (EditText) findViewById(R.id.editTextFrom);
        editTextTo= (EditText) findViewById(R.id.editTextTo);
        node1 =(EditText) findViewById(R.id.node1);
        node2 =(EditText) findViewById(R.id.node2);
        showDate = (EditText) findViewById(R.id.showDate);
        editTextFreeSeats= (EditText) findViewById(R.id.editTextFreeSeats);

        buttonPost = (Button) findViewById(R.id.buttonPost);
        buttonPost.setOnClickListener(this);


        // MOCK DATA
        uid = "1";

        from = "Bekescsaba Petofi utca";
        to = "Budapest Deak Ferenc ter";
        node1 = "Gyula";
        node2 = null;
        seats = 2;

        String dateInString = "30-04-2017 10:20:00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        try {
            when = format.parse(dateInString);
        } catch (ParseException e) {
            Toast.makeText(this,"Wrong date format!",Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "creating mock data: success");
        // END OF MOCK DATA*/

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
