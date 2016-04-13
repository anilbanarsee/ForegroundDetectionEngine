/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Release;

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
    double threshold = 0.8;
    
    public ForegroundDetection(String path){
        network = (BasicNetwork)EncogDirectoryPersistence.loadObject(new File(path));
    }
    public void setImage(String path) throws IOException{
        
        image = ImageIO.read(new File(path));
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
                
                inputs[x] = new double[]{red, green, blue, red*red, green*green, blue*blue, red*green, red*blue, green*blue};
                    
                    //System.out.println("Hello");
                    
                
                
                
               
                
                
            }
            
            
             trainingSet = new BasicMLDataSet(inputs, outputs);
             int i = 0;
             for(MLDataPair pair: trainingSet){
                    final MLData output = network.compute(pair.getInput());
                    //System.out.println(Arrays.toString(output.getData()));
                    if(output.getData(0)>=threshold){
                        image.setRGB(i, y, Color.RED.getRGB());
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
        for(int[] i: regionHist){
            //System.out.println(Arrays.toString(i));
        }
        for(int x=0; x<regions.length; x++){
            for(int y=0; y<regions[0].length; y++){
                
                double perc = regionHist[regions[x][y]][1];
                perc = perc/regionHist[regions[x][y]][0];
                
                image.setRGB(x, y, Color.WHITE.getRGB());
                if(perc>0.3){
                    image.setRGB(x, y, Color.WHITE.getRGB());
                    //System.out.println(perc);
                }
                if(perc>0.4){
                    image.setRGB(x, y, Color.BLUE.getRGB());
                    //System.out.println(perc);
                }
                if(perc>0.5){
                    image.setRGB(x, y, Color.GREEN.getRGB());
                    //System.out.println(perc);
                }
                if(perc>0.6){
                    image.setRGB(x, y, Color.YELLOW.getRGB());
                    //System.out.println(perc);
                }
                if(perc>0.7){
                image.setRGB(x, y, Color.ORANGE.getRGB());
                    //System.out.println(perc);
                }
                if(perc>0.8){
                    image.setRGB(x, y, Color.RED.getRGB());
                    //System.out.println(perc);
                }
                if(perc>0.9){
                    image.setRGB(x, y, Color.BLACK.getRGB());
                    //System.out.println(perc);
                }

                
            }
        }
    }
    public void generateRegions(String path){
        ImageSegmentation ig = new ImageSegmentation(path);
        ig.generateSeeds(1000);
        ig.setThreshold(30);
        ig.setMergeThreshold(20);
        
        numberOfRegions = ig.growRegion();
        ig.testMergeImage();
        regions = ig.returnRegions();
    }
    
    
}
