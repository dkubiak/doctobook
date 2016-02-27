package com.github.dkubiak.doctobook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.dkubiak.doctobook.model.Office;
import com.github.dkubiak.doctobook.office.AddOfficeActivity;
import com.github.dkubiak.doctobook.office.UpdateOfficeActivity;
import com.github.dkubiak.doctobook.service.GainsCalculator;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SummaryActivity extends AppCompatActivity {

    private Spinner spinnerMonthList;
    private Office activeOffice;
    private Date chooseMonth = new Date();
    private DatabaseHelper db = new DatabaseHelper(this);
    private GainsCalculator gainsCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activeOffice = ((GlobalData) this.getApplicationContext()).getActiveOffice();
        gainsCalculator = new GainsCalculator(db);
        showActiveOffice();
        setContentView(R.layout.summary);
        addToolbar();

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            list.add(LocalDate.now().minusMonths(i).toString("MMM/yyyy"));
        }

        spinnerMonthList = (Spinner) findViewById(R.id.sprMonthList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_view, list);
        spinnerMonthList.setAdapter(adapter);

        spinnerMonthList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM/yyyy");
                try {
                    chooseMonth = simpleDateFormat.parse(item);

                    TextView tvGainMonthSum = (TextView) findViewById(R.id.tvGainMonthSum);
                    tvGainMonthSum.setText(gainsCalculator.forMeByMonthWithRound(activeOffice, chooseMonth) + " zł");

                    TextView tvGainMonthSumAllOffice = (TextView) findViewById(R.id.tvGainMonthSumAllOffice);
                    tvGainMonthSumAllOffice.setText(gainsCalculator.forMeByMonthWithRound(chooseMonth) + " zł");

                    TextView tvGainPointsMonthSum = (TextView) findViewById(R.id.tvGainPointsMonthSum);
                    tvGainPointsMonthSum.setText(gainsCalculator.pointsForOfficeByMonth(activeOffice, chooseMonth) + " pkt");

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void showActiveOffice() {
        if (activeOffice != null) {
            setTitle(getString(R.string.app_name) + " - " + activeOffice.getName());
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent it = new Intent(this, UpdateOfficeActivity.class);
            startActivity(it);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
