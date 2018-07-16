package com.example.javalearn;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RandomImage {

    public static void main(String [] args) throws IOException {
        // Image file dimension
        int width = 640;
        int height = 320;

        // Create the buffered image object
        BufferedImage img = null;
        File f = null;

        // read image
        try {
            f = new File("D:\\xch\\Daten\\Java_imagetext\\test-01.png");
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            img = ImageIO.read(f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        // create random values pixel by pixel
        for (int y = 0; y < height ; y++){
            for (int x = 0; x < width; x++) {
                int a = (int) (Math.random() * 256);//generating
                int r = (int) (Math.random() * 256);
                int g = (int) (Math.random() * 256);
                int b = (int) (Math.random() * 256);

                int p = (a<<24) | (r<<16) | (g<<8) | b; //pixel
                img.setRGB(x,y,p);
            }
        }

     // write image
        // write image
        try
        {
            f = new File("D:\\xch\\Daten\\Java_imagetext\\test-random.jpg");
            ImageIO.write(img, "jpg", f);
        }
        catch(IOException e)
        {
            System.out.println("Error: " + e);
        }
    }
}

