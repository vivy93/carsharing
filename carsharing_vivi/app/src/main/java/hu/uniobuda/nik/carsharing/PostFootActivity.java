package hu.uniobuda.nik.carsharing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class PostFootActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PostCarActivity";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;

    private Button buttonPost;
    private EditText dateOnFoot;
    private EditText editTextFrom;
    private Date travelDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_foot);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        editTextFrom = (EditText) findViewById(R.id.editTextFrom);
        dateOnFoot= (EditText) findViewById(R.id.dateOnFoot);

        buttonPost = (Button) findViewById(R.id.buttonPost);
        buttonPost.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==buttonPost){
            postAd();
            //finish();

            //Log.d(TAG, "finishing " + TAG + ": success");

            startActivity(new Intent(this, ProfileActivity.class));
        }
    }

    private void postAd() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String dateInString = dateOnFoot.getText().toString().trim();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd/hh-mm-ss", Locale.getDefault());
        try {
            travelDate = format.parse(dateInString);
        } catch (ParseException e) {
            Toast.makeText(this,"Wrong date format!",Toast.LENGTH_SHORT).show();
        }

        Advertisement ad = new Advertisement(TravelMode.ON_FOOT, travelDate, editTextFrom.getText().toString().trim(), null, null, null, null);
        firebaseDatabase.child("advertisements").child(currentUser.getUid()).push().setValue(ad);

        Log.d(TAG, "creating real data: success");
    }
}
