package com.github.dkubiak.doctobook.office;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

public abstract class AbstractOfficeActivity extends AppCompatActivity {

    protected DatabaseHelper db;
    protected EditText editOfficeName, editCommissionPrivate, editCommissionPublic, editNfzConversion;

    protected void init() {
        editNfzConversion = (EditText) findViewById(R.id.etNfzConversion);
        editOfficeName = (EditText) findViewById(R.id.etOfficeName);
        editCommissionPrivate = (EditText) findViewById(R.id.etProvisionPrivate);
        editCommissionPublic = (EditText) findViewById(R.id.etProvisionPublic);
    }

    protected void addToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
