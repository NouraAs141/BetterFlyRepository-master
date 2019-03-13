package com.example.betterfly;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class volunteerInfoAdaptor extends ArrayAdapter<Volunteer> {
    Activity context;
    private List<Volunteer> volunteersList;

    public volunteerInfoAdaptor( Activity context,List<Volunteer> volunteersList){
        super(context,R.layout.volunteerslist,volunteersList);
        this.context=context;
        this.volunteersList=volunteersList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listView=inflater.inflate(R.layout.volunteerslist, null, true);


        final TextView vName= listView.findViewById(R.id.RName);
        final TextView vdate= listView.findViewById(R.id.RDate);
        final TextView vemail= listView.findViewById(R.id.remail);

        final Volunteer volunteer1= volunteersList.get(position);
        DateFormat format = new SimpleDateFormat("d/MM/yyyy");
        Date date=volunteer1.getDob();
        String DoB=format.format(date);
        vName.setText(volunteer1.getName());
        vdate.setText(DoB);
        vemail.setText(volunteer1.getEmail());


        return listView;


    }
}
