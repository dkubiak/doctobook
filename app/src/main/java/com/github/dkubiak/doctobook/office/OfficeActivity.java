package com.github.dkubiak.doctobook.office;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.dkubiak.doctobook.DatabaseHelper;
import com.github.dkubiak.doctobook.GlobalData;
import com.github.dkubiak.doctobook.R;
import com.github.dkubiak.doctobook.model.Office;

import java.math.BigDecimal;

public class OfficeActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private Button buttonAddOffice, buttonUpdateOffice, buttonDeleteOffice;
    private EditText editOfficeName, editCommissionPrivate, editCommissionPublic, editNfzConversion;
    private Office activeOffice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_office);
        init();
        activeOffice = ((GlobalData) this.getApplicationContext()).getActiveOffice();
        updateOffice();
        saveOffice();
        removeOffice();
        addToolbar();
    }

    private void updateOffice() {
        if (activeOffice != null) {
            buttonAddOffice.setVisibility(View.INVISIBLE);
            buttonUpdateOffice.setVisibility(View.VISIBLE);
            buttonDeleteOffice.setVisibility(View.VISIBLE);

            editOfficeName.setText(activeOffice.getName());
            editNfzConversion.setText(String.valueOf(activeOffice.getNfzConversion()));
            editCommissionPrivate.setText(String.valueOf(activeOffice.getCommissionPrivate()));
            editCommissionPublic.setText(String.valueOf(activeOffice.getCommissionPublic()));

            buttonUpdateOffice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Office office = new Office.Builder()
                            .setId(activeOffice.getId())
                            .setName(editOfficeName.getText().toString())
                            .setNfzConversion(editNfzConversion.getText().toString())
                            .setCommissionPrivate(editCommissionPrivate.getText().toString())
                            .setCommissionPublic((editCommissionPublic.getText().toString()))
                            .createOffice();
                    boolean isUpdated = db.updateOffice(office);
                    if (isUpdated) {
                        ((GlobalData) OfficeActivity.this.getApplicationContext()).setActiveOffice(office);
                        Toast.makeText(OfficeActivity.this, R.string.alertInfoOfficeUpdated, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(OfficeActivity.this, R.string.alertWrongOfficeNotUpdated, Toast.LENGTH_LONG).show();
                    }
                    finish();
                }
            });
        } else {
            buttonAddOffice.setVisibility(View.VISIBLE);
            buttonUpdateOffice.setVisibility(View.INVISIBLE);
            buttonDeleteOffice.setVisibility(View.INVISIBLE);
        }
    }

    private void saveOffice() {
        buttonAddOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = db.addOffice(new Office.Builder()
                        .setName(editOfficeName.getText().toString())
                        .setNfzConversion(editNfzConversion.getText().toString())
                        .setCommissionPrivate(editCommissionPrivate.getText().toString())
                        .setCommissionPublic((editCommissionPublic.getText().toString()))
                        .createOffice());
                if (isInserted) {
                    Toast.makeText(OfficeActivity.this, R.string.alertInfoOfficeInserted, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(OfficeActivity.this, R.string.alertWrongOfficeNotInserted, Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });
    }

    private void removeOffice() {
        buttonDeleteOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Office office = new Office.Builder()
                        .setId(activeOffice.getId())
                        .createOffice();
                boolean isUpdated = db.removeOffice(office);
                if (isUpdated) {
                    Toast.makeText(OfficeActivity.this, R.string.alertInfoOfficeUpdated, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(OfficeActivity.this, R.string.alertWrongOfficeNotUpdated, Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });
    }

    private void init() {
        db = new DatabaseHelper(this);
        editNfzConversion = (EditText) findViewById(R.id.etNfzConversion);
        editOfficeName = (EditText) findViewById(R.id.etOfficeName);
        editCommissionPrivate = (EditText) findViewById(R.id.etProvisionPrivate);
        editCommissionPublic = (EditText) findViewById(R.id.etProvisionPublic);
        buttonAddOffice = (Button) findViewById(R.id.btAddOffice);
        buttonUpdateOffice = (Button) findViewById(R.id.btUpdateOffice);
        buttonDeleteOffice = (Button) findViewById(R.id.btDeleteOffice);
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
