package com.example.betterfly;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class volunteerprofile extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    Volunteer volunteer= new Volunteer();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_database1);

        mListView = (ListView) findViewById(R.id.listview);
        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(this);

        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be useable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            volunteer.setName(ds.child(userID).getValue(Volunteer.class).getName()); //set the name
            volunteer.setEmail(ds.child(userID).getValue(Volunteer.class).getEmail()); //set the email
            volunteer.setPhone(ds.child(userID).getValue(Volunteer.class).getPhone()); //set the phone_num

            //display all the information
            Log.d(TAG, "showData: name: " + volunteer.getName());
            Log.d(TAG, "showData: email: " + volunteer.getEmail());
            Log.d(TAG, "showData: phone_num: " + volunteer.getPhone());

            ArrayList<String> array  = new ArrayList<>();
            array.add(volunteer.getName());
            array.add(volunteer.getEmail());
            array.add(volunteer.getPhone());
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
            mListView.setAdapter(adapter);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;

    private ListView mListView;

    // this method connects the editprofile to the volenteer profile with a buttom

        @Override
        public void onClick(View v)
        {
            switch (v.getId()){
            case R.id.button:
                finish();
          //  startActivity(new Intent(getApplicationContext(),editProfile.class));
break;
            }
        }
}
