package com.example.betterfly;


import com.google.firebase.FirebaseApp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    DatabaseReference VdatabaseReference;
    DatabaseReference OdatabaseReference;
    public List<Volunteer> vList;
    public  List<Organization> organizationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        mAuth =FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressbar);

        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
        findViewById(R.id.textViewforget).setOnClickListener(this);

        VdatabaseReference= FirebaseDatabase.getInstance().getReference().child("Volunteer");
        OdatabaseReference=FirebaseDatabase.getInstance().getReference().child("Organization");

        vList=new ArrayList<Volunteer>();
        organizationList = new ArrayList<Organization>();



        VdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot vSnapshot : dataSnapshot.getChildren()) {

                    Volunteer volunteer= vSnapshot.getValue(Volunteer.class);
                    if(!vList.contains(volunteer))
                        vList.add(volunteer);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        OdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot oSnapshot : dataSnapshot.getChildren()) {

                    Organization organization= oSnapshot.getValue(Organization.class);
                    if(!organizationList.contains(organization))
                        organizationList.add(organization);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    private void userLogin() {
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if(email.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(email).matches()|| password.isEmpty()|| password.length() < 6) {
            if (email.isEmpty()) {
                editTextEmail.setError("Email is required");
                editTextEmail.requestFocus();
            }

            if (!email.isEmpty()&&!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Please enter a valid email");
                editTextEmail.requestFocus();
            }

            if (password.isEmpty()) {
                editTextPassword.setError("Password is required");
                editTextPassword.requestFocus();
            }

            if (!password.isEmpty() && password.length() < 6) {
                editTextPassword.setError("Minimum lenght of password should be 6");
                editTextPassword.requestFocus();
            }
            return;

        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    boolean volunteer = false ;
                    boolean org  = false;
                    for (int i =0 ; i< vList.size() ; i++) {
                        if (email.equals(vList.get(i).email)) {
                            finish();
                            Intent intent = new Intent(MainActivity.this, eventRetrievd.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            volunteer = true;
                            break;
                        }
                    }

                    for (int i =0 ; i< organizationList.size() ; i++) {
                        if (email.equals(organizationList.get(i).email)) {
                            if (!organizationList.get(i).status.equals("PROCESSING")) {
                                finish();
                                Intent intent = new Intent(MainActivity.this, OrgProcessActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                org = true;
                                break;
                            } else {
                                Toast.makeText(getApplicationContext(), "Sorry we are still processing your request", Toast.LENGTH_SHORT).show();
                                org=true;
                                break;
                            }

                        }
                    }

                    if(volunteer==false && org==false){
                        finish();
                        Intent intent = new Intent(MainActivity.this, dataRetrieved.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewSignup:
                finish();
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case R.id.textViewforget:
                finish();
                startActivity(new Intent(this, forgetPass.class));
                break;

            case R.id.buttonLogin:
                userLogin();
                break;
           /* case R.id.button2:
                finish();
                startActivity(new Intent(this, EditProfile.class));
                break;*/

        }
    }
}
