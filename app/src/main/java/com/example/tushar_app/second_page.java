package com.example.tushar_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class second_page extends AppCompatActivity {
    SQLiteDatabase database;
    recycleclass mAdapter;
    EditText datetext,timetext,titletext;
    Button saveBtn, item;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    String timeTonotify ;
    FloatingActionButton floatingActionButton;
    Calendar calendar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);

        dbHelper dbHelper=new dbHelper(this);
        database = dbHelper.getWritableDatabase();
        editdata();
        item = findViewById(R.id.itemClick);
        titletext = findViewById(R.id.titletext);
        datetext=findViewById(R.id.datetext);
        timetext=findViewById(R.id.timetext);
        saveBtn = findViewById(R.id.button);
        floatingActionButton=findViewById(R.id.floatingActionButton);

        for (EditText editText : Arrays.asList(datetext, timetext)) {
        }

        datetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(datetext);
            }
        });

        timetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(timetext);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEntries();

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnhome();
            }
        });
        /*item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues cv =new ContentValues();
                cv.put("title",titletext.getText().toString());
                cv.put("date",datetext.getText().toString());
                cv.put("time",timetext.getText().toString());
                database=dbHelper.getReadableDatabase();
                long recedit =database.update(names.Entries.TABLE_NAME,cv,"id="+ names.Entries._ID,null);
                if (recedit!=-1){
                    Toast.makeText(second_page.this,"update successful",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(second_page.this,"something went wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    private void editdata() {
        if (getIntent().getBundleExtra("editdata")!=null){
            Bundle bundle = getIntent().getBundleExtra("editdata");
            titletext.setText(bundle.getString("title"));
            datetext.setText(bundle.getString("date"));
            timetext.setText(bundle.getString("time"));


        }
    }

    private void saveEntries() {
        if(titletext.getText().toString().trim().length()==0||datetext.getText().toString().trim().length()==0||timetext.getText().toString().trim().length()==0){
            return;
        }
        mAdapter = new recycleclass(getApplicationContext(),getAllItems());

        String title = titletext.getText().toString().trim();
        String date = datetext.getText().toString().trim();
        String time= timetext.getText().toString().trim();
        ContentValues cv = new ContentValues();
        cv.put(names.Entries.COLUMN_TITLE, title);
        cv.put(names.Entries.COLUMN_DATE, date);
        cv.put(names.Entries.COLUMN_TIME, time);
        database.insert(names.Entries.TABLE_NAME,null,cv);
        //clear text
        titletext.getText().clear();
        datetext.getText().clear();
        timetext.getText().clear();
        mAdapter.swapCursor(getAllItems());
        //alarm
        setAlarm(title,time,date);
        //return to home screen
        returnhome();
        Toast.makeText(second_page.this,"Saved",Toast.LENGTH_SHORT).show();
    }

/*    private void setAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this,AlarmReciver.class);
        pendingIntent =PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, Long.parseLong(names.Entries.COLUMN_TIME),pendingIntent);


    */

    private void returnhome() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    private void showDateTimeDialog(final EditText date_time_in) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        timeTonotify = hourOfDay + ":" + minute;
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd HH:mm");
                    }
                };

                new TimePickerDialog(second_page.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(second_page.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void showTimeDialog(final EditText time_in) {
        final Calendar calendar=Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                time_in.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };

        new TimePickerDialog(second_page.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
    }

    private void showDateDialog(final EditText date_in) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd");
                date_in.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };

        new DatePickerDialog(second_page.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private Cursor getAllItems(){
        return database.query(
                names.Entries.TABLE_NAME,
                null,
               null,
                null,
                null,
                null,
                null
       );
    }
/*    private void NotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "ReminderChannel";
            String description = "alarm app channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("ReminderApp",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }*/
    private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmReciver.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Toast.makeText(getApplicationContext(), "Alaram", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentBack);

    }

}