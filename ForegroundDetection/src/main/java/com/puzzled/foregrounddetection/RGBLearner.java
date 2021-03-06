/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzzled.foregrounddetection;

import static com.puzzled.foregrounddetection.Main.FILENAME;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
 * @author Reetoo
 */
public class RGBLearner {
    String encogFilePath;
    String desPath;
    BasicNetwork network;
    
    public RGBLearner(String epath, String desPath){
        encogFilePath = epath;
        this.desPath = desPath;
        network = (BasicNetwork)EncogDirectoryPersistence.loadObject(new File(epath));
    }
    public static void createNewNetwork(String path) throws IOException{
        BasicNetwork network;
        network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, 4));
        network.addLayer(new BasicLayer(new ActivationSigmoid(),true,2));
        network.addLayer(new BasicLayer(new ActivationSigmoid(),false,1));
        network.getStructure().finalizeStructure();
        network.reset();
        EncogDirectoryPersistence.saveObject(new File(path), network);
        Encog.getInstance().shutdown();
    }
    public double train(String imagePath) throws IOException{
        
                  int rgbRed = 255;
        rgbRed = (rgbRed << 8) + 0;
        rgbRed = (rgbRed << 8) + 0;
        

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
        
        double[][] inputs = new double[image.getWidth()][4];
        double[][] outputs = new double[image.getWidth()][1];
        
        MLDataSet trainingSet = null;
        ResilientPropagation train = null;
        //Backpropagation train = null;
        
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

                
                
                inputs[x] = new double[]{1,r, g, b};
                
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
    public void endTraining(){
        EncogDirectoryPersistence.saveObject(new File(desPath), network);
        Encog.getInstance().shutdown();
    }
}
