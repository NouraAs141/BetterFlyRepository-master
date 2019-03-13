package com.example.betterfly;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrgProcessActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ViewDatabase";
    private ListView listView;
    DatabaseReference databaseReference;
   private FirebaseAuth.AuthStateListener mAuthListener;
    List<event> eventsList;
    private FirebaseAuth mAuth;
    private Button add;
    FirebaseUser user;
    private  String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_process);
        listView=(ListView) findViewById(R.id.list_view1);
        findViewById(R.id.signOut).setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Events");
        user=mAuth.getCurrentUser();
        userID = user.getUid();
        eventsList=new ArrayList<>();



        add=findViewById(R.id.buttonAdd);
        findViewById(R.id.buttonAdd).setOnClickListener(this);



        }

    @Override

    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot eventSnapshot:dataSnapshot.getChildren()){
                    String org=eventSnapshot.child("org").getValue().toString();
                    if(userID.equals(org)) {

                        event event1 = eventSnapshot.getValue(event.class);
                        eventsList.add(event1);
                    }
                }
                eventInfoAdaptor eventinfoAdaptor=new eventInfoAdaptor(OrgProcessActivity.this, eventsList);
                listView.setAdapter(eventinfoAdaptor);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonAdd:
                finish();
                startActivity(new Intent(this, postEvent.class));
                break;

            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

    }
}

