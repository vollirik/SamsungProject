package com.example.mrorb_000.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Valentun on 23.05.2017.
 * <p>
 * Это активити для обработки permissions на 6+ ведре.
 * Сюда пихай все методы типа check<Name>Permission().
 * Или для работы с бд/файлами.
 * Они будут достпуны во всех осталных активити и не придётся захламлять их.
 * Нужно только чтобы другие активити были унаследованы от этой.
 */

public abstract class BasicActivity extends AppCompatActivity {

    protected final int READ_STORAGE_CODE = 1;
    protected final int WRITE_STORAGE_CODE = 2;

    // ---------- PERMISSIONS ----------

    protected boolean checkReadPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_CODE);
            return false;

        } else {
            return true;
        }
    }

    protected boolean checkWritePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_CODE);
            return false;

        } else {
            return true;
        }
    }

    protected boolean isGranted(int grantResults[]) {
        return grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    protected boolean isPermissionRequestNeeded() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.M;
    }

    // ---------- FILE WORKING ----------

    protected String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    protected void writeToFile(String filepath, String text) {
        try {
            FileOutputStream stream = new FileOutputStream(filepath);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
            writer.write(text);

            writer.close();
            stream.close();
        } catch (IOException e){
            Toast.makeText(getApplicationContext(),
                    "Exception: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    // ---------- ALERTS ----------

    protected void showAnalyzeResultsDialog(ArrayList arrayList) {
        String title = "Analyze completed";

        String message = "Herst's parameter: " + arrayList.get(3);

        String save = "Save";
        String cancel = "Cancel";

        AlertDialog.Builder ad = new AlertDialog.Builder(this);

        ad.setTitle(title);

        ad.setMessage(message);

        ad.setPositiveButton(save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
        ad.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.cancel();
            }
        });

        ad.setCancelable(false);
        ad.show();
    }
}
