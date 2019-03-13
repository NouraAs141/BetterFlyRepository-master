package com.example.betterfly;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

public class dataRetrieved extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    DatabaseReference databaseReference;
    List<Organization>organizationList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_retrieved);


        listView=findViewById(R.id.list_view);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Organization");

        organizationList=new ArrayList<Organization>();

        findViewById(R.id.signOut).setOnClickListener(this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot organizationSnapshot:dataSnapshot.getChildren()){

                    Organization organization=organizationSnapshot.getValue(Organization.class);
                    if(organizationList.contains(organization))
                        continue;

                    else
                        organizationList.add(organization);

                }
                organizationInfoAdaptor organizationinfoAdaptor=new organizationInfoAdaptor(dataRetrieved.this,organizationList);
                listView.setAdapter(organizationinfoAdaptor);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }



   

}
