package com.example.betterfly;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class eventPage extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "eventPage";
    private TextView textViewName, textViewDate, textViewNOV, textViewLocation, textViewDes;
    event event1;
    public String eventName;
    public String date, loc, des;
    int nov;
    DatePickerDialog.OnDateSetListener datePickerDoB;
    public Date DoE;
    DatabaseReference databaseReference;
    String eventID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);

        databaseReference = FirebaseDatabase.getInstance().getReference("Events");

        findViewById(R.id.backbtn).setOnClickListener(this);
        findViewById(R.id.edit).setOnClickListener(this);
        findViewById(R.id.view).setOnClickListener(this);


        textViewName = findViewById(R.id.name);
        textViewDate = findViewById(R.id.date);
        textViewNOV = findViewById(R.id.num);
        textViewLocation = findViewById(R.id.loc);
        textViewDes = findViewById(R.id.des);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        event1 = (event) intent.getSerializableExtra("event");
        if (bundle != null) {
            eventName = (String) bundle.get("name");
            DateFormat format = new SimpleDateFormat("d/MM/yyyy", Locale.ENGLISH);
            date = format.format(bundle.get("date"));
            // DoE=format.parse(date);
            nov = (int) bundle.get("Number of Volunteers");
            loc = (String) bundle.get("location");
            des = (String) bundle.get("description");
            eventID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            eventID = eventID + eventName;
            String numOfVol = String.valueOf(nov);
            textViewName.setText(eventName);
            textViewDate.setText(date);
            textViewNOV.setText(numOfVol);
            textViewLocation.setText(loc);
            textViewDes.setText(des);

        }





    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbtn:
                finish();
                startActivity(new Intent(this, OrgProcessActivity.class));
                break;
            case R.id.edit:
                finish();
                startActivity(new Intent(this, EditEvent.class).putExtra("name" ,eventName )
                        .putExtra("description" , event1.getDescreption())
                        .putExtra("location", event1.getLocation())
                        .putExtra("date", event1.getDate())
                        .putExtra("Credit Hours", event1.getcHours())
                        .putExtra("Number of Volunteers", event1.getNov())
                        .putExtra( "event",  event1));
                break;

            case R.id.view:
                finish();
                startActivity(new Intent(this, viewVolunteers.class).putExtra("name" ,eventName )
                        .putExtra("description" , event1.getDescreption())
                        .putExtra("location", event1.getLocation())
                        .putExtra("date", event1.getDate())
                        .putExtra("Credit Hours", event1.getcHours())
                        .putExtra("Number of Volunteers", event1.getNov())
                        .putStringArrayListExtra("emails",event1.getEmails())
                        .putExtra( "event",  event1));
                break;

        }
    }
}