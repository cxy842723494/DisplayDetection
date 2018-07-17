package com.example.javalearn;

/*
Algorithm:
1. Get the RGB value of the pixel.
2. Find the average of RGB i.e., Avg = (R+G+B)/3.
3. Replace the R,G and B value of the pixel with average(Avg) calculated in step2.
4. Repeat Step 1 to Step 3 for each pixel of the image.
*/

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

//  demonstrate colored to gray scale conversion
public class RGB2Gray {
    public static void main (String[] args) throws IOException
    {
        BufferedImage img = null;
        File f= null;

//        read the images
        try {
            f = new File("G:\\Daten\\test-01.png");
            img = ImageIO.read(f);
            System.out.println("Reading complete.");
        }
        catch (IOException e)
        {
            System.out.println(e);
        }

//        get the width and the height of the image
        int width = img.getWidth();
        int height = img.getHeight();

//        covert to gray scale
        for (int y = 0;y < height; y++)
        {
            for (int x = 0;x < width; x++)
            {
//                Here (x,y) denote the coordinate of the image
//                for modifying the pixel value.
                int p = img.getRGB(x,y);

                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b =  p&0xff;

//                calculate average
                int arg = (r+g+b)/3;

//                replace RGB value with avg
                p = (a<<24)|(arg<<16)|(arg<<8)|(arg);
                img.setRGB(x,y,p);
            }
        }

//        write image
        try
        {
            f = new File("G:\\Daten\\test_3.jpg");
            ImageIO.write(img,"jpg",f);
            System.out.println("Writing complete.");
        }
        catch (IOException e)
        {
            System.out.println(e);
        }

    }

}
