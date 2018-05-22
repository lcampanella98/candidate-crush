package com.gmail.enzocampanella98.candidatecrush.tools;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Lorenzo Campanella on 6/13/2016.
 */
public class ArrayTools {

    public static <T> String getTwoDimArrayString(T[][] a) {
        String s = "{\n";
        for (T[] a1 :
                a) {
            s += " { ";
            for (T e :
                    a1) {
                s += e.toString() + " , ";
            }
            s += " }\n";
        }
        s += "\n}";
        return s;
    }

    public static <T> String getTwoDimArrayString(Array<Array<T>> a) {
        String s = "{\n";
        for (Array<T> a1 :
                a) {
            s += " { ";
            for (T e :
                    a1) {
                s += e.toString() + " , ";
            }
            s += " }\n";
        }
        s += "\n} ";
        return s;
    }

    public static <T> String getArrayString(Array<T> a) {
        String s = "{\n";
        for (T t : a) {
            s += t.toString() + " , ";
        }
        s += "\n}";
        return s;
    }

    public static <T> String getArrayString(T[] a) {
        String s = "{\n";
        for (T t : a) {
            s += t.toString() + " , ";
        }
        s += "\n}";
        return s;
    }
}
