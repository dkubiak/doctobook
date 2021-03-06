package com.github.dkubiak.doctobook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dkubiak.doctobook.model.Office;
import com.github.dkubiak.doctobook.office.AddOfficeActivity;
import com.github.dkubiak.doctobook.office.UpdateOfficeActivity;
import com.github.dkubiak.doctobook.service.GainsCalculator;
import com.github.dkubiak.doctobook.visit.UpdateVisitActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SingleDayHistoryActivity extends AppCompatActivity {

    public static final String SELECT_DAY_PARAM = "SELECT_DAY_PARAM";
    private DatabaseHelper db = new DatabaseHelper(this);
    private Date date;
    private Office activeOffice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_single_day);
        activeOffice = ((GlobalData) this.getApplicationContext()).getActiveOffice();
        showActiveOffice();
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
        tvAmountSum.setText(String.valueOf(db.amountByOfficeAndDay(activeOffice, date)) + " zł");

        TextView tvPointSum = (TextView) findViewById(R.id.tvPointSum);
        tvPointSum.setText(String.valueOf(db.pointByOfficeAndDay(activeOffice, date)) + " pkt");

        TextView tvGainSum = (TextView) findViewById(R.id.tvGainSum);
        tvGainSum.setText("Σ " + gainsCalculator.forMeByDayWithRound(activeOffice, date) + " zł");
    }

    private void addList() {
        ListView listView = (ListView) findViewById(R.id.listViewSingleDay);
        final SingleDayHistoryListAdapter adapter = new SingleDayHistoryListAdapter(
                this, db.getVisitByOfficeAndDay(activeOffice, date));
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final long visitId = id;
                AlertDialog.Builder builder = new AlertDialog.Builder(SingleDayHistoryActivity.this);

                builder.setTitle(getString(R.string.RemoveVisit));
                builder.setMessage(getString(R.string.AreYouSure));

                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        boolean result = db.deleteVisitById(visitId);
                        if (result) {
                            adapter.removeVisitItem(position);
                            adapter.notifyDataSetChanged();
                            showSummary();
                            Toast.makeText(SingleDayHistoryActivity.this, R.string.alertInfoVisitDeleted, Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent it = new Intent(this, UpdateOfficeActivity.class);
            startActivity(it);
            return true;
        } else if (id == R.id.action_summary) {
            Intent it = new Intent(this, SummaryActivity.class);
            startActivity(it);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showActiveOffice() {
        if (activeOffice != null) {
            setTitle(getString(R.string.app_name) + " - " + activeOffice.getName());
        }
    }
}
