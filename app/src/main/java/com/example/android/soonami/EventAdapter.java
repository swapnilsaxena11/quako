package com.example.android.soonami;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.soonami.Event;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {


    public EventAdapter(@NonNull Context context,@NonNull List<Event> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_main, parent, false);
        }
        Event currentEvent = getItem(position);

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        dateTextView.setText(new SimpleDateFormat("EEE, d MMM yyyy").format((currentEvent.getTime())));

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time);
        timeTextView.setText(new SimpleDateFormat("hh : mm : ss").format(currentEvent.getTime()));

        TextView placeTextView = (TextView) listItemView.findViewById(R.id.place);
        placeTextView.setText(currentEvent.getTitle());

        TextView magText = (TextView) listItemView.findViewById(R.id.mag_text);
        magText.setText(String.valueOf(BigDecimal.valueOf(currentEvent.getmMagnitude()).setScale(1, RoundingMode.HALF_DOWN).doubleValue()));

        switch ((int) Math.floor(currentEvent.getmMagnitude())){
            case 1:
                listItemView.findViewById(R.id.event_detail).setBackgroundResource(R.color.magnitude1);
                break;
            case 2:
                listItemView.findViewById(R.id.event_detail).setBackgroundResource(R.color.magnitude2);
                break;
            case 3:
                listItemView.findViewById(R.id.event_detail).setBackgroundResource(R.color.magnitude3);
                break;
            case 4:
                listItemView.findViewById(R.id.event_detail).setBackgroundResource(R.color.magnitude4);
                break;
            case 5:
                listItemView.findViewById(R.id.event_detail).setBackgroundResource(R.color.magnitude5);
                break;
            case 6:
                listItemView.findViewById(R.id.event_detail).setBackgroundResource(R.color.magnitude6);
                break;
            case 7:
                listItemView.findViewById(R.id.event_detail).setBackgroundResource(R.color.magnitude7);
                break;
            case 8:
                listItemView.findViewById(R.id.event_detail).setBackgroundResource(R.color.magnitude8);
                break;
            case 9:
                listItemView.findViewById(R.id.event_detail).setBackgroundResource(R.color.magnitude9);
                break;
            default:
                listItemView.findViewById(R.id.event_detail).setBackgroundResource(R.color.magnitude10plus);
                break;
        }
        return listItemView;
    }
}