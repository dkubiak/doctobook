package com.github.dkubiak.doctobook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dawid.kubiak on 08/01/16.
 */
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

        Bundle extras = getIntent().getExtras();
        this.date = (Date) extras.get(SELECT_DAY_PARAM);

        addList();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        tvDate.setText(df.format(date));

        TextView tvAmountSum = (TextView) findViewById(R.id.tvAmountSum);
        tvAmountSum.setText(String.valueOf(db.amountByDay(date))+" zł");

        TextView tvPointSum = (TextView) findViewById(R.id.tvPointSum);
        tvPointSum.setText(String.valueOf(db.pointByDay(date))+ " pkt");
    }

    private void addList() {
        ListView listView = (ListView) findViewById(R.id.listViewSingleDay);
        SingleDayHistoryListAdapter adapter = new SingleDayHistoryListAdapter(this, db.getVisitByDay(date));
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
