/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzzled.foregrounddetection;



import ImageProcessing.ImageProcessing;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.encog.Encog;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;

/**
 *
 * @author Reetoo
 */
public class TestNetwork {
    
    
    
    public static void main(String[] args) throws IOException{
        
          int rgbRed = 255;
        rgbRed = (rgbRed << 8) + 0;
        rgbRed = (rgbRed << 8) + 0;
        
        int rgbBlue = 0;
        rgbBlue = (rgbBlue << 8) + 0;
        rgbBlue = (rgbBlue << 8) + 255;
        
        rgbRed = (rgbBlue + rgbRed)/2;
         
        
        File folder = new File("assets//trainingset");
        for (final File file : folder.listFiles()) {
            
        //String FILENAME = "assets//images//3002154.jpg"; //main
        //String FILENAME = "assets//images//9000868.jpg"; //main
        //String FILENAME = "assets//images//1001685.jpg";
        //String FILENAME = "assets//images//3000148.jpg";
        //String FILENAME = "assets//images//9002577.jpg";
        //String FILENAME = "assets//images//1100007.jpg";
        //String FILENAME = "assets//test.png";
        
        if(file.isDirectory()){
            
        }
        else{
         
        BasicNetwork network = (BasicNetwork)EncogDirectoryPersistence.loadObject(new File("rgbtrain.eg"));
        
        
        BufferedImage image2 = null;
        BufferedImage image = null;
        try{
            image = ImageIO.read(file);
            image2 = ImageIO.read(file);
        }
        catch(IOException e){
            System.out.println("Cannot find image.");
        }
        
      //  ImageProcessing.blur(image2, image);
        
        double[][] inputs = new double[image.getWidth()][3];
        double[][] outputs = new double[image.getWidth()][1];
        MLDataSet trainingSet = null;
        ResilientPropagation train = null;
        
        for (int y = 0; y < image.getHeight(); y++) {
           
            
            
            for (int x = 0; x < image.getWidth(); x++) {
                final int clr = image.getRGB(x, y);
                final int red = (clr & 0x00ff0000) >> 16;
                final int green = (clr & 0x0000ff00) >> 8;
                final int blue = clr & 0x000000ff;
                
                               
                double r = red;
                double g = green;
                double b = blue;
                
                double total = r+g+b;
                
                r = r/total;
                g = g/total;
                b = b/total;

                
                inputs[x] = new double[]{1,r, g, b};
                    
                    //System.out.println("Hello");
                    
                
                
                
               
                
                
            }
            Color[] colors = new Color[]{Color.WHITE,Color.MAGENTA,Color.GREEN,Color.BLUE,Color.CYAN,Color.YELLOW,Color.ORANGE,Color.RED,Color.BLACK, Color.GRAY};
            
            
             trainingSet = new BasicMLDataSet(inputs, outputs);
             int i = 0;
             for(MLDataPair pair: trainingSet){
                    final MLData output = network.compute(pair.getInput());
                    //System.out.println(Arrays.toString(output.getData()));
                    
                    //image.setRGB(i,y, colors[colors.length-1].getRGB());
                    


                    if(output.getData(0)>=0.9){
                         image.setRGB(i, y, Color.WHITE.getRGB());
                        //image.setRGB(i, y, rgbRed);
                        //System.out.println(Arrays.toString(output.getData()));
                    }

                      
                       
                   
                    

                    
                    i++;
            //System.out.println(pair.getInput().getData(0) + ", " + pair.getInput().getData(1) + ", actual=" + output.getData(0) + ",ideal =" + pair.getIdeal().getData(0));
            }
             //System.out.println();

        }
        ImageIO.write(image, "jpg", new File("output/"+file.getName()));
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        
        ImageIcon img = new ImageIcon(image);
        
        JLabel imgLabel = new JLabel(img);
        
        frame.add(imgLabel);
        frame.setVisible(true);
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(file.getName());
        }
        }
        Encog.getInstance().shutdown();
    }
}
