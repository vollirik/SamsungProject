package com.example.mrorb_000.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private static final int ACTIVITY_CHOOSE_FILE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;

            case R.id.button_graph:
                Intent intent2 = new Intent(this, GraphActivity.class);
                startActivity(intent2);
                break;
            case R.id.button_editor:
                Intent intent3 = new Intent(this, EditorActivity.class);
                startActivity(intent3);
                break;

        }

    }

    public void onClickAnalyze(View view) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            Toast.makeText(this, "Permission checking", Toast.LENGTH_SHORT).show();
            checkPermission();
        } else {
            sendChooseIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendChooseIntent();
                } else {
                    checkPermission();
                }
            }
        }
    }

    private void CreateAlertDialog(ArrayList arrayList) {
        String title = "Analiz zakonchen";

        String message = "Pokazatel Hersta = " + arrayList.get(3);

        String button1String = "save";
        String button2String = "delete";

        AlertDialog.Builder ad = new AlertDialog.Builder(this);

        ad.setTitle(title);

        ad.setMessage(message);

        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.cancel();
            }
        });

        ad.setCancelable(false);
        ad.show();
    }

    private ArrayList analyzeText(String text) {
        ArrayList<Double> arrayListResult = new ArrayList<>();
        char[] charArray = text.toCharArray();
        ArrayList<Integer> wordsLengths = Porazatel.BuildWordsLengths(charArray);
        double middleSquire = Porazatel.middleSquire(wordsLengths);

        arrayListResult.add((double) wordsLengths.size());
        arrayListResult.add(Porazatel.middle(wordsLengths));
        arrayListResult.add(middleSquire);
        arrayListResult.add((Math.log10(Porazatel.Amplitude(wordsLengths)) - Math.log10(middleSquire)) / (Math.log10(wordsLengths.size()) - Math.log10(2)));
        return arrayListResult;
    }


    private String prepareText(String filePath) {
        String text = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            text = sb.toString();

        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
        return text;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        if (requestCode == ACTIVITY_CHOOSE_FILE) {
            Uri uri = data.getData();
            String filePath = getRealPathFromURI(uri);

            String text = prepareText(filePath);
            ArrayList arrayList = analyzeText(text);
            CreateAlertDialog(arrayList);
        }
    }


    private void sendChooseIntent() {
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("file/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);

        } else {
            sendChooseIntent();
        }
    }
}