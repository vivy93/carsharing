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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hu.uniobuda.nik.carsharing.model.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;
    private EditText editTextName;
    private EditText editTextDate;
    private EditText editTextTelephone;
    private RadioButton editRadioMale;
    private RadioButton editRadioFemale;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;

    /*temp button*/
    public Button buttonBackDoor;

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
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignIn);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextName=(EditText) findViewById(R.id.editTextName);
        editTextDate=    (EditText) findViewById(R.id.editTextDate);
        editTextTelephone=  (EditText) findViewById(R.id.editTextTelephone);
        editRadioMale = (RadioButton) findViewById(R.id.editRadioMale);
        editRadioFemale = (RadioButton) findViewById(R.id.editRadioFemale);

        buttonBackDoor  =(Button) findViewById(R.id.backdoor);

        buttonRegister.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
        buttonBackDoor.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v== buttonRegister)
        {
            RegisterUser();
        }
        if (v==textViewSignUp)
        {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if(v == buttonBackDoor)
        {
            Intent intent = new Intent(MainActivity.this,BackDoor.class);
            startActivity(intent);
        }
    }

    private void RegisterUser() {
        final String name = editTextName.getText().toString().trim();
        if (name.isEmpty())
        {
            Toast.makeText(this,"Please enter your name!",Toast.LENGTH_SHORT).show();
            return;
        }
        final String email = editTextEmail.getText().toString().trim();
        if (email.isEmpty())
        {
            Toast.makeText(this,"Please enter your email address!",Toast.LENGTH_SHORT).show();
            return;
        }
        final String password = editTextPassword.getText().toString().trim();
        if (password.isEmpty())
        {
            Toast.makeText(this,"Please enter your password!",Toast.LENGTH_SHORT).show();
            return;
        }
        final String Bdate = editTextDate.getText().toString().trim();
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        final Date birthDate;
        try {
            birthDate = format.parse(Bdate);
        } catch (ParseException e) {
            Toast.makeText(this,"Wrong birth date format!",Toast.LENGTH_SHORT).show();
            return;
        }

        final Boolean sex = editRadioMale.isChecked(); //0 female - 1 male

        if (!editRadioMale.isChecked() && !editRadioFemale.isChecked())
        {
            Toast.makeText(this,"Please choose your sexuality!",Toast.LENGTH_SHORT).show();
            return;
        }

        final String telephone = editTextTelephone.getText().toString().trim();
        if (telephone.isEmpty())
        {
            Toast.makeText(this,"Please enter your telephone number!",Toast.LENGTH_SHORT).show();
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
                            onAuthSuccess(task.getResult().getUser(), birthDate, telephone, name, password, sex);
                        } else
                        {
                            Toast.makeText(MainActivity.this, "Registration Error!", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();

                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user, Date birthDate, String telephone, String name, String password, Boolean sex) {

        // mock name for testing
        //String mockName = "Herbie Hancock";

        // mock save
        //writeNewUser(user.getUid(),mockName, user.getEmail());

        // real save
        writeNewUser(user.getUid(), name, user.getEmail(), password, birthDate, sex, telephone);

        startActivity(new Intent(new Intent(getApplicationContext(),ProfileActivity.class)));
        finish();
    }

    // mock save
    /*private void writeNewUser(String userId, String name, String email) {

     User user = new User(name, email);
     firebaseDatabase.child("users").child(userId).setValue(user);

    }*/


    //real save
    private void writeNewUser(String userId, String name, String email, String password, Date birthDate, Boolean sex, String telephone) {

        User user = new User(name, email, password, birthDate, sex, telephone);
        firebaseDatabase.child("users").child(userId).setValue(user);
    }



}
