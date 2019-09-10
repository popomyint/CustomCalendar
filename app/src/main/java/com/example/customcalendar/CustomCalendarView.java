package com.example.customcalendar;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CustomCalendarView extends LinearLayout {

    ImageButton mNextButton,mPreviousButton;
    TextView mCurrentDate;

    DBHelper dpOpenHelper;
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
    SimpleDateFormat singleMonth = new SimpleDateFormat("M", Locale.ENGLISH);


    AlertDialog alertDialog;
    GridViewAdapter gridViewAdapter;

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(final Context context, @Nullable AttributeSet attrs) {
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

        gridLayoutDay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getVisibility() == View.GONE){
                    Toast.makeText(getContext(), "Gone..",Toast.LENGTH_LONG).show();
                } else {
                    int cMonth = calendar.get(Calendar.MONTH) + 1;
                    int sM = Integer.parseInt(singleMonth.format(dates.get(i)));
                    if(cMonth == sM){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(true);
                        final View addView = LayoutInflater.from(adapterView.getContext()).inflate(R.layout.add_event,null);
                        final EditText typeEvent = addView.findViewById(R.id.ed_type_event);
                        ImageButton btnSetTime = addView.findViewById(R.id.btn_set_time);
                        final EditText description = addView.findViewById(R.id.description);
                        Button buttonAddEvent = addView.findViewById(R.id.btn_add_event);

                        btnSetTime.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                                int minutes = calendar.get(Calendar.MINUTE);
                                TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                                                Calendar c = Calendar.getInstance();
                                                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                                c.set(Calendar.MINUTE,minute);
                                                c.setTimeZone(TimeZone.getDefault());
                                                SimpleDateFormat hrFormat = new SimpleDateFormat("K:mm a",Locale.ENGLISH);
                                                String event_Time = hrFormat.format(c.getTime());
                                                typeEvent.setText(event_Time);
                                            }
                                        },hours,minutes, false);
                                timePickerDialog.show();
                            }
                        });
                        final String date = dateFormat.format(dates.get(i));
                        final String month = monthFormat.format(dates.get(i));
                        final String year = yearFormat.format(dates.get(i));

                        buttonAddEvent.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                saveEvent(typeEvent.getText().toString(),description.getText().toString(),date,month,year);
                                SetUpCalender();
                                alertDialog.dismiss();
                            }
                        });

                        builder.setView(addView);
                        alertDialog = builder.create();
                        alertDialog.show();
                    }else{
                        Toast.makeText(getContext(), " Not Same " + cMonth + " " + sM, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void saveEvent(String event, String description, String date, String month, String year) {
        dpOpenHelper = new DBHelper(context);
        SQLiteDatabase database = dpOpenHelper.getWritableDatabase();
        dpOpenHelper.SaveEvent(event,description,date,month,year,database);
        dpOpenHelper.close();
        Toast.makeText(getContext(),"Event Saved", Toast.LENGTH_LONG).show();

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
