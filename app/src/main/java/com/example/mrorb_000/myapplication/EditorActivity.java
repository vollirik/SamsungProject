package com.example.mrorb_000.myapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;

public class EditorActivity extends BasicActivity {
    private EditText mEditText;
    private String filePath;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_editor);

        filePath = getIntent().getStringExtra("PATH");

        mEditText = (EditText) findViewById(R.id.editText);

        openFile(filePath);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open:
                openFile(filePath);
                break;
            case R.id.action_save:
                saveFile(filePath);
                ArrayList<Double> analyzeResult = Analyzer.analyze(mEditText.getText().toString());
                showAnalyzeResultsDialog(analyzeResult);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == WRITE_STORAGE_CODE) {
            if (isGranted(grantResults)) {
                writeToFile(filePath, mEditText.getText().toString());
            } else {
                checkReadPermission();
            }
        }

        switch (requestCode) {
            case WRITE_STORAGE_CODE:
                if (isGranted(grantResults)) {
                    writeToFile(filePath, mEditText.getText().toString());
                } else {
                    checkWritePermission();
                }
            case READ_STORAGE_CODE:
                if (isGranted(grantResults)) {
                    openFile(filePath);
                } else {
                    checkReadPermission();
                }
        }
    }

    private void saveFile(String path) {
        if (isPermissionRequestNeeded()) {
            if (checkWritePermission()) writeToFile(path, mEditText.getText().toString());
        } else {
            writeToFile(path, mEditText.getText().toString());
        }
    }

    private void openFile(String fileName) {
        if (isPermissionRequestNeeded()) {
            if (checkReadPermission()) {
                String text = Utils.readFileAsString(fileName);
                mEditText.setText(text);
            }
        } else {
            String text = Utils.readFileAsString(fileName);
            mEditText.setText(text);
        }
    }
}