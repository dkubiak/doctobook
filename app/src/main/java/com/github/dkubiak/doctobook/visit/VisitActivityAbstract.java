package com.github.dkubiak.doctobook.visit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.github.dkubiak.doctobook.DatabaseHelper;
import com.github.dkubiak.doctobook.R;
import com.github.dkubiak.doctobook.SummaryActivity;
import com.github.dkubiak.doctobook.model.Visit;
import com.github.dkubiak.doctobook.office.AddOfficeActivity;
import com.github.dkubiak.doctobook.office.UpdateOfficeActivity;

import java.math.BigDecimal;

public abstract class VisitActivityAbstract extends AppCompatActivity {

    protected EditText editPatientName, editAmount, editPoint, editDate, editExtraCosts;
    protected EditText editOfficeName, editCommissionPrivate, editCommissionPublic, editNfzConversion;
    protected DatabaseHelper db;


    protected void init() {
        db = new DatabaseHelper(this);
        editPatientName = (EditText) findViewById(R.id.etPatientName);
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
        return builder.createProcedureType();
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

    protected void addToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }
}
