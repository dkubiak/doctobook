package com.github.dkubiak.doctobook.visit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.github.dkubiak.doctobook.DatabaseHelper;
import com.github.dkubiak.doctobook.GlobalData;
import com.github.dkubiak.doctobook.R;
import com.github.dkubiak.doctobook.SingleDayHistoryActivity;
import com.github.dkubiak.doctobook.converter.DateConverter;
import com.github.dkubiak.doctobook.model.Visit;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateVisitActivity extends AppCompatActivity {

    public static final String SELECT_VISIT_ID_PARAM = "SELECT_VISIT_ID_PARAM";

    private EditText editPatientName, editAmount, editPoint, editDate;
    private CheckBox checkProcedureTypeConservative;
    private CheckBox checkProcedureTypeEndodontics;
    private CheckBox checkProcedureTypeProsthetics;
    private Button buttonUpdateVisit;
    private DatabaseHelper db;
    private long visitId;

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_visit);
        init();

        addToolbar();
        showExistsVisit();
        updateVisit();
    }

    private void init() {
        db = new DatabaseHelper(this);
        editPatientName = (EditText) findViewById(R.id.etPatientName);
        checkProcedureTypeConservative = (CheckBox) findViewById(R.id.cbConservative);
        checkProcedureTypeEndodontics = (CheckBox) findViewById(R.id.cbEndodontics);
        checkProcedureTypeProsthetics = (CheckBox) findViewById(R.id.cbProsthetics);
        editDate = (EditText) findViewById(R.id.etDate);
        editAmount = (EditText) findViewById(R.id.etAmount);
        editPoint = (EditText) findViewById(R.id.etPoint);
        buttonUpdateVisit = (Button) findViewById(R.id.btUpdateVisit);
    }

    private void showExistsVisit() {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(SELECT_VISIT_ID_PARAM)) {

            this.visitId = (Long) extras.get(SELECT_VISIT_ID_PARAM);
            Visit visit = db.getVisitById(visitId);
            editAmount.setText(String.valueOf(visit.getAmount()));
            editPatientName.setText(String.valueOf(visit.getPatientName()));
            editPoint.setText(String.valueOf(visit.getPoint()));
            editDate.setText(DateConverter.toString(visit.getDate()));
            checkProcedureTypeConservative.setChecked(visit.getProcedureType().isConservative());
            checkProcedureTypeEndodontics.setChecked(visit.getProcedureType().isEndodontics());
            checkProcedureTypeProsthetics.setChecked(visit.getProcedureType().isProsthetics());
        }
    }

    private void updateVisit() {
        buttonUpdateVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean isUpdated = db.updateVisit(new Visit.Builder()
                            .setId(UpdateVisitActivity.this.visitId)
                            .setPatientName(editPatientName.getText().toString())
                            .setDate(DATE_FORMATTER.parse(editDate.getText().toString()))
                            .setAmount(getAmount())
                            .setPoint(getPoint())
                            .setProcedureType(getProcedureType())
                            .createVisit());
                    if (isUpdated) {
                        Toast.makeText(UpdateVisitActivity.this, R.string.alertInfoVisitUpdated, Toast.LENGTH_LONG).show();
                        Intent it = new Intent(UpdateVisitActivity.this, SingleDayHistoryActivity.class);
                        it.putExtra(SingleDayHistoryActivity.SELECT_DAY_PARAM, DATE_FORMATTER.parse(editDate.getText().toString()));
                        finish();
                        startActivity(it);
                    } else {
                        Toast.makeText(UpdateVisitActivity.this, R.string.alertWrongVisitNotInserted, Toast.LENGTH_LONG).show();
                    }
                } catch (ParseException e) {
                    Toast.makeText(UpdateVisitActivity.this, R.string.alertWrongDateFormat, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private int getPoint() {
        String value = editPoint.getText().toString();
        return value.length() == 0 ? 0 : Integer.valueOf(value);
    }

    private BigDecimal getAmount() {
        String value = editAmount.getText().toString();
        return value.length() == 0 ? BigDecimal.ZERO : new BigDecimal(value);
    }

    Visit.ProcedureType getProcedureType() {
        Visit.ProcedureType.Builder builder = new Visit.ProcedureType.Builder();
        if (checkProcedureTypeConservative.isChecked()) {
            builder.isConservative();
        }
        if (checkProcedureTypeEndodontics.isChecked()) {
            builder.isEndodontics();
        }
        if (checkProcedureTypeProsthetics.isChecked()) {
            builder.isProsthetics();
        }
        return builder.createProcedureType();
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
