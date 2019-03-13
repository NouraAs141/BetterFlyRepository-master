package com.example.betterfly;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;


public class eventRetrievd extends AppCompatActivity {

    private ListView listView;
    DatabaseReference databaseReference;
    public  List<event>eventList;
    public List<String>eventsName;
    MaterialSearchView searchView ;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(eventRetrievd.this,eventRetrievd.class));
                    finish();

                    return true;

                case R.id.profile:

                    startActivity(new Intent(eventRetrievd.this, vHome.class));
                    finish();

                    return true;

                case R.id.logout:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(eventRetrievd.this, MainActivity.class));
                    finish();
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search for events");
        toolbar.setTitleTextColor(Color.parseColor("#708090"));

        searchView = (MaterialSearchView)findViewById(R.id.search_view);
        listView=findViewById(R.id.list_view);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Events");

        eventList=new ArrayList<event>();
        eventsName= new ArrayList<String>();

        // findViewById(R.id.signOut).setOnClickListener(this);
        //  SearchView searchView = (SearchView)findViewById(R.id.searchEditText);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {

                    event eventObj = eventSnapshot.getValue(event.class);
                    if(eventList.contains(eventObj))
                        continue;
                    else{
                        eventList.add(eventObj);
                        eventsName.add(eventObj.name);
                    }

                }
                eventinfoAdaptorv eventinfoAdaptor = new eventinfoAdaptorv(eventRetrievd.this, eventList);
                listView.setAdapter(eventinfoAdaptor);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

                eventinfoAdaptorv eventinfoAdaptor = new eventinfoAdaptorv(eventRetrievd.this, eventList);
                listView.setAdapter(eventinfoAdaptor);
            }

            @Override
            public void onSearchViewClosed() {

                eventinfoAdaptorv eventinfoAdaptor = new eventinfoAdaptorv(eventRetrievd.this, eventList);
                listView.setAdapter(eventinfoAdaptor);

            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()) {
                    List<event> list = new ArrayList<event>();
                    for (int i =0 ; i <eventList.size() ; i++) {

                        if (eventList.get(i).name.contains(newText))
                            list.add(eventList.get(i));
                    }

                    eventinfoAdaptorv eventinfoAdaptor = new eventinfoAdaptorv(eventRetrievd.this, list);
                    listView.setAdapter(eventinfoAdaptor);



                }
                else{
                    ArrayAdapter adapter = new ArrayAdapter(eventRetrievd.this, android.R.layout.simple_list_item_1,eventsName );
                    listView.setAdapter(adapter);
                }

                return true;
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }


}