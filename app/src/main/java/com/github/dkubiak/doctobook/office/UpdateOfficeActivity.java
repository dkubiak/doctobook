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

public class UpdateOfficeActivity extends AbstractOfficeActivity {

    private Button buttonUpdateOffice, buttonDeleteOffice;
    private Office activeOffice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_office);
        init();
        activeOffice = ((GlobalData) this.getApplicationContext()).getActiveOffice();
        updateOffice();
        removeOffice();
        addToolbar();
    }

    private void updateOffice() {
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
                    db.updateVisitByNewOffice(activeOffice, office);
                    Toast.makeText(UpdateOfficeActivity.this, R.string.alertInfoOfficeUpdated, Toast.LENGTH_LONG).show();
                    ((GlobalData) UpdateOfficeActivity.this.getApplicationContext()).setActiveOffice(office);
                } else {
                    Toast.makeText(UpdateOfficeActivity.this, R.string.alertWrongOfficeNotUpdated, Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });
    }

    private void removeOffice() {
        buttonDeleteOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Office office = new Office.Builder()
                        .setId(activeOffice.getId())
                        .createOffice();

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateOfficeActivity.this);

                builder.setTitle(getString(R.string.RemoveVisit));
                builder.setMessage(getString(R.string.AreYouSure));

                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        boolean isRemoved = db.removeOffice(office);
                        finish();
                        if (isRemoved) {
                            Toast.makeText(UpdateOfficeActivity.this, R.string.alertInfoOfficeDeleted, Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    protected void init() {
        super.init();
        db = new DatabaseHelper(this);
        buttonUpdateOffice = (Button) findViewById(R.id.btUpdateOffice);
        buttonDeleteOffice = (Button) findViewById(R.id.btDeleteOffice);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
