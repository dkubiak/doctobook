package com.github.dkubiak.doctobook.office;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.dkubiak.doctobook.DatabaseHelper;
import com.github.dkubiak.doctobook.R;
import com.github.dkubiak.doctobook.model.Office;

public class AddOfficeActivity extends AbstractOfficeActivity {

    protected Button buttonAddOffice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_office);
        init();
        saveOffice();
        addToolbar();
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
                    Toast.makeText(AddOfficeActivity.this, R.string.alertInfoOfficeInserted, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddOfficeActivity.this, R.string.alertWrongOfficeNotInserted, Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });
    }

    protected void init() {
        db = new DatabaseHelper(this);
        buttonAddOffice = (Button) findViewById(R.id.btAddOffice);
        super.init();
    }

}
