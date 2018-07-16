package com.example.javalearn;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RedImage {
    public static void main(String args[]){
        BufferedImage img = null;
        File f = null;
        //        read image

        try {
            f = new File("D:\\xch\\Daten\\Java_imagetext\\test-01.png");
            img = ImageIO.read(f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

//        get width and height
        int width = img.getWidth();
        int height = img.getHeight();

//        convert to red image
        for (int y = 0; y < height ; y++) {
            for (int x = 0; x < width ; x++) {

                int p = img.getRGB(x,y);
                int a = (p >>24)&0xff;
                int r = (p >>16)&0xff;
                int g = (p >> 8)&0xff;
                int b = p&0xff;

//          set new RGB
//          keeping the value same as in original
//          image and setting g and b as 0
//                int g = 0;
//                int b = 0;

                p = (a << 24)|(r << 16)|(g << 8)|b;
                img.setRGB(x,y,p);
            }
        }
//        write image
        try {
            f = new File("D:\\xch\\Daten\\Java_imagetext\\test-red_converg.jpg");
            ImageIO.write(img,"jpg",f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}