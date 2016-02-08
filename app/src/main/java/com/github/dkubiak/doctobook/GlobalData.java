package com.github.dkubiak.doctobook;

import android.app.Application;

import com.github.dkubiak.doctobook.model.Office;


public class GlobalData extends Application {
    private Office activeOffice;

    public Office getActiveOffice() {
        return activeOffice;
    }

    public void setActiveOffice(Office activeOffice) {
        this.activeOffice = activeOffice;
    }
}
