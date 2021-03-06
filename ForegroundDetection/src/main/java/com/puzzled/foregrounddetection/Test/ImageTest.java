/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzzled.foregrounddetection.Test;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Color;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.encog.Encog;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

/**
 *
 * @author Reetoo
 */
public class ImageTest {
    public static void main(String[] args) throws IOException{
        train("assets//images//1100007.jpg");
    }
    public static double train(String imagePath) throws IOException{
        
        int rgbRed = 255;
        rgbRed = (rgbRed << 8) + 0;
        rgbRed = (rgbRed << 8) + 0;
        
        String code = imagePath.substring(16,imagePath.length()-4);
        String regionPath = "assets//labels//"+code+".regions.txt";
        
        final File imageFile = new File(imagePath);
        final File regionFile = new File(regionPath);
        
        final BufferedImage image = ImageIO.read(imageFile);
        
        BufferedReader br = null;
        
        try{
        br = new BufferedReader(new FileReader(regionFile));
        }
        catch(FileNotFoundException e){
            System.out.println("****************************| ERROR CANNOT FIND REGION FILE AT "+regionPath+" |****************************");
        }
        
        double[][] inputs = new double[image.getWidth()][9];
        double[][] outputs = new double[image.getWidth()][1];
        
        MLDataSet trainingSet = null;
        ResilientPropagation train = null;
        
        for (int y=0; y<image.getHeight(); y++){
            
            String line = br.readLine();
            
            String[] splitLine = line.split(" ");
            
            for (int x = 0; x < image.getWidth(); x++){
                
                final int clr = image.getRGB(x, y);
                final int red = (clr & 0x00ff0000) >> 16;
                final int green = (clr & 0x0000ff00) >> 8;
                final int blue = clr & 0x000000ff;
                
                Color c = new Color( green, red, blue);
                
                
                inputs[x] = new double[]{red, green, blue, red*red, green*green, blue*blue, red*green, red*blue, green*blue};
                
                //image.setRGB(x, y, c.getRGB());
                
                
                if(Integer.parseInt(splitLine[x])==7)                                      
                    image.setRGB(x,y,rgbRed);
                
                else
                    outputs[x] = new double[]{0.0};
                    
            }
               
        }
         JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        
        ImageIcon img = new ImageIcon(image);
        
        JLabel imgLabel = new JLabel(img);
        
        frame.add(imgLabel);
        frame.setVisible(true);
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Encog.getInstance().shutdown();
        return 0.0;
    }
}
