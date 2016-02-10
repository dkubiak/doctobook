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
import java.util.Date;

public abstract class VisitActivityAbstract extends AppCompatActivity {

    protected EditText editPatientName, editAmount, editPoint, editDate, editExtraCosts;
    protected CheckBox checkProcedureTypeConservative;
    protected CheckBox checkProcedureTypeEndodontics;
    protected CheckBox checkProcedureTypeProsthetics;
    protected EditText editOfficeName, editCommissionPrivate, editCommissionPublic, editNfzConversion;
    protected DatabaseHelper db;


    protected void init() {
        db = new DatabaseHelper(this);
        editPatientName = (EditText) findViewById(R.id.etPatientName);
        checkProcedureTypeConservative = (CheckBox) findViewById(R.id.cbConservative);
        checkProcedureTypeEndodontics = (CheckBox) findViewById(R.id.cbEndodontics);
        checkProcedureTypeProsthetics = (CheckBox) findViewById(R.id.cbProsthetics);
        editDate = (EditText) findViewById(R.id.etDate);
        editAmount = (EditText) findViewById(R.id.etAmount);
        editPoint = (EditText) findViewById(R.id.etPoint);
        editExtraCosts = (EditText) findViewById(R.id.etExtraCosts);

        editNfzConversion = (EditText) findViewById(R.id.etNfzConversion);
        editOfficeName = (EditText) findViewById(R.id.etOfficeName);
        editCommissionPrivate = (EditText) findViewById(R.id.etProvisionPrivate);
        editCommissionPublic = (EditText) findViewById(R.id.etProvisionPublic);
    }

    protected int getPoint() {
        String value = editPoint.getText().toString();
        return value.length() == 0 ? 0 : Integer.valueOf(value);
    }

    protected BigDecimal getAmount() {
        String value = editAmount.getText().toString();
        return value.length() == 0 ? BigDecimal.ZERO : new BigDecimal(value);
    }

    protected Visit.ProcedureType getProcedureType() {
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

    protected void addToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }
}
