package com.github.dkubiak.doctobook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.github.dkubiak.doctobook.converter.DateConverter;
import com.github.dkubiak.doctobook.model.Visit;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dawid.kubiak on 10/01/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Doctobook.db";
    public static final String TABLE_NAME_VISIT = "visit_table";
    public static final String VISIT_COL_ID = "ID";
    public static final String VISIT_COL_PATIENT_NAME = "PATIENT_NAME";
    public static final String VISIT_COL_DATE = "DATE";
    public static final String VISIT_COL_PROCEDURE_TYPE_PROSTHETICS = "PROCEDURE_TYPE_PROSTHETICS";
    public static final String VISIT_COL_PROCEDURE_TYPE_ENDODONTICS = "PROCEDURE_TYPE_ENDODONTICS";
    public static final String VISIT_COL_PROCEDURE_TYPE_CONSERVATIVE = "PROCEDURE_TYPE_CONSERVATIVE";
    public static final String VISIT_COL_AMOUNT = "AMOUNT";
    public static final String VISIT_COL_POINT = "POINT";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME_VISIT + " " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, PATIENT_NAME TEXT, DATE TEXT," +
                " PROCEDURE_TYPE_PROSTHETICS VARCHAR(1), PROCEDURE_TYPE_ENDODONTICS VARCHAR(1)," +
                "PROCEDURE_TYPE_CONSERVATIVE VARCHAR(1), AMOUNT DECIMAL(10,2), POINT INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_VISIT);
        onCreate(db);
    }

    public boolean addVisit(Visit visit) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_NAME_VISIT, null, getContentValues(visit));
        if (result == -1) {
            return false;
        }
        return true;
    }

    public boolean updateVisit(Visit visit) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.update(TABLE_NAME_VISIT, getContentValues(visit), VISIT_COL_ID + "=" + visit.getId(), null);
        if (result == -1) {
            return false;
        }
        return true;
    }

    @NonNull
    private ContentValues getContentValues(Visit visit) {
        ContentValues cv = new ContentValues();
        cv.put(VISIT_COL_PATIENT_NAME, visit.getPatientName());
        cv.put(VISIT_COL_PROCEDURE_TYPE_CONSERVATIVE, booleanToString(visit.getProcedureType().isConservative()));
        cv.put(VISIT_COL_PROCEDURE_TYPE_ENDODONTICS, booleanToString(visit.getProcedureType().isEndodontics()));
        cv.put(VISIT_COL_PROCEDURE_TYPE_PROSTHETICS, booleanToString(visit.getProcedureType().isProsthetics()));
        cv.put(VISIT_COL_AMOUNT, visit.getAmount().doubleValue());
        cv.put(VISIT_COL_POINT, visit.getPoint());
        cv.put(VISIT_COL_DATE, DateConverter.toStringDB(visit.getDate()));
        return cv;
    }

    public List<Visit> getVisitByDay(Date date) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Visit> result = new ArrayList();
        Cursor row = db.rawQuery("select * from " + TABLE_NAME_VISIT + " where date=date('" + DateConverter.toStringDB(date) + "') order by " + VISIT_COL_ID + " desc", null);

        while (row.moveToNext()) {
            result.add(buildSingleVisit(row));
        }
        return result;
    }

    public Visit getVisitById(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor row = db.rawQuery("select * from " + TABLE_NAME_VISIT + " where id=" + id, null);

        row.moveToFirst();
        return buildSingleVisit(row);
    }

    private Visit buildSingleVisit(Cursor row) {
        try {
            Visit.ProcedureType.Builder procedureTypeBuilder = new Visit.ProcedureType.Builder();
            if (stringToBoolean(row.getString(row.getColumnIndex(VISIT_COL_PROCEDURE_TYPE_CONSERVATIVE)))) {
                procedureTypeBuilder.isConservative();
            }
            if (stringToBoolean(row.getString(row.getColumnIndex(VISIT_COL_PROCEDURE_TYPE_ENDODONTICS)))) {
                procedureTypeBuilder.isEndodontics();
            }
            if (stringToBoolean(row.getString(row.getColumnIndex(VISIT_COL_PROCEDURE_TYPE_PROSTHETICS)))) {
                procedureTypeBuilder.isProsthetics();
            }

            return new Visit.Builder()
                    .setId(row.getLong(row.getColumnIndex(VISIT_COL_ID)))
                    .setAmount(row.getString(row.getColumnIndex(VISIT_COL_AMOUNT)))
                    .setPatientName(row.getString(row.getColumnIndex(VISIT_COL_PATIENT_NAME)))
                    .setPoint(row.getInt(row.getColumnIndex(VISIT_COL_POINT)))
                    .setDate(DateConverter.toDateFromDB(row.getString(row.getColumnIndex(VISIT_COL_DATE))))
                    .setProcedureType(procedureTypeBuilder.createProcedureType())
                    .createVisit();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BigDecimal amountByDay(Date date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("select sum(AMOUNT) from " + TABLE_NAME_VISIT + " where date=date('" + DateConverter.toStringDB(date) + "')", null);
        if (cur.moveToFirst()) {
            String amount = cur.getString(0);
            return amount == null ? BigDecimal.ZERO : new BigDecimal(amount);
        }
        return BigDecimal.ZERO;
    }

    public int pointByDay(Date date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("select sum(POINT) from " + TABLE_NAME_VISIT + " where date=date('" + DateConverter.toStringDB(date) + "')", null);
        if (cur.moveToFirst()) {
            return (cur.getInt(0));
        }
        return 0;
    }

    private String booleanToString(boolean value) {
        return value ? "T" : "F";
    }

    private boolean stringToBoolean(String value) {
        return "T".equals(value);
    }
}
