package com.github.dkubiak.doctobook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.dkubiak.doctobook.model.Office;
import com.github.dkubiak.doctobook.office.OfficeActivity;
import com.github.dkubiak.doctobook.visit.AddVisitActivity;
import com.imanoweb.calendarview.CalendarListener;
import com.imanoweb.calendarview.CustomCalendarView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerOfficeList;
    private DatabaseHelper db = new DatabaseHelper(this);
    private String addNewOfficeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        addNewOfficeItem = getString(R.string.addNewOffice);
        toolbar();
        showOfficeList();
        calendar();
        fab();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        showOfficeList();
    }

    private void showOfficeList() {
        spinnerOfficeList = (Spinner) findViewById(R.id.sprOfficeList);
        ArrayList<String> list = new ArrayList<String>();
        List<Office> allOffices = db.getAllOffices();
        if (allOffices.isEmpty()) {
            ((GlobalData) MainActivity.this.getApplicationContext()).setActiveOffice(null);
            Intent it = new Intent(MainActivity.this, OfficeActivity.class);
            startActivity(it);
            return;
        }
        for (Office office : allOffices) {
            list.add(office.getName());
        }
        list.add(addNewOfficeItem);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_view, list);
        spinnerOfficeList.setAdapter(adapter);

        Office activeOffice = ((GlobalData) MainActivity.this.getApplicationContext()).getActiveOffice();
        if (activeOffice != null) {
            for (int i = 0; i < spinnerOfficeList.getCount(); i++) {
                if (spinnerOfficeList.getItemAtPosition(i).equals(activeOffice.getName())) {
                    spinnerOfficeList.setSelection(i);
                    break;
                }
            }
        } else {
            Office officeSelected = db.getOfficeByName((String) spinnerOfficeList.getSelectedItem());
            ((GlobalData) MainActivity.this.getApplicationContext()).setActiveOffice(officeSelected);
        }
        spinnerOfficeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (addNewOfficeItem.equals(item)) {
                    ((GlobalData) MainActivity.this.getApplicationContext()).setActiveOffice(null);
                    Intent it = new Intent(MainActivity.this, OfficeActivity.class);
                    startActivity(it);
                } else {
                    Office officeSelected = db.getOfficeByName(item);
                    ((GlobalData) MainActivity.this.getApplicationContext()).setActiveOffice(officeSelected);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, AddVisitActivity.class);
                startActivity(it);
            }
        });
    }

    private void calendar() {
        //Initialize CustomCalendarView from layout
        CustomCalendarView calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                Intent it = new Intent(MainActivity.this, SingleDayHistoryActivity.class);
                it.putExtra(SingleDayHistoryActivity.SELECT_DAY_PARAM, date);
                startActivity(it);
            }

            @Override
            public void onMonthChanged(Date date) {
            }
        });
    }

    private void toolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent it = new Intent(MainActivity.this, OfficeActivity.class);
            startActivity(it);
            return true;
        } else if (id == R.id.action_summary) {
            Intent it = new Intent(MainActivity.this, SummaryActivity.class);
            startActivity(it);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
