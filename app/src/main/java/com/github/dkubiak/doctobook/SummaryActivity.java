package com.github.dkubiak.doctobook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.dkubiak.doctobook.model.Office;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private Spinner spinnerMonthList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        addToolbar();

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            list.add(LocalDate.now().minusMonths(i).toString("MMM/yyyy"));
        }

        spinnerMonthList = (Spinner) findViewById(R.id.sprMonthList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_view, list);
        spinnerMonthList.setAdapter(adapter);
    }

    private void addToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
