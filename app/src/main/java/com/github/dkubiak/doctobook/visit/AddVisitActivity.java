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

public class AddVisitActivity extends VisitActivityAbstract {

    private Button buttonAddVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_visit);
        init();
        addToolbar();
        setDefaultDate();
        saveVisit();
    }

    protected void init() {
        super.init();
        db = new DatabaseHelper(this);
        buttonAddVisit = (Button) findViewById(R.id.btAddVisit);

        Office activeOffice = ((GlobalData) this.getApplicationContext()).getActiveOffice();
        if (activeOffice != null) {
            editOfficeName.setText(activeOffice.getName());
            editNfzConversion.setText(String.valueOf(activeOffice.getNfzConversion()));
            editCommissionPrivate.setText(String.valueOf(activeOffice.getCommissionPrivate()));
            editCommissionPublic.setText(String.valueOf(activeOffice.getCommissionPublic()));
        }
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
                            .setExtraCosts(editExtraCosts.getText().toString())
                            .setOffice(new Office.Builder()
                                    .setCommissionPrivate(editCommissionPrivate.getText().toString())
                                    .setCommissionPublic(editCommissionPublic.getText().toString())
                                    .setNfzConversion(editNfzConversion.getText().toString())
                                    .setName(editOfficeName.getText().toString())
                                    .createOffice())
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

    protected void setDefaultDate() {
        EditText etDate = (EditText) findViewById(R.id.etDate);
        etDate.setText(DateConverter.toString(new Date()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
