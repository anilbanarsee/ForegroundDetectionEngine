/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzzled.foregrounddetection;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;

/**
 *
 * @author User
 */
public class IntensityLearner {
    
    String encogFilePath;
    String desPath;
    BasicNetwork network;
    
    
    public IntensityLearner(String epath, String desPath){
        encogFilePath = epath;
        this.desPath = desPath;
        network = (BasicNetwork)EncogDirectoryPersistence.loadObject(new File(epath));
    }
    
    public double train(String imagePath) throws IOException{
            
            String regionPath = Utils.convertToRegionFile(imagePath);
            
            final File imageFile = new File(imagePath);
            final File regionFile = new File(regionPath);
            
            final BufferedImage image = ImageIO.read(imageFile);
            
            BufferedReader br = new BufferedReader(new FileReader(regionFile));
            
            double[][] inputs = new double[image.getWidth()][3];
            double[][] outputs = new double[image.getWidth()][1];
            
            MLDataSet trainingSet = null;
            ResilientPropagation train = null;
            
            for(int y = 0; y<image.getHeight(); y++){
                String line = br.readLine();
                
                String[] splitLine = line.split(" ");
                
                for(int x = 0; x<image.getWidth(); x++){
                    
                    
                    int clr = image.getRGB(x, y);
                    final int red = (clr & 0x00ff0000) >> 16;
                    final int green = (clr & 0x0000ff00) >> 8;
                    final int blue = clr & 0x000000ff;
                    
                    clr = getIntensity(red, green, blue);
                    
                    
                    inputs[x] = new double[]{1,clr,clr*clr};
                    
                    if(Integer.parseInt(splitLine[x])==7){
                        outputs[x] = new double[]{1.0};
                    }
                    
                    else
                        outputs[x] = new double[]{0.1};
                    
                }
                trainingSet = new BasicMLDataSet(inputs, outputs);
                train = new ResilientPropagation(network, trainingSet);
            }
            
        train.finishTraining();
        
        return train.getError();
            
    }
    public static void setupNetwork(String path){
        
            BasicNetwork network;
            network = new BasicNetwork();
            network.addLayer(new BasicLayer(null, true, 3));
            network.addLayer(new BasicLayer(new ActivationSigmoid(),true,3));
            network.addLayer(new BasicLayer(new ActivationSigmoid(),false,1));
            network.getStructure().finalizeStructure();
            network.reset();
            EncogDirectoryPersistence.saveObject(new File(path), network);
            Encog.getInstance().shutdown();
    }
    public void endTraining(){
        EncogDirectoryPersistence.saveObject(new File(desPath), network);
        Encog.getInstance().shutdown();
    }
    public int getIntensity(int r, int g, int b){
         
        int i = (r+g+b)/3;
        
        return i;
       
    }
}
