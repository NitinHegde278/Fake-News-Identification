package com.example.news;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
    Button bForgot, backForgot;
    EditText etForgot;
    TextView tvForgot;
    private static final String TAG = "ForgotActivity";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        bForgot = findViewById(R.id.bForgot);
        etForgot = findViewById(R.id.etForgot);
        tvForgot = findViewById(R.id.tvForgot);
        backForgot = findViewById(R.id.backForgot);
        bForgot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etForgot.getText().toString().isEmpty()) {
                    etForgot.setError("Please enter E-mail Id");
                    etForgot.requestFocus();
                } else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(etForgot.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    tvForgot.setVisibility(View.VISIBLE);
                                    tvForgot.setText("Reset link sent to E-mail id - "+etForgot.getText());
                                    tvForgot.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvForgot.setVisibility(View.INVISIBLE);
                                        }
                                    },8000);
                                    Log.d(TAG, "Email sent.");
                                }
                                else
                                {
                                    tvForgot.setText("Something went wrong!");
                                }

                            }
                        });
            }
            }
        });

        backForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
