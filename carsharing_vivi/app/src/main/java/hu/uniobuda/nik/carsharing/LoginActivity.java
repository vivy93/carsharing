package hu.uniobuda.nik.carsharing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth= FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!= null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }

        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignup = (TextView) findViewById(R.id.textViewSignUp);
        progressDialog = new ProgressDialog(this);

        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v == buttonSignIn) {
            userLogin();
        }
        if(v == textViewSignup) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void userLogin()
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please enter your email",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please enter your password",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Sign In User...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful())
                {
                    finish();
                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                }
                else{
                    wrongUserAndPass();
                }

            }
        });
    }

    void wrongUserAndPass()
    {
        Toast.makeText(this,"Username or password is incorrect!",Toast.LENGTH_SHORT).show();
        return;
    }





}