package com.example.mrorb_000.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import static com.example.mrorb_000.myapplication.Analyzer.analyze;
import static com.example.mrorb_000.myapplication.Utils.readFileAsString;


public class MainActivity extends BasicActivity {
    private static final int ACTIVITY_ANALYZE_FILE = 0;
    private static final int ACTIVITY_EDIT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == READ_STORAGE_CODE) {
            if (isGranted(grantResults)) {
                sendChooseIntent(ACTIVITY_ANALYZE_FILE);
            } else {
                checkReadPermission();
            }
        }
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
        }
    }

    public void onClickEditor(View view) {
        sendChooseIntent(ACTIVITY_EDIT_FILE);
    }


    public void onClickAnalyze(View view) {
        if (isPermissionRequestNeeded()) {
            if (checkReadPermission()) sendChooseIntent(ACTIVITY_ANALYZE_FILE);
        } else {
            sendChooseIntent(ACTIVITY_ANALYZE_FILE);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        Uri uri = data.getData();
        String filePath = getRealPathFromURI(uri);
        switch (requestCode){
            case ACTIVITY_ANALYZE_FILE:
                String text = readFileAsString(filePath);
                ArrayList arrayList = analyze(text);
                showAnalyzeResultsDialog(arrayList);
                break;
            case ACTIVITY_EDIT_FILE:
                Intent intent = new Intent(this, EditorActivity.class);
                intent.putExtra("PATH", filePath);
                startActivity(intent);
        }
    }

    private void sendChooseIntent(int code) {
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("file/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(intent, code);
    }
}