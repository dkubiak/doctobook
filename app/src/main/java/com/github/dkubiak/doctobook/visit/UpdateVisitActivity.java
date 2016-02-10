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

public class UpdateVisitActivity extends VisitActivityAbstract {

    public static final String SELECT_VISIT_ID_PARAM = "SELECT_VISIT_ID_PARAM";
    private Button buttonUpdateVisit;
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

    protected void init() {
        super.init();
        db = new DatabaseHelper(this);
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
            editExtraCosts.setText(String.valueOf(visit.getExtraCosts()));
            checkProcedureTypeConservative.setChecked(visit.getProcedureType().isConservative());
            checkProcedureTypeEndodontics.setChecked(visit.getProcedureType().isEndodontics());
            checkProcedureTypeProsthetics.setChecked(visit.getProcedureType().isProsthetics());

            editCommissionPrivate.setText(String.valueOf(visit.getOffice().getCommissionPrivate()));

            editOfficeName.setText(String.valueOf(visit.getOffice().getName()));
            editNfzConversion.setText(String.valueOf(visit.getOffice().getNfzConversion()));
            editCommissionPublic.setText(visit.getOffice().getCommissionPublicAsString());
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
                            .setExtraCosts(editExtraCosts.getText().toString())
                            .setOffice(new Office.Builder()
                                    .setCommissionPrivate(editCommissionPrivate.getText().toString())
                                    .setCommissionPublic(editCommissionPublic.getText().toString())
                                    .setNfzConversion(editNfzConversion.getText().toString())
                                    .setName(editOfficeName.getText().toString())
                                    .createOffice())
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
