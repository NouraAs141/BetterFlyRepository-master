package com.example.betterfly;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.IOException;


public class ApproveOrg extends AppCompatActivity implements View.OnClickListener {

  //  private DatabaseReference orgRef;
    private TextView textViewName , textViewEmail, textViewApprovalID;

    Organization organization;
    public String orgName;
    public String orgID , password;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_org);

        databaseReference= FirebaseDatabase.getInstance().getReference("Organization");

        findViewById(R.id.backbtn).setOnClickListener(this);
        findViewById(R.id.Request).setOnClickListener(this);
        findViewById(R.id.reject).setOnClickListener(this);
         textViewName = findViewById(R.id.EvName);
         textViewEmail = findViewById(R.id.aprove);
         textViewApprovalID = findViewById(R.id.OrgName);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        organization= (Organization) intent.getSerializableExtra("organization");
         if(bundle!=null){
             orgName = (String) bundle.get("name");
             String orgEmail =(String) bundle.get("email");
             orgID = (String) bundle.get("ApprovalId") ;

             textViewName.setText(orgName);
             textViewEmail.setText(orgEmail);
             textViewApprovalID.setText(orgID);

         }


    }







        @Override
    public void onClick(View view) {


                switch (view.getId()) {
                    case R.id.Request:
                        organization.setStatus(Status.APPROVED);


                        FirebaseDatabase.getInstance().getReference("Organization").child(orgID).setValue(organization);
                        sendEmailaccept();
                        Toast.makeText(ApproveOrg.this, "The organization has been accepted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(this, dataRetrieved.class));


                        break;

                    case R.id.reject:
                        DatabaseReference orgRef = FirebaseDatabase.getInstance().getReference("Organization").child(orgID);
                        orgRef.removeValue();

                       // FirebaseAuth.getInstance().deleteUser(uid);

//                        firebaseUser = FirebaseAuth.getInstance().getCurrentUs
                        AuthCredential authCredential = EmailAuthProvider.getCredential(organization.email, organization.password);

                        firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ApproveOrg.this, "The organization has been deleted successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                     sendEmailReject();


                        finish();
                        startActivity(new Intent(this, dataRetrieved.class));
                        break;
                    case R.id.backbtn:
                        finish();
                        startActivity(new Intent(this, dataRetrieved.class));
                        break;
                }



        }


    protected void sendEmailReject() {



       Log.i("Send email", "");

        String[] TO = {organization.email};
        String[] CC = {"zezoazizah@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setDataAndType(Uri.parse("mailto:"),"text/plain");




        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "About your registration request");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Sorry to inform you that your request has been rejected. If you have any complains please contact us with this email");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ApproveOrg.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

     /*   Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        String[] recipients = new String[]{organization.email, "",};

        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);

        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "About your registration request");

        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Sorry to inform you that your request has been rejected. If you have any complains please contact us with this email");

        emailIntent.setType("text/plain");

        startActivity(Intent.createChooser(emailIntent, "Send mail..."));*/



    }

    protected void sendEmailaccept() {
        Log.i("Send email", "");

        String[] TO = {organization.email};
        String[] CC = {"zezoazizah@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setDataAndType(Uri.parse("mailto:"),"text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "About your registration request");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Congratulations your request have been accepted ");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ApproveOrg.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    }

