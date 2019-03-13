package com.example.betterfly;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class viewVolunteers extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    DatabaseReference databaseReference, databaseReference2;
    List<Volunteer> VolunteerList;
    public String eventName;
    public String date, loc, des;
    int nov;
    DatePickerDialog.OnDateSetListener datePickerDoB;
    public Date DoE;
    event event1;
    String eventID;
    ArrayList<String> emails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_volunteers);


        listView = findViewById(R.id.list_view);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Events");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Volunteer");
        VolunteerList = new ArrayList<Volunteer>();

        findViewById(R.id.backbtn).setOnClickListener(this);
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
            emails = bundle.getStringArrayList("emails");
            eventID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            eventID = eventID + eventName;
            databaseReference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot volunteerSnapshot : dataSnapshot.getChildren()) {
                        String email = volunteerSnapshot.child("email").getValue().toString();
                        if (emails.contains(email)) {
                            Volunteer volunteer = volunteerSnapshot.getValue(Volunteer.class);
                            if (VolunteerList.contains(volunteer))
                                continue;

                            else
                                VolunteerList.add(volunteer);

                        }
                    }
                    volunteerInfoAdaptor volunteerinfoAdaptor = new volunteerInfoAdaptor(viewVolunteers.this, VolunteerList);
                    listView.setAdapter(volunteerinfoAdaptor);

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

        }




    }
    @Override
    public void onClick (View v){
        switch (v.getId()) {
            case R.id.backbtn:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, eventPage.class));
                break;
        }
    }
}
