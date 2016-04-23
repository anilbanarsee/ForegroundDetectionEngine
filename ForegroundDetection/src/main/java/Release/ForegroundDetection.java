/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Release;

import ImageProcessing.ImageProcessing;
import ImageSegmentation.ImageSegmentation;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
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
public class ForegroundDetection {
    
    int[][] regions;
    int[][] foreground;
    BasicNetwork network;
    BufferedImage image;
    int numberOfRegions;
    double threshold = 0.9;
    
    public ForegroundDetection(String path){
        network = (BasicNetwork)EncogDirectoryPersistence.loadObject(new File(path));
    }
    public void setImage(String path) throws IOException{
        
        BufferedImage image2 = ImageIO.read(new File(path));
        image = ImageIO.read(new File(path));
        //ImageProcessing.blur(image2, image);
        foreground = new int[image.getWidth()][image.getHeight()];
    }
    public BufferedImage getForegroundPixels(String path){
        
        
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
                
                
                inputs[x] = new double[]{1, r, g, b};  
                    //System.out.println("Hello");
                    
                
                
                
               
                
                
            }
            
            
             trainingSet = new BasicMLDataSet(inputs, outputs);
             int i = 0;
             for(MLDataPair pair: trainingSet){
                    final MLData output = network.compute(pair.getInput());
                    //System.out.println(Arrays.toString(output.getData()));
                    if(output.getData(0)>=threshold){
                        //image.setRGB(i, y, Color.RED.getRGB());
                        foreground[i][y] = 1;
                        //System.out.println(Arrays.toString(output.getData()));
                    }
                    else{
                        //image.setRGB(i, y, Color.WHITE.getRGB());
                        //System.out.print("O");
                    }
                    i++;
            //System.out.println(pair.getInput().getData(0) + ", " + pair.getInput().getData(1) + ", actual=" + output.getData(0) + ",ideal =" + pair.getIdeal().getData(0));
            }
             //System.out.println();

        }
        Encog.getInstance().shutdown();
        return image;
    }
    public void thresholdRegions(){
        
        System.out.println(regions.length+", "+regions[0].length);
        System.out.println(foreground.length+", "+foreground[0].length);
        int[][] regionHist = new int[numberOfRegions][2];
        
        for(int x=0; x<regions.length; x++){
            for(int y=0; y<regions[0].length; y++){
                if(foreground[x][y]!=0)
                    regionHist[regions[x][y]][1]++;
                regionHist[regions[x][y]][0]++;
            }
        }
         int index = 0;
        {
                double x = 0.05;
        double y = 0.05;
        
        x =  x*image.getWidth();
        y =  y*image.getHeight();
        
        int xPos = (int) x;
        int yPos = (int) y;
        index = regions[xPos][yPos];
        
        System.out.println("hello"+xPos +", "+ yPos);
            System.out.println("debug "+regions[xPos][yPos]);
            regionHist[regions[xPos][yPos]][1] = 0;
            //image.setRGB(xPos, yPos, Color.RED.getRGB());
        }
        for(int[] i: regionHist){
            //System.out.println(Arrays.toString(i));
        }
        for(int x=0; x<regions.length; x++){
            for(int y=0; y<regions[0].length; y++){
                
                //System.out.print(regions[x][y]+" :");
                
                double perc = regionHist[regions[x][y]][1];
                perc = perc/regionHist[regions[x][y]][0];
                
                //System.out.println(perc+"");
                
                if(regions[x][y]==index){
                    image.setRGB(x, y, Color.BLACK.getRGB());
                }
                else if(regions[x][y]==0){
                   image.setRGB(x,y, Color.BLACK.getRGB());
                }
                else{
                
                //image.setRGB(x, y, Color.WHITE.getRGB());
                if(perc>0.7){
                    image.setRGB(x, y, Color.BLACK.getRGB());
                    //System.out.println(perc);
                }
             
                }
                
            }
        }

        
        
    }
    public void generateRegions(String path){
        ImageSegmentation ig = new ImageSegmentation(path);
        ig.generateSeeds(500);
        ig.setThreshold(42);
        ig.setMergeThreshold(20);
        
        numberOfRegions = ig.growRegion();
        ig.testImage();
        ig.testMergeImage();
        regions = ig.returnRegions();
    }
    
    
}
