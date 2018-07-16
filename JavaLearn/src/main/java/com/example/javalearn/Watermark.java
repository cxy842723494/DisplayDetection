package com.example.javalearn;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Watermark {
    public static void main(String [] args){
        BufferedImage img = null;
        File f = null;

        // read image
        try {
            f = new File("D:\\xch\\Daten\\Java_imagetext\\test-01.png");
            img = ImageIO.read(f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // create BufferedImage object of same width and height as of input image
        BufferedImage temp = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        // Create graphics object and add original image to it
        Graphics graphics = temp.getGraphics();
        graphics.drawImage(img, 0, 0, null);

        // set font for the watermark text
        graphics.setFont(new Font("Arial", Font.PLAIN,160));
        graphics.setColor(new Color(255,0,0,40));

        // Setting watermark text
        String watermark = "Watermark generated";

        // Add the watermark text at (width/5, height/3) location
        graphics.drawString(watermark, img.getWidth() / 5, img.getHeight()/3);

        // releases any system resources that it is using
        graphics.dispose();
        f = new File("D:\\xch\\Daten\\Java_imagetext\\test-watermark.png");
        try {
            ImageIO.write(temp,"png",f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }
}
