package com.example.javalearn;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Negativ {
    public static void main(String args[]){
        BufferedImage img = null;
        File f = null;

//        read the image
        try {
            f = new File("D:\\xch\\Daten\\Java_imagetext\\test-01.png");
            img = ImageIO.read(f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

//        get the height and width of the image
        int width = img.getWidth();
        int height = img.getHeight();

//        covert to negativ
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x,y);
                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;

//                subtract RGB from 255
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;

//                set new RGB value
                p = (a<<24)|(r<<16)|(g<<8)|b;
                img.setRGB(x,y,p);
            }
        }

//        write the image
        try {
            f = new File("D:\\xch\\Daten\\Java_imagetext\\test-negative.jpg");
            ImageIO.write(img,"jpg",f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
