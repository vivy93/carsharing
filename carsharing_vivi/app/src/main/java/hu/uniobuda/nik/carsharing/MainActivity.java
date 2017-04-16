package hu.uniobuda.nik.carsharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hu.uniobuda.nik.carsharing.model.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegistered;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        if (firebaseAuth.getCurrentUser()!= null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }

        progressDialog = new ProgressDialog(this);
        buttonRegistered = (Button) findViewById(R.id.buttonRegister);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignIn);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonRegistered.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v==buttonRegistered)
        {
            RegisterUser();
        }
        if (v==textViewSignUp)
        {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void RegisterUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                if (task.isSuccessful()) {
                    onAuthSuccess(task.getResult().getUser());
                } else
                {
                    Toast.makeText(MainActivity.this, "Registration Error!", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();

            }
        });
    }

    private void onAuthSuccess(FirebaseUser user) {

        // mock name for testing
        String mockName = "Herbie Hancock";

        // mock save
        writeNewUser(user.getUid(), mockName, user.getEmail());

        // real save
        // writeNewUser(user.getUid(), mockName, user.getEmail(), password, birthDate, sex, telephone);

        startActivity(new Intent(new Intent(getApplicationContext(),ProfileActivity.class)));
        finish();
    }

    // mock save
    private void writeNewUser(String userId, String name, String email) {

        User user = new User(name, email);
        firebaseDatabase.child("users").child(userId).setValue(user);
    }


    /* real save
    private void writeNewUser(String userId, String name, String email, String password, Date birthDate, Boolean sex, String telephone) {

        User user = new User(name, email, password, birthDate, sex, telephone);
        firebaseDatabase.child("users").child(userId).setValue(user);
    }
    */


}
