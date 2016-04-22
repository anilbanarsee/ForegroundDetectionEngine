/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzzled.foregrounddetection;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;

/**
 *
 * @author Reetoo
 */
public class TotalNetworkTrainer {
    
    BasicNetwork rgbNetwork
                ,intNetwork
                ,locNetwork;
    
    String readPath, desPath;
    BasicNetwork network;
    
    public TotalNetworkTrainer(String path, String dpath, String rgb, String inte, String loc){
        
        rgbNetwork = (BasicNetwork)EncogDirectoryPersistence.loadObject(new File(rgb));
        intNetwork = (BasicNetwork)EncogDirectoryPersistence.loadObject(new File(inte));
        locNetwork = (BasicNetwork)EncogDirectoryPersistence.loadObject(new File(loc));
        
        readPath = path;
        desPath = dpath;
        
        network = (BasicNetwork)EncogDirectoryPersistence.loadObject(new File(path));
        
    }
    public static void createNewNetwork(String path) throws IOException{
        BasicNetwork network;
        network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, 4));
        network.addLayer(new BasicLayer(new ActivationSigmoid(),true,4));
        network.addLayer(new BasicLayer(new ActivationSigmoid(),false,1));
        network.getStructure().finalizeStructure();
        network.reset();
        EncogDirectoryPersistence.saveObject(new File(path), network);
        Encog.getInstance().shutdown();
    }
    
    public double train(String imagePath) throws IOException{
        
        String regionPath = Utils.convertToRegionFile(imagePath);
        
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
         
        double[][] inputsRGB = new double[image.getWidth()][4];
        double[][] inputsInt = new double[image.getWidth()][4];
        double[][] inputsLoc = new double[image.getWidth()][6];
        
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
                
                double r = red;
                double g = green;
                double b = blue;
                
                double total = r+g+b;
                
                r = r/total;
                g = g/total;
                b = b/total;

                
                
                inputs[x] = new double[]{1,r,g,b};
                
                if(Integer.parseInt(splitLine[x])==7){
                    //image.setRGB(x,y,rgbRed);
                    outputs[x] = new double[]{0.0};
                
                }

                else
                    outputs[x] = new double[]{0.99};
                
                
                    
            }
            
             trainingSet = new BasicMLDataSet(inputs, outputs);
             train = new ResilientPropagation(network, trainingSet);
             //System.out.println(train.getError());
             //train = new Backpropagation(network, trainingSet);
             train.iteration();            
        }
        
        train.finishTraining();
        
        return train.getError();
    }
        
    }
}
