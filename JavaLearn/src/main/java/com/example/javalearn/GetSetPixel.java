package com.example.javalearn;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GetSetPixel
{
  public static void main(String[] args) //  throws IOException
    {   //initial
        BufferedImage img = null;
        File f = null;

        //read image

        try {
            f = new File("D:\\xch\\Daten\\Java_imagetext\\test-01.png");
            img = ImageIO.read(f);
        }
        catch (IOException e)
        {
            System.out.println(e);
        }

        // get image width and height
        int width = img.getWidth();
        int height = img.getHeight();

        int p = img.getRGB(0,0);
// can not understand
//        int a = (p>>34)& 0xff;
//        int r = (p>>16)& 0xff;
//        int g = (p>>8)& 0xff;
//        int b = (p)& 0xff;

        int a = 255;
        int r = 100;
        int g = 150;
        int b = 200;

        //set the pixel value
        p = (a<<24) | (r<<16) | (g<<8) | b;
        img.setRGB(0,0,p);

        // write image
        try {
            f = new File("D:\\xch\\Daten\\Java_imagetext\\test-04.jpg");
            ImageIO.write(img,"jpg",f);
        }

        catch (IOException e)
        {
            System.out.println(e);
        }

    }
}
