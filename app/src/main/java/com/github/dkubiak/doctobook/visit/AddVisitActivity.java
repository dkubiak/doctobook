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
import com.github.dkubiak.doctobook.model.Office;
import com.github.dkubiak.doctobook.model.Visit;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddVisitActivity extends AppCompatActivity {

    private EditText editPatientName, editAmount, editPoint, editDate;
    private CheckBox checkProcedureTypeConservative;
    private CheckBox checkProcedureTypeEndodontics;
    private CheckBox checkProcedureTypeProsthetics;
    private Button buttonAddVisit;
    private EditText editOfficeName, editCommissionPrivate, editCommissionPublic, editNfzConversion;
    private DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_visit);
        init();
        addToolbar();

        Office activeOffice = ((GlobalData) this.getApplicationContext()).getActiveOffice();
        if (activeOffice != null) {
            editNfzConversion = (EditText) findViewById(R.id.etNfzConversion);
            editOfficeName = (EditText) findViewById(R.id.etOfficeName);
            editCommissionPrivate = (EditText) findViewById(R.id.etProvisionPrivate);
            editCommissionPublic = (EditText) findViewById(R.id.etProvisionPublic);

            editOfficeName.setText(activeOffice.getName());
            editNfzConversion.setText(String.valueOf(activeOffice.getNfzConversion()));
            editCommissionPrivate.setText(String.valueOf(activeOffice.getCommissionPrivate()));
            editCommissionPublic.setText(String.valueOf(activeOffice.getCommissionPublic()));
        }

        setDefaultDate();
        saveVisit();
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
        buttonAddVisit = (Button) findViewById(R.id.btAddVisit);
    }


    private void saveVisit() {
        buttonAddVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Date visitDate = DateConverter.toDate(editDate.getText().toString());
                    boolean isInserted = db.addVisit(new Visit.Builder()
                            .setPatientName(editPatientName.getText().toString())
                            .setDate(visitDate)
                            .setAmount(getAmount())
                            .setPoint(getPoint())
                            .setProcedureType(getProcedureType())
                            .createVisit());
                    if (isInserted) {
                        Toast.makeText(AddVisitActivity.this, R.string.alertInfoVisitInserted, Toast.LENGTH_LONG).show();
                        Intent it = new Intent(AddVisitActivity.this, SingleDayHistoryActivity.class);
                        it.putExtra(SingleDayHistoryActivity.SELECT_DAY_PARAM, visitDate);
                        finish();
                        startActivity(it);
                    } else {
                        Toast.makeText(AddVisitActivity.this, R.string.alertWrongVisitNotInserted, Toast.LENGTH_LONG).show();
                    }
                } catch (ParseException e) {
                    Toast.makeText(AddVisitActivity.this, R.string.alertWrongDateFormat, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    int getPoint() {
        String value = editPoint.getText().toString();
        return value.length() == 0 ? 0 : Integer.valueOf(value);
    }

    BigDecimal getAmount() {
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

    private void setDefaultDate() {
        EditText etDate = (EditText) findViewById(R.id.etDate);
        etDate.setText(DateConverter.toString(new Date()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
