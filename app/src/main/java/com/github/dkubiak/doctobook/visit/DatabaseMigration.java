package com.github.dkubiak.doctobook.visit;


import android.os.Environment;

import com.github.dkubiak.doctobook.DatabaseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class DatabaseMigration {

    public File exportDB(File currentDB) throws IOException {

        File sd = Environment.getExternalStorageDirectory();

        String backupDBPath = DatabaseHelper.DATABASE_NAME;
        File backupDB = new File(sd, backupDBPath);
        copyDB(currentDB, backupDB);

        return backupDB;
    }


    public void importDB(File currentDB) throws IOException {

        File sd = Environment.getExternalStorageDirectory();
        String backupDBPath = DatabaseHelper.DATABASE_NAME;
        File backupDB = new File(sd, backupDBPath);

        copyDB(backupDB, currentDB);
    }


    private void copyDB(File srcDB, File destDB) throws IOException {
        FileChannel src = new FileInputStream(srcDB).getChannel();
        FileChannel dst = new FileOutputStream(destDB).getChannel();
        dst.transferFrom(src, 0, src.size());
        src.close();
        dst.close();
    }
}
