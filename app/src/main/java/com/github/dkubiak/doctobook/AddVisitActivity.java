package com.github.dkubiak.doctobook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.github.dkubiak.doctobook.model.Visit;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.github.dkubiak.doctobook.model.Visit.*;

/**
 * Created by dawid.kubiak on 08/01/16.
 */
public class AddVisitActivity extends AppCompatActivity {

    private EditText editPatientName, editAmount, editPoint, editDate;
    private CheckBox checkProcedureTypeConservative;
    private CheckBox checkProcedureTypeEndodontics;
    private CheckBox checkProcedureTypeProsthetics;
    private Button buttonAddVisit;
    private DatabaseHelper db;

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_visit);

        db = new DatabaseHelper(this);
        editPatientName = (EditText) findViewById(R.id.etPatientName);
        checkProcedureTypeConservative = (CheckBox) findViewById(R.id.cbConservative);
        checkProcedureTypeEndodontics = (CheckBox) findViewById(R.id.cbEndodontics);
        checkProcedureTypeProsthetics = (CheckBox) findViewById(R.id.cbProsthetics);
        editDate = (EditText) findViewById(R.id.etDate);
        editAmount = (EditText) findViewById(R.id.etAmount);
        editPoint = (EditText) findViewById(R.id.etPoint);
        buttonAddVisit = (Button) findViewById(R.id.btAddVisit);

        addToolbar();
        setDefaultDate();
        saveVisit();
    }

    private void saveVisit() {
        buttonAddVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean isInserted = db.addVisit(new Visit.Builder()
                            .setPatientName(editPatientName.getText().toString())
                            .setDate(DATE_FORMATTER.parse(editDate.getText().toString()))
                            .setAmount(getAmount())
                            .setPoint(getPoint())
                            .setProcedureType(getProcedureType())
                            .createVisit());
                    if (isInserted) {
                        Toast.makeText(AddVisitActivity.this, R.string.alertInfoVisitInserted, Toast.LENGTH_LONG).show();
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

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String dateNow = df.format(new Date());
        etDate.setText(dateNow);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
