package org.schakalacka.java.raytracing;

public class Counter {

    //INFO: Counter : Counter{mulM=10, mulT=98170001, transpose=8047815, determinant=61409504, subM=12, minor=0, cofactor=0, isInvertible=0, inverse=61467434, translate=6, scale=5, rotX=2, rotY=3, rotZ=0, shear=0}

    public  static int mulM = 0;
    public  static int mulT = 0;
    public  static int transpose = 0;
    public  static int determinant = 0;
    public  static int subM = 0;
    public  static int minor = 0;
    public  static int cofactor = 0;
    public  static int isInvertible = 0;
    public  static int inverse = 0;
    public  static int translate = 0;
    public  static int scale = 0;
    public  static int rotX = 0;
    public  static int rotY = 0;
    public  static int rotZ = 0;
    public  static int shear = 0;


    @Override
    public String toString() {
        return "Counter{" +
                "mulM=" + mulM +
                ", mulT=" + mulT +
                ", transpose=" + transpose +
                ", determinant=" + determinant +
                ", subM=" + subM +
                ", minor=" + minor +
                ", cofactor=" + cofactor +
                ", isInvertible=" + isInvertible +
                ", inverse=" + inverse +
                ", translate=" + translate +
                ", scale=" + scale +
                ", rotX=" + rotX +
                ", rotY=" + rotY +
                ", rotZ=" + rotZ +
                ", shear=" + shear +
                '}';
    }
}
