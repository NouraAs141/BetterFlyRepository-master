package com.example.betterfly;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.time.LocalDate;


public class vsignUp extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "vsignUp";
    private ProgressBar progressBar;
    EditText editTextEmail, editTextPassword , editTextRepeatPassword , editTextName, editTextDoB;
    DatePickerDialog.OnDateSetListener datePickerDoB;
    String date;
    Date DoB;
    boolean ageres;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vsign_up);

        editTextName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.chours);
        editTextPassword = findViewById(R.id.password_signup);

        editTextDoB = findViewById(R.id.DoB);
        editTextRepeatPassword = findViewById(R.id.repassword_signup);

       // progressBar = (ProgressBar) findViewById(R.id.progressbar);
//        progressBar.setVisibility(View.GONE);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.sign_up).setOnClickListener(this);
        editTextDoB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        vsignUp.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        datePickerDoB,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        datePickerDoB= new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                Log.d(TAG,"onDateSet: dd/mm/yyyy:"+ dayOfMonth+"/"+month+"/"+year);

                 date=dayOfMonth+"/"+month+"/"+year;
                editTextDoB.setText(date);

                LocalDate currentDate = LocalDate.now();
                int y = currentDate.getYear();
                int age = y - year;
                if( age >= 16 ){
                    ageres = true;
                }
                else{ ageres = false;}
            }
        };

        findViewById(R.id.sign_up).setOnClickListener(this);
    }
    private void registerUser() throws ParseException {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();

        final String password = editTextPassword.getText().toString().trim();
        String repaetPassword = editTextRepeatPassword.getText().toString().trim();


       // final String DobString = editTextDoB.getText().toString().trim();
        if(date!=null) {
            DateFormat format = new SimpleDateFormat("d/MM/yyyy", Locale.ENGLISH);
            DoB = format.parse(date);



        }

        if(email.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(email).matches()||password.isEmpty()|| password.length()<6||repaetPassword.isEmpty()|| !password.equals(repaetPassword)||name.isEmpty() ||DoB==null||!ageres) {

            if (email.isEmpty()) {
                editTextEmail.setError("Email is required");
                editTextEmail.requestFocus();
               // return;
            }


            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Please enter a valid email");
                editTextEmail.requestFocus();
              //  return;
            }


            if (password.isEmpty()) {
                editTextPassword.setError("Password is required");
                editTextPassword.requestFocus();
              //  return;
            }

            if (password.length() < 6) {
                editTextPassword.setError("Minimum length of password should be 6 characters");
                editTextPassword.requestFocus();
               // return;
            }

            if (repaetPassword.isEmpty()) {
                editTextRepeatPassword.setError("Re-write your password");
                editTextRepeatPassword.requestFocus();
              //  return;
            }

            if (!password.equals(repaetPassword)) {
                editTextPassword.setError("Your password does not match");
                editTextPassword.setText("");
                editTextRepeatPassword.setText("");
              //  return;
            }


            if (name.isEmpty()) {
                editTextName.setError("Please enter your name");
                editTextName.requestFocus();
                //return;
            }

            if (DoB==null) {
                editTextDoB.setError("Please enter your Date of Birth");
                editTextDoB.requestFocus();
                //return;
            }
            if(!ageres){
                editTextDoB.setError("You have to be 16 years or old");
                editTextDoB.requestFocus();
            }


            return;
        }
     //   progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Volunteer volUser = new Volunteer(
                            name,
                            email,
                            DoB

                    );

                    FirebaseDatabase.getInstance().getReference("Volunteer")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(volUser).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                      //      progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(vsignUp.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                finish();
                                Intent intent = new Intent(vsignUp.this, eventRetrievd.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            } else {
                                //display a failure message
                                Toast.makeText(vsignUp.this, getString(R.string.registration_fail), Toast.LENGTH_LONG).show();
                            }


                        }
                    });
                }
                else {
                    Toast.makeText(vsignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            }

        });
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.sign_up:
                try {
                    registerUser();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    }

}
