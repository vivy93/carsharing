package hu.uniobuda.nik.carsharing;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private Button buttonCreate;
    private Button buttonAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()== null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();

       /* setContentView(R.layout.activity_profile);
        AdsListFragment fragment = AdsListFragment.newInstance();// át kellene adni az user ID
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();*/

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText("Welcome "+ user.getEmail());

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonCreate = (Button) findViewById(R.id.buttonCreate);
        buttonAds = (Button) findViewById(R.id.buttonAds);

        buttonLogout.setOnClickListener(this);
        buttonCreate.setOnClickListener(this);
        buttonAds.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (v == buttonCreate){
            finish();

            /// TODO
            // előszőr ide kéne továbbjutni
            startActivity(new Intent(this, PostChooseActivity.class));

            //startActivity(new Intent(this, PostCarActivity.class));
        }

        if (v == buttonAds){
            finish();
            startActivity(new Intent(this,ListActivity.class));
        }
    }
}