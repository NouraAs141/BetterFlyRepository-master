package com.example.betterfly;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class organizationInfoAdaptor extends ArrayAdapter<Organization> {
    Activity context;
    private List<Organization> organizationList;
   // Activity activity;

    public organizationInfoAdaptor( Activity context,List<Organization> organizationList){
        super(context,R.layout.list,organizationList);
        this.context=context;
        this.organizationList=organizationList;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listView=inflater.inflate(R.layout.list, null, true);


       final TextView organizationName= listView.findViewById(R.id.orgName);

        TextView organizationStatus= listView.findViewById(R.id.orgStatus);
        final Organization organization= organizationList.get(position);
        organizationName.setText(organization.name);
            organizationStatus.setText(organization.status.name());


            organizationName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                        String orgName = organizationName.getText().toString();
                        Intent intent = new Intent(context, ApproveOrg.class);
                        intent.putExtra("name" ,orgName );
                        intent.putExtra("email" , organization.email);
                        intent.putExtra("password",organization.password);
                        intent.putExtra("ApprovalId", organization.approvalId);
                        intent .putExtra("organization", organization);


                        context.startActivity(intent);

                }

        });

        return listView;


    }

}
