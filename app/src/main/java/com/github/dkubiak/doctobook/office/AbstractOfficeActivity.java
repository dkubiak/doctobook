package com.github.dkubiak.doctobook.office;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.dkubiak.doctobook.DatabaseHelper;
import com.github.dkubiak.doctobook.R;
import com.github.dkubiak.doctobook.visit.DatabaseMigration;

import java.io.IOException;

public abstract class AbstractOfficeActivity extends AppCompatActivity {

    // Storage Permissions
    private static String TAG = "Permission";
    private static final int REQUEST_WRITE_STORAGE = 112;

    protected DatabaseHelper db;
    protected EditText editOfficeName, editCommissionPrivate, editCommissionPublic, editNfzConversion;
    protected Button buttonExportDatabase, buttonImportDatabase;

    protected DatabaseMigration databaseMigration;

    protected void init() {
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Permission to access the SD-CARD is required for this app to export/import DB.")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Clicked");
                        makeRequest();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                makeRequest();
            }
        }


        editNfzConversion = (EditText) findViewById(R.id.etNfzConversion);
        editOfficeName = (EditText) findViewById(R.id.etOfficeName);
        editCommissionPrivate = (EditText) findViewById(R.id.etProvisionPrivate);
        editCommissionPublic = (EditText) findViewById(R.id.etProvisionPublic);

        buttonExportDatabase = (Button) findViewById(R.id.btExportDatabase);
        buttonImportDatabase = (Button) findViewById(R.id.btImportDatabase);

        databaseMigration = new DatabaseMigration();

        buttonExportDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    databaseMigration.exportDB(getDatabasePath(DatabaseHelper.DATABASE_NAME));
                    Toast.makeText(AbstractOfficeActivity.this, R.string.alertInfoDatabaseWasExported, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonImportDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    databaseMigration.importDB(getDatabasePath(DatabaseHelper.DATABASE_NAME));
                    Toast.makeText(AbstractOfficeActivity.this, R.string.alertInfoDatabaseWasImported, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(AbstractOfficeActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user");
                } else {
                    Log.i(TAG, "Permission has been granted by user");
                }
                return;
            }
        }
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_STORAGE);
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
