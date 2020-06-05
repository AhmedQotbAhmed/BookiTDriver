package com.example.bookitdriver.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookitdriver.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;

public class   MainActivity extends AppCompatActivity implements View.OnClickListener {


    private LinearLayout signIn_Content;
    private EditText email_edt;
    private EditText password_edt;
    private AlertDialog loadingBar;
    private SharedPreferences sp;
    private SharedPreferences.Editor Ed;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    private String onlineUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp=getSharedPreferences("driverLogin", MODE_PRIVATE);
        Ed=sp.edit();
        mAuth = FirebaseAuth.getInstance();
        email_edt=findViewById(R.id.email_signIn);
        password_edt=findViewById(R.id.pass_signIn);
        final FloatingActionButton signIn = findViewById(R.id.sign_in_btn);
        final TextView signUp = findViewById(R.id.sign_up_btn);
        TextView frg_btn = findViewById(R.id.forgotPass_btn);
        signIn_Content=findViewById(R.id.sign_in_content);
        loadingBar=new SpotsDialog(this);



        SharedPreferences sp1=this.getSharedPreferences("driverLogin", MODE_PRIVATE);

        String unm=sp1.getString("Unm", null);
        String pass = sp1.getString("Psw", null);
        if (unm != null && pass != null) {
            if (!unm.isEmpty() && !pass.isEmpty()) {


                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                AllowAccessToAccount(unm, pass);

            }
        }

        // doubleClick is
        //"A android library lo handle double click on android Views components. You just need to call it on your view
        // in  https://github.com/pedromassango/doubleClick imp "
        signUp.setOnClickListener(this);
        frg_btn.setOnClickListener(this);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn.setEnabled(false);
                String email_Str = email_edt.getText().toString();
                String password_Str = password_edt.getText().toString();
                if (!email_Str.isEmpty() && !password_Str.isEmpty()) {
                    LoginUser();


                } else {
                    email_edt.setError("Email is required");
                    password_edt.setError("password is required");

                    signIn.setEnabled(true);
                }
            }
        });
    }


    private void AllowAccessToAccount(final String email_str, final String password_str) {

        loadingBar.setTitle("Login Account");
        loadingBar.setMessage("Loading");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        final String email = (email_str.replace("@", "-")).replace(".", "_");

        mAuth.signInWithEmailAndPassword(email_str, password_str)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Ed.putString("Unm", email_str);
                            Ed.putString("Psw", password_str);
                            Ed.commit();

                            // setUser Online
                            onlineUserId=mAuth.getCurrentUser().getUid();

                            rootRef.child("OnlineUsers").child("Drivers").child(onlineUserId).setValue(true);





                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        } else {
                            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("Drivers").child(email).child("profile_inf").exists()) {
                                        loadingBar.dismiss();
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        password_edt.setError("password is incorrect");
                                    } else {
                                        loadingBar.dismiss();
                                        email_edt.setError("Email do not exists");

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e(" logIn onCancelled", databaseError.toString());
                                }
                            });

                        }

                    }
                });

    }


    private void LoginUser() {

        String email_Str = email_edt.getText().toString();
        String password_Str = password_edt.getText().toString();


        if (email_edt != null && password_edt != null) {

            if (password_Str.length() < 8) {
                password_edt.setError(" Minimum length of Password is should be 8 ");
                password_edt.requestFocus();
            } else if (email_Str.isEmpty()) {
                email_edt.setError("Email is required");
                email_edt.requestFocus();

            } else {

                loadingBar.setTitle("Login Account");
                loadingBar.setMessage("Please wait, while we are checking the credentials");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                AllowAccessToAccount(email_Str, password_Str);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up_btn:
                startActivity( new Intent(this, SignUpActivity.class));
                break;


            case R.id.forgotPass_btn:

                break;
        }
    }


}
