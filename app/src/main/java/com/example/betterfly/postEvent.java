package com.example.betterfly;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Annotation;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import android.app.DatePickerDialog;

public class postEvent extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "postEvent";
    EditText editTextName, editTextLoc, editTextDisc, editTextnov, editTextDoB, EditTextch;
    DatePickerDialog.OnDateSetListener datePickerDoB;
    String date;
    Date DoE;

    FirebaseAuth mAuth;
    DatabaseReference databaseEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);


        editTextName = findViewById(R.id.name);
        editTextLoc = findViewById(R.id.loc);
        editTextDisc = findViewById(R.id.desc);
        editTextnov = findViewById(R.id.NoV);
        editTextDoB = findViewById(R.id.DoB);
        EditTextch = findViewById(R.id.ch);


        databaseEvents = FirebaseDatabase.getInstance().getReference("Events");

        findViewById(R.id.post).setOnClickListener(this);
        editTextDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        postEvent.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        datePickerDoB,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        datePickerDoB = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyyy:" + dayOfMonth + "/" + month + "/" + year);

                date = dayOfMonth + "/" + month + "/" + year;
                editTextDoB.setText(date);

            }
        };

        findViewById(R.id.post).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post:
                try {
                    newPost();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void newPost() throws ParseException {
       final String name = editTextName.getText().toString().trim();
        String loc = editTextLoc.getText().toString().trim();
        String disc = editTextDisc.getText().toString().trim();
        String ch = EditTextch.getText().toString().trim();
        String snov = editTextnov.getText().toString().trim();
        int nov=0,h=0;



        if (date != null) {
            DateFormat format = new SimpleDateFormat("d/MM/yyyy", Locale.ENGLISH);
            DoE = format.parse(date);
            if(System.currentTimeMillis()>DoE.getTime()){
                editTextDoB.setError("Please Don't enter a passed date");
                editTextDoB .requestFocus();}
        }
        if (snov.isEmpty()){}
        else{
            nov = Integer.parseInt(snov);
        }

        if (ch.isEmpty()){}
        else{
            h = Integer.parseInt(ch);
        }

        if (name.isEmpty() || loc.isEmpty() || disc.isEmpty() || DoE == null || snov.isEmpty() || ch.isEmpty()|| h<1 || h >8 ) {

            if (name.isEmpty()) {
                editTextName.setError("Name is required");
                editTextName.requestFocus();

            }
            if (ch.isEmpty()) {
                EditTextch.setError("Number of hours is required");
                EditTextch.requestFocus();

            }

            if (loc.isEmpty()) {
                editTextLoc.setError("Location is required");
                editTextLoc.requestFocus();

            }

            if (disc.isEmpty()) {
                editTextDisc.setError("Description is required");
                editTextDisc.requestFocus();

            }

            if (snov.isEmpty()) {
                editTextnov.setError("Number of volunteers is required");
                editTextnov.requestFocus();
            }

            if (DoE == null) {
                editTextDoB.setError("Please enter The Date of event");
                editTextDoB.requestFocus();

            }
            if (h<1 || h >8) {
                EditTextch.setError("Number of volunteers should be between 1-8");
                EditTextch.requestFocus();
            }


            return;
        }


        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(loc) && !TextUtils.isEmpty(disc) && !TextUtils.isEmpty(snov)) {
          final   String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
          final ArrayList<String> emails = new ArrayList<>();
            event e = new event(id, name, disc, DoE, h, loc, nov , emails);

            databaseEvents.child(id + name)
                    .setValue(e).addOnCompleteListener(new OnCompleteListener<Void>() {

                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    databaseEvents.child(id + name).child("emails").setValue(emails);
                    if (task.isSuccessful()) {
                        Toast.makeText(postEvent.this, "Event posted", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(postEvent.this, OrgProcessActivity.class));
                    } else {
                        Toast.makeText(postEvent.this, "Fill all feild", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
