package com.example.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegisterMain extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton google_signIn;
    private String TAG = "RegisterMain";
    private int RC_SIGN_IN =1;
    EditText emailId,password;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);

        mFirebaseAuth= FirebaseAuth.getInstance();
        emailId=findViewById(R.id.editText);
        password=findViewById(R.id.editText2);
        btnSignUp=findViewById(R.id.button);
        tvSignIn=findViewById(R.id.textView);
        google_signIn = findViewById(R.id.google_signIn);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailId.getText().toString();
                String pwd = password.getText().toString();

                if (email.isEmpty()) {
                    emailId.setError("Please enter Email ID");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {

                    password.setError("Please enter Password");
                    password.requestFocus();

                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(RegisterMain.this, "Fields Are Empty!", Toast.LENGTH_SHORT).show();

                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterMain.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterMain.this, "SignUp Unsuccessful, Please try again !", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(RegisterMain.this, MainActivity.class));
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterMain.this, "ERROR OCCURRED!! ", Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(RegisterMain.this,LoginActivity.class);
                startActivity(i);

            }
        });

        GoogleSignInOptions gso= new GoogleSignInOptions.Builder((GoogleSignInOptions.DEFAULT_SIGN_IN))
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });
    }

    private void SignIn(){
        Intent SignInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(SignInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(RegisterMain.this,"You are Logged In",Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle(account);
                startActivity(new Intent(RegisterMain.this, MainActivity.class));
                finish();

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(RegisterMain.this,"Sign In Failed.",Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        //updateUI(currentUser);
        mGoogleSignInClient.signOut();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void updateUI(FirebaseUser fUser) {
        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account != null){
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();

            Toast.makeText(RegisterMain.this,"Welcome "+personName, Toast.LENGTH_SHORT).show();
        }
    }


}
