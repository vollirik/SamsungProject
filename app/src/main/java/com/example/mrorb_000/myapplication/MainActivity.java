package com.example.mrorb_000.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    String text;
    AlertDialog.Builder ad;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void CreaterAlertDialog(ArrayList arrayList) {
        String title = "Analiz zakonchen";

        String message = "Pokazatel Hersta =";//Potom dodelayu

        String button1String = "save";
        String button2String = "delete";

        ad = new AlertDialog.Builder(context);

        ad.setTitle(title);

        ad.setMessage(message);

        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {// KOROCHE TUT REZULTATI ANALIZA DOLJNI V BD DOBAVLYATSA


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

    public ArrayList analizText(String text) {//sdelal nahuya to arraylist lol, tut tip resultati analiza
        ArrayList<Double> arrayListResult = new ArrayList();
        char[] charArray = text.toCharArray();
        ArrayList<Integer> DlinSlovArray = Porazatel.CreaterDlinSlovArray(charArray);
        double SredneKv = Porazatel.Srednekvadrat(DlinSlovArray);

        arrayListResult.add((double) DlinSlovArray.size());
        arrayListResult.add(Porazatel.Sredn(DlinSlovArray));
        arrayListResult.add(SredneKv);
        arrayListResult.add((Math.log10(Porazatel.Razmah(DlinSlovArray)) - Math.log10(SredneKv)) / (Math.log10(DlinSlovArray.size()) - Math.log10(2)));
       return arrayListResult;
    }

    public String prepareText() {//Tipo text tut gotovlu - na dele huinya, HELP
        String text = null;
        try {
            String filename = "text.txt";
            InputStream inputStream = openFileInput(filename);

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                text = builder.toString();
                inputStream.close();
            }


        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
        return text;
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_about:
                Intent intent = new Intent(this, about.class);
                startActivity(intent);
                break;


            case R.id.button_analiz:
                text = prepareText();
                ArrayList arrayList = analizText(text);
                CreaterAlertDialog(arrayList);

                break;
            case R.id.button_graph:
                Intent intent2 = new Intent(this, graph.class);
                startActivity(intent2);
                break;
            case R.id.button_editor:
                Intent intent3 = new Intent(this, Editor.class);
                startActivity(intent3);
                break;

        }

    }
}