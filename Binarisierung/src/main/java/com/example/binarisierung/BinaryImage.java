package com.example.binarisierung;




import org.ujmp.jmatio.ImportMatrixMAT;
import org.ujmp.core.Matrix;
import java.io.File;
import java.io.IOException;


public class BinaryImage{
    public  static void main(String[] args)throws IOException{
        //相对路径的根目录是当前工程的目录（C:\Users\hfz\Desktop\test）。另外相对路径的起始处无“/”
        ImportMatrixMAT test=new ImportMatrixMAT();
        File file=new File("D:\\xch\\Daten\\Java_imagetext\\ImageProcessing\\grayscaleimage_double.mat");
        Matrix testMatrix=test.fromFile(file);
        testMatrix.showGUI();
        System.out.println("ss");


    }
}


