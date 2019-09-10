package com.example.customcalendar;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomCalendarView extends LinearLayout {

    ImageButton mNextButton,mPreviousButton;
    TextView mCurrentDate;

    //DBOpenHelper dpOpenHelper;
    GridView gridLayoutDay;

    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);

    //private static final int MAX_CALENDARS_DAYS = Calendar.getInstance(Locale.ENGLISH).getActualMaximum(Calendar.DAY_OF_MONTH);
    private static final int MAX_CALENDARS_DAYS = 37;
    Context context;

    List<Date> dates = new ArrayList<>();
    List<Events> events = new ArrayList<>();

    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);

    AlertDialog alertDialog;
    GridViewAdapter gridViewAdapter;

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        InitializeLayout();
        SetUpCalender();
        mPreviousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH,-1);
                SetUpCalender();
            }
        });
        mNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH,1);
                SetUpCalender();
            }
        });
    }



    private void SetUpCalender() {
        String currentDate = dateFormat.format(calendar.getTime());
        mCurrentDate.setText(currentDate);
        dates.clear();
        Calendar monthCalender = (Calendar) calendar.clone();
        monthCalender.set(Calendar.DAY_OF_MONTH,1);
        int firstDayofMonth = monthCalender.get(Calendar.DAY_OF_WEEK) - 1;
        monthCalender.add(Calendar.DAY_OF_MONTH, - firstDayofMonth);
        while (dates.size() < MAX_CALENDARS_DAYS){
            dates.add(monthCalender.getTime());
            monthCalender.add(Calendar.DAY_OF_MONTH, 1);

        }

        gridViewAdapter = new GridViewAdapter(context,dates, calendar, events);
        gridLayoutDay.setAdapter(gridViewAdapter);
    }

    private void InitializeLayout() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_calendar,this);
        mNextButton =  view.findViewById(R.id.btn_next);
        mPreviousButton = view.findViewById(R.id.btn_prev);
        mCurrentDate = view.findViewById(R.id.tv_current_date);
        gridLayoutDay = view.findViewById(R.id.gv_days);
    }


}
