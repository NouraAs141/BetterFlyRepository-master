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

import java.io.Serializable;
import java.util.List;

public class eventinfoAdaptorv extends ArrayAdapter<event>{
    Activity context;
    private List<event> eventList;

    public eventinfoAdaptorv( Activity context,List<event> eventList){
        super(context,R.layout.eventslist,eventList);
        this.context=context;
        this.eventList=eventList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listView=inflater.inflate(R.layout.eventslist, null, true);


        final TextView eventName= listView.findViewById(R.id.eventName);
        final event event1= eventList.get(position);
        eventName.setText(event1.getName());

        eventName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String eveName = eventName.getText().toString();
                Intent intent = new Intent(context, RequestToVounteer.class);
                intent.putExtra("name" ,eveName );
                intent.putExtra("description" , event1.getDescreption());
                intent.putExtra("location", event1.getLocation());
                intent .putExtra("date", event1.getDate());
                intent .putExtra("Credit Hours", event1.getcHours());
                intent .putExtra("Number of Volunteers", event1.nov);
                intent .putExtra( "event",  event1);
                intent .putExtra( "id",  event1.org);


                context.startActivity(intent);

            }

        });

        return listView;


    }
}
