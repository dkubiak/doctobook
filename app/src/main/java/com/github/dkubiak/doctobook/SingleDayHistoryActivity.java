package com.github.dkubiak.doctobook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.dkubiak.doctobook.service.GainsCalculator;
import com.github.dkubiak.doctobook.visit.AddVisitActivity;
import com.github.dkubiak.doctobook.visit.UpdateVisitActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SingleDayHistoryActivity extends AppCompatActivity {

    public static final String SELECT_DAY_PARAM = "SELECT_DAY_PARAM";
    private DatabaseHelper db = new DatabaseHelper(this);
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_single_day);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        showSummary();
        addList();
    }

    private void showSummary() {

        GainsCalculator gainsCalculator = new GainsCalculator(db);

        Bundle extras = getIntent().getExtras();
        this.date = (Date) extras.get(SELECT_DAY_PARAM);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        tvDate.setText(df.format(date));

        TextView tvAmountSum = (TextView) findViewById(R.id.tvAmountSum);
        tvAmountSum.setText(String.valueOf(db.amountByDay(date)) + " PLN");

        TextView tvPointSum = (TextView) findViewById(R.id.tvPointSum);
        tvPointSum.setText(String.valueOf(db.pointByDay(date)) + " pkt");

        TextView tvGainSum = (TextView) findViewById(R.id.tvGainSum);
        tvGainSum.setText("Σ " + gainsCalculator.forSingleDayWithRound(date) + " PLN");
    }

    private void addList() {
        ListView listView = (ListView) findViewById(R.id.listViewSingleDay);
        SingleDayHistoryListAdapter adapter = new SingleDayHistoryListAdapter(this, db.getVisitByDay(date));
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(SingleDayHistoryActivity.this, UpdateVisitActivity.class);
                it.putExtra(UpdateVisitActivity.SELECT_VISIT_ID_PARAM, id);
                finish();
                startActivity(it);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
