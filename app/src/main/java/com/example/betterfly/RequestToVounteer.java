package com.example.betterfly;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


public class RequestToVounteer extends AppCompatActivity  implements View.OnClickListener{

    private TextView textViewEventName , textViewOrgName, textViewDiscreption , textViewLoc , textViewDate , textViewVolunteerNum;

    event event;
    public String orgName , location ,des , id;
    public String eventName;
    public String date;
    public int nov;
    FirebaseAuth auth;
    String userEmail;
    FirebaseUser user;
    ArrayList<String> Emails;
    Button ReqButton , Withdraw , WButton;



    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volunteering_request);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // String name = user.getDisplayName();
            userEmail = user.getEmail();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Events");



        findViewById(R.id.backbtn).setOnClickListener(this);
       // findViewById(R.id.Request).setOnClickListener(this);

        textViewEventName = findViewById(R.id.EvName);
        textViewDiscreption = findViewById(R.id.description);
        textViewOrgName = findViewById(R.id.OrgName);
        textViewLoc = findViewById(R.id.loc);
        textViewDate = findViewById(R.id.date);
        textViewVolunteerNum = findViewById(R.id.VolNum);
        ReqButton= findViewById(R.id.vReq);
        ReqButton.setOnClickListener(this);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        event= (event) intent.getSerializableExtra("event");
        if(bundle!=null){
            eventName = (String) bundle.get("name");
            DateFormat format = new SimpleDateFormat("d/MM/yyyy", Locale.ENGLISH);
            date=format.format(bundle.get("date"));
            // DoE=format.parse(date);
            nov= (int) bundle.get("Number of Volunteers");
            location =(String) bundle.get("location");
            des= (String) bundle.get("description");
            id= (String) bundle.get("id");

            textViewEventName.setText(eventName);
            textViewDiscreption.setText(des);
            textViewLoc.setText(location);
            textViewDate.setText(date);
            textViewVolunteerNum.setText(String.valueOf(nov));

        }

        Emails =event.emails;
        if(Emails!=null) {
            for (int i = 0; i < Emails.size(); i++) {
                if (Emails.get(i).equals(userEmail)) {
                  //  ReqButton.setEnabled(false);
                    Withdraw = ReqButton;
                  Withdraw.setId(R.id.withdraw);
                    Withdraw.setText("Want to withdraw ?");
                    ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.RV);
                    layout.removeView(ReqButton);
                    layout.addView(Withdraw);
                    break;
                }
            }
        }
        Withdraw = findViewById(R.id.withdraw);
        Withdraw.setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backbtn:
                finish();
                startActivity(new Intent(this, eventRetrievd.class));
                break;


            case R.id.vReq:

                int check=0;
                if (userEmail != null) {
                    check = event.addEmail(userEmail);
                    if(check == 2) {
                        Emails = new ArrayList<>();
                        event.setEmails(Emails);
                        databaseReference.child(id + eventName).child("emails").setValue(Emails);
                       // Toast.makeText(RequestToVounteer.this, "You are now added to this event as a volunteer", Toast.LENGTH_SHORT).show();
                    }
                        check = event.addEmail(userEmail);
                        if (check == 1)
                            Toast.makeText(RequestToVounteer.this, "You are now added to this event as a volunteer", Toast.LENGTH_SHORT).show();
                        else
                            if(check==-1)
                            Toast.makeText(RequestToVounteer.this, "Sorry we reach the desired number of volunteers", Toast.LENGTH_SHORT).show();

                        String id = event.org + event.name;

                        FirebaseDatabase.getInstance().getReference("Events").child(id).setValue(event);
                    }

        else

        Toast.makeText(RequestToVounteer.this, "there is an error", Toast.LENGTH_SHORT).show();

                finish();
                startActivity(new Intent(this, eventRetrievd.class));


                break;

            case  R.id.withdraw:
                            event.removeEmail(userEmail);
                            Toast.makeText(RequestToVounteer.this, "You withdrew succussfully", Toast.LENGTH_SHORT).show();
                            String id = event.org + event.name;
                            FirebaseDatabase.getInstance().getReference("Events").child(id).setValue(event);
                            finish();
                startActivity(new Intent(this, eventRetrievd.class));
            break;

        }

    }

}
