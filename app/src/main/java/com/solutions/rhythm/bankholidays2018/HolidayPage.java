package com.solutions.rhythm.bankholidays2018;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.Arrays;
import java.util.Calendar;

public class HolidayPage extends AppCompatActivity {

    public CalendarDay date,selectedCalendarDay;
    public TextView eventTextView,holidayTypeTextView;

    MaterialCalendarView calendarView;

    DatabaseReference mDatabase;

    AdView mAdView;
    String event, selectedLocation, holidayType;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_page);

        //advertisement
        MobileAds.initialize(this,"ca-app-pub-9529062142998244~1236529437");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //add items to spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinnerLocation);


        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        eventTextView=(TextView)findViewById(R.id.eventTextView);
        eventTextView.setVisibility(View.INVISIBLE);
        holidayTypeTextView=(TextView)findViewById(R.id.holidayTypeTextView);
        holidayTypeTextView.setVisibility(View.INVISIBLE);

        //progressbar
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        date=CalendarDay.today();

        //setting first calender view with holiday list from firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        MonthChange(date.getMonth());

        //onitemChanged Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLocation=adapterView.getItemAtPosition(i).toString();
                Log.i("Location",selectedLocation);
                MonthChange(date.getMonth());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //setting calendar view on month change
       calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
           @Override
           public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
               MonthChange(date.getMonth());
           }
       });

       //changes on date selection
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedCalendarDay = date;
                Log.i("Selected day", String.valueOf(selectedCalendarDay.getDay()));
                DateSelected();
            }
        });

    }

    //on Month Change
    public void MonthChange(final int thismonth)
    {
        calendarView.removeDecorators();
        Log.i("Month", String.valueOf(thismonth));
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    int month = Integer.parseInt(postSnapshot.child("month").getValue().toString())-1;

                    //getting location & separating
                    String location=postSnapshot.child("location").getValue().toString();
                    String[] separateLocation = location.split(",");

                    boolean islocation = false;
                    if(selectedLocation.equals("All Regional Offices")) {islocation=true;}
                    else {
                        if (Arrays.asList(separateLocation).contains(selectedLocation) || location.equals("All Regional Offices")) {
                            islocation = true;
                        }
                    }

                    if(month==thismonth && islocation)
                    {
                        int day = Integer.parseInt(postSnapshot.child("date").getValue().toString());
                        date=CalendarDay.from(2018,month, day);
                        int colorId = colorSelector(postSnapshot.child("color").getValue().toString());
                        calendarView.addDecorator(new MyCalendarDecorator(getBaseContext(),date,colorId));
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
                eventTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //select color
    public int colorSelector(String colorString)
    {
        int colorId = Color.BLACK;
        switch (colorString)
        {
            case "green":
                    colorId = Color.GREEN;
                    holidayType = "Local Regional Holiday";
                    break;
            case "blue":
                    colorId = Color.BLUE;
                    holidayType = "RBI closed. RTGS Holiday";
                    break;
            case "yellow":
                    colorId=Color.YELLOW;
                    holidayType="Regional Bank's Closing of Accounts";
                    break;
            case "cyan":
                    colorId = Color.CYAN;
                    holidayType="RBI Annual Closing of Accounts";
                    break;
        }
        return colorId;
    }

    //on date selected change
    public void DateSelected()
    {
        event="No Event";
        holidayTypeTextView.setVisibility(View.INVISIBLE);
        final int selectedDay = selectedCalendarDay.getDay();
        final int selectedMonth = selectedCalendarDay.getMonth()+1;
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    int day= Integer.parseInt(postSnapshot.child("date").getValue().toString());
                    int month = Integer.parseInt(postSnapshot.child("month").getValue().toString());
                    if((day==selectedDay)&&(month==selectedMonth))
                    {
                        event = postSnapshot.child("event").getValue().toString();
                        int colorId = colorSelector(postSnapshot.child("color").getValue().toString());
                        holidayTypeTextView.setVisibility(View.VISIBLE);
                        holidayTypeTextView.setText(holidayType);
                    }
                }
                eventTextView.setText(selectedDay+"."+selectedMonth+"."+"2018"+" : "+event);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
