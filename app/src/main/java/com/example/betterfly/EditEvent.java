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
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditEvent extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "eventPage";
    private EditText textName, textDate, textNOV, textLocation, textDes;
    event event1;
    public String eventName;
    public String date, loc, des;
    int nov;
    String numOfVol;
    DatePickerDialog.OnDateSetListener datePickerDoB;
    public Date DoE;
    DatabaseReference databaseReference;
    String eventID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);


        databaseReference = FirebaseDatabase.getInstance().getReference("Events");

        findViewById(R.id.backbtn).setOnClickListener(this);

        findViewById(R.id.save).setOnClickListener(this);

        textName = findViewById(R.id.name);
        textDate = findViewById(R.id.date);
        textNOV = findViewById(R.id.num);
        textLocation = findViewById(R.id.loc);
        textDes = findViewById(R.id.des);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        event1 = (event) intent.getSerializableExtra("event");
        if (bundle != null) {
            eventName = (String) bundle.get("name");
            eventID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            eventID = eventID + eventName;

            DateFormat format = new SimpleDateFormat("d/MM/yyyy", Locale.ENGLISH);
            date = format.format(bundle.get("date"));
            // DoE=format.parse(date);
            nov = (int) bundle.get("Number of Volunteers");
            loc = (String) bundle.get("location");
            des = (String) bundle.get("description");

            numOfVol = String.valueOf(nov);
            textName.setText(eventName);
            textDate.setText(date);
            textNOV.setText(numOfVol);
            textLocation.setText(loc);
            textDes.setText(des);

        }
        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        EditEvent.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        datePickerDoB,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        datePickerDoB = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyyy:" + dayOfMonth + "/" + month + "/" + year);

                date = dayOfMonth + "/" + month + "/" + year;
                textDate.setText(date);
                DateFormat format = new SimpleDateFormat("d/MM/yyyy", Locale.ENGLISH);
                try {
                    DoE = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                databaseReference.child(eventID).child("date").setValue(DoE);

            }
        };

    }
    public void update () {
        eventName = textName.getText().toString().trim();
        event1.setName(eventName);
        textName.setText(eventName);
        databaseReference.child(eventID).child("name").setValue(eventName);

        numOfVol = textNOV.getText().toString().trim();
        nov = Integer.parseInt(numOfVol);
        event1.setNov(nov);
        textNOV.setText(numOfVol);
        databaseReference.child(eventID).child("nov").setValue(nov);

        loc = textLocation.getText().toString().trim();
        event1.setLocation(loc);
        textLocation.setText(loc);
        databaseReference.child(eventID).child("location").setValue(loc);

        des = textDes.getText().toString().trim();
        event1.setDescreption(des);
        textDes.setText(des);
        databaseReference.child(eventID).child("desception").setValue(des);

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        event1.setOrg(id);
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbtn:
                finish();
                startActivity(new Intent(this, eventPage.class).putExtra("name" ,eventName )
                        .putExtra("description" , event1.getDescreption())
                        .putExtra("location", event1.getLocation())
                        .putExtra("date", event1.getDate())
                        .putExtra("Credit Hours", event1.getcHours())
                        .putExtra("Number of Volunteers", event1.getNov())
                        .putExtra("event",  event1));
                break;
            case R.id.save:
                update();
                Toast.makeText(this, "Event Edited", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(this, OrgProcessActivity.class));
                break;
        }
    }
}
