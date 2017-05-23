package com.example.mrorb_000.myapplication;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Valentun on 22.05.2017.
 */
class Utils {
    static String readFileAsString(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            fis.close();
            bufferedReader.close();

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
