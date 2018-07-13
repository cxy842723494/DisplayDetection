package com.example.javalearn;
// Demonstrate the Read and Write of the image
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MyImage
{
    public static void main(String args[]) // throws IOException
    {

        int width = 3840;
        int height = 2160;
// for store image on RAM
        BufferedImage image =null;
// read image
        try
        {
            File input_file = new File("D:\\xch\\Daten\\Java_imagetext\\test-01.png");
            // is necessary ?
            image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);

            image = ImageIO.read(input_file);

            System.out.println("Reading complete.");

        }
        catch (IOException e){
            System.out.println("Error:"+e);
        }

// write image
        try {
            File output_file = new File("D:\\xch\\Daten\\Java_imagetext\\test-01out.jpg");
            ImageIO.write(image,"jpg",output_file);
            System.out.println("Writing complete.");
            }

        catch (IOException e){
            System.out.println("Error:"+e);
        }

    }


}
