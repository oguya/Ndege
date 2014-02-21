package com.droid.ndege.db;

import android.content.Context;
import android.util.Log;

import com.droid.ndege.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by james on 21/02/14.
 */
public class DBUtils {

    public Context context;

    public DBUtils(Context context) {
        this.context = context;
    }

    public void copyDB(String dbName, String dbDir) throws IOException {
        String dbPath = dbDir + dbName;
        File db_dir = new File(dbDir);
        File db_file = new File(dbPath);

        Log.e("DBUtils", "Copying " + dbName + " to " + dbPath);

        byte[] buffer = new byte[1024];
        int length;
        db_dir.mkdirs();

        OutputStream dbOutputStream = new FileOutputStream(dbPath);
        InputStream dbInputStream;
        dbInputStream = this.context.getResources().openRawResource(R.raw.ndege);
        while ((length = dbInputStream.read(buffer)) > 0) {
            dbOutputStream.write(buffer, 0, length);
        }

        dbInputStream.close();
        dbOutputStream.flush();
        dbOutputStream.close();
    }
}

