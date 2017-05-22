package com.example.mrorb_000.myapplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by mrorb_000 on 06.04.2017.
 */

public class Porazatel {// HUINYA
// HUINYA
// HUINYA
// HUINYA
// HUINYA
// HUINYA// HUINYA// HUINYA
    // HUINYA// HUINYAv// HUINYA// HUINYA// HUINYA// HUINYA
    //Eto moy kod dlya npk ha-ha!










    private static boolean KakoySimvol(char simvol) {
        return (simvol >= 'a' && simvol <= 'z') || (simvol >= 'A' && simvol <= 'Z');
    }

    private static double MNK(ArrayList<Double> Love, ArrayList<Double> Lerocka) {
        double m, a11 = Love.size(), a12 = 0, a21 = 0, a22 = 0, d1 = 0, d2 = 0, D, D2;
        int mda = Lerocka.size();
        for (int n = 0; n < mda; n++) {
            a12 += Love.get(n);
            a21 += a12;
            a22 += Love.get(n) * Love.get(n);
            d1 += Lerocka.get(n);
            d2 += Lerocka.get(n) * Love.get(n);
        }
        D = a11 * a22 - a21 * a12;
        D2 = a11 * d2 - a21 * d1;
        m = D2 / D;
        return m;

    }

    public static ArrayList<Integer> CreaterDlinSlovArray(char[] text) {
        int DlinaSlova = 0;
        ArrayList<Integer> DlinSlovArray = new ArrayList<>();
        for (int count = 0; count < text.length; count++) {
            char simvol = text[count];
            if (KakoySimvol(simvol)) {
                DlinaSlova++;
                if (count == (text.length - 1)) {
                    DlinSlovArray.add(DlinaSlova);
                }
            } else {
                if (DlinaSlova == 0)
                    continue;
                DlinSlovArray.add(DlinaSlova);
                DlinaSlova = 0;
            }
        }
        return (DlinSlovArray);
    }

    public static double Razmah(ArrayList<Integer> DlinSlovArray) {
        ArrayList<Double> NakopArray = new ArrayList<>();
        double Nakop = 0;
        double Sredn = Sredn(DlinSlovArray);
        for (int i = 0; i < DlinSlovArray.size(); i++) {
            Nakop += DlinSlovArray.get(i) - Sredn;
            NakopArray.add(Nakop);
        }
        double min = 999999;
        double max = 0;
        for (int i = 0; i < NakopArray.size(); i++) {
            if (max <= NakopArray.get(i)) {
                max = NakopArray.get(i);
            }
            if (min >= NakopArray.get(i)) {
                min = NakopArray.get(i);
            }
        }
        return (max - min);
    }

    private static int Summa(ArrayList<Integer> DlinSlovArray) {
        int summ = 0;
        for (int NomerSlova = 0; NomerSlova < DlinSlovArray.size(); NomerSlova++) {
            summ += DlinSlovArray.get(NomerSlova);
        }
        return (summ);
    }

    public static double Srednekvadrat(ArrayList<Integer> DlinSlovArray) {
        double Sredn = Sredn(DlinSlovArray);
        double snizu = 0;
        for (int i = 0; i < DlinSlovArray.size(); i++) {
            snizu += (Sredn - DlinSlovArray.get(i)) * (Sredn - DlinSlovArray.get(i));
        }
        return (Math.sqrt(snizu / DlinSlovArray.size()));
    }

    public static double Sredn(ArrayList<Integer> DlinSlovArray) {
        return (double) Summa(DlinSlovArray) / DlinSlovArray.size();
    }

    private static void MetodNormRazmaha(ArrayList<Integer> X) {
        int L = X.size();
        int pmax = 10;
        ArrayList E = new ArrayList();
        ArrayList N = new ArrayList();
        ArrayList dX = new ArrayList<Integer>();
        for (int p = 1; p < pmax; p++) {
            for (int i = 0; i <= L - pmax; i++) {
                dX.add(X.get(i + p) - X.get(i));
            }
            N.add(Math.log10(Srednekvadrat(dX)));
            E.add(Math.log10(p));
            dX = new ArrayList();
        }
        System.out.println("PokHer" + MNK(N, E));
    }

    private static void MetodModuleyPrirasheniya(ArrayList<Integer> DlinSlovArray) {
        int n = DlinSlovArray.size();
        int mda = 0;
        double kappa = 0;
        ArrayList<Double> ArrayAM = new ArrayList<>();
        ArrayList<Double> ArrayM = new ArrayList<>();
        ArrayList<Integer> ArrayPrirasheniy = CreateArrayPrirasheniy(DlinSlovArray);
        double mappa = 0;
        ArrayList<Double> ArrayAM2 = new ArrayList<>();
        ArrayList<Double> Array = new ArrayList<>();
        double SredneePrirashenie = Sredn(ArrayPrirasheniy);
        int m = 1 + (int) (3.322 * Math.log10(n));
        for (; m < n; m++) {
            ArrayM.add(Math.log10((double) m));
            for (int k = 0; k < n / m; k++) {
                for (int i = k * m; i < (k + 1) * m; i++) {
                    mda += ArrayPrirasheniy.get(i);
                }
                Array.add((double) mda / (double) m);
                mda = 0;
            }
            for (int k = 0; k < n / m; k++) {
                kappa += Math.abs(Array.get(k) - SredneePrirashenie);
                mappa += (Array.get(k) - SredneePrirashenie) * (Array.get(k) - SredneePrirashenie);
            }
            ArrayAM2.add(Math.log10(mappa / n / m));
            ArrayAM.add(Math.log10(kappa / n / m));
            Array = new ArrayList<>();
            kappa = 0;
            mappa = 0;
        }
        double tangens = MNK(ArrayAM, ArrayM) + 1;
        double tangens2 = (MNK(ArrayAM2, ArrayM) + 2) / 2;
        System.out.println("Показатель Херста = " + tangens);
        System.out.println("Pokaefa" + tangens2);
    }


    private static ArrayList<Integer> CreateArrayPrirasheniy(ArrayList<Integer> DlinSlovArray) {
        ArrayList<Integer> ArrayPrirasheniy = new ArrayList<>();
        ArrayPrirasheniy.add(DlinSlovArray.get(0));
        for (int count = 1; count < DlinSlovArray.size(); count++) {
            ArrayPrirasheniy.add(DlinSlovArray.get((count)) - DlinSlovArray.get(count -
                    1));
        }
        return ArrayPrirasheniy;
    }

    private static void MetodNormirovannogoRazmaha(ArrayList<Integer> DlinSlovArray) {
        double SredneKv = Srednekvadrat(DlinSlovArray);
        double Razmah = Razmah(DlinSlovArray);
        System.out.println("Показатель Херста методом нормированного размаха = " + ((Math.log10(Razmah) - Math.log10(SredneKv)) / (Math.log10(DlinSlovArray.size()) - Math.log10(2))));
    }

    public static void main(String[] args) throws FileNotFoundException {
        String myText = "";
        Scanner md = new Scanner(new File("C:\\Apps\\myText.txt"));

        while (md.hasNext()) {
            myText += md.nextLine() + "\r\n";
        }

        char[] charArray =
                myText.toCharArray();//Массив символов
        ArrayList<Integer> DlinSlovArray = CreaterDlinSlovArray(charArray);//Массив длин слов
        MetodNormRazmaha(DlinSlovArray);
        MetodNormirovannogoRazmaha(DlinSlovArray);
        MetodModuleyPrirasheniya(DlinSlovArray);

    }
    public static void lol(String str){
        char[] charArray =
                str.toCharArray();
        ArrayList<Integer> DlinSlovArray = CreaterDlinSlovArray(charArray);
        double SredneKv = Srednekvadrat(DlinSlovArray);
        double Razmah = Razmah(DlinSlovArray);
        double pokher = (Math.log10(Razmah) - Math.log10(SredneKv)) / (Math.log10(DlinSlovArray.size()) - Math.log10(2));
    }
}

