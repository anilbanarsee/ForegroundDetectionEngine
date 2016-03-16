/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzzled.foregrounddetection;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static javax.script.ScriptEngine.FILENAME;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
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
public class Main {
    
    public static double XOR_INPUT[][] = {{0.0,0.0},{1.0,0.0},{0.0,1.0},{1.0,1.0}};
    
    public static double XOR_IDEAL[][] = {{1.0},{0.0},{0.0},{1.0}};
    
    public static final String FILENAME = "encogexample.eg";
    
    public static void main(String[] args){
        /*
        BasicNetwork network = new BasicNetwork();
        
        network.addLayer(new BasicLayer(null,true,2));
        network.addLayer(new BasicLayer(new ActivationSigmoid(),true,3));
        network.addLayer(new BasicLayer(new ActivationSigmoid(),false,1));
        
        network.getStructure().finalizeStructure();
        network.reset();
        
        MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
        
        final ResilientPropagation train = new ResilientPropagation(network, trainingSet);
        
        int epoch = 1;
        
        do{
            train.iteration();
            System.out.println("Epoch #" + epoch + "Error : "+ train.getError());
            epoch++;
        }
        while(train.getError()>0.01);
        train.finishTraining();
        
        System.out.println("Neural Network Results: ");
        for(MLDataPair pair: trainingSet){
            final MLData output = network.compute(pair.getInput());
            System.out.println(pair.getInput().getData(0) + ", " + pair.getInput().getData(1) + ", actual=" + output.getData(0) + ",ideal =" + pair.getIdeal().getData(0));
        }
        
        Encog.getInstance().shutdown();
         
        
        */
        
        final File file = new File("assets//test.png");
        BufferedImage image = null;
        try{
            image = ImageIO.read(file);
        }
        catch(IOException e){
            System.out.println("Cannot find image.");
        }
        
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, 3));
        network.addLayer(new BasicLayer(new ActivationSigmoid(),true,3));
        network.addLayer(new BasicLayer(new ActivationSigmoid(),false,1));
         network.getStructure().finalizeStructure();
        network.reset();
         
       
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
                
                
                if(red>=200){
                    inputs[x] = new double[]{red,green,blue};
                    outputs[x] = new double[]{1.0};
                    //System.out.println("Hello");
                    
                
                }
                else{
                    inputs[x] = new double[]{red,green,blue};
                    outputs[x] = new double[]{0.0};
                   
                }
               
                
                
            }
             trainingSet = new BasicMLDataSet(inputs, outputs);
                train = new ResilientPropagation(network, trainingSet);
                train.iteration();
        }
        train.finishTraining();
         System.out.println("Neural Network Results: ");
        for(MLDataPair pair: trainingSet){
            final MLData output = network.compute(pair.getInput());
            System.out.println(pair.getInput().getData(0) + ", " + pair.getInput().getData(1) + ", actual=" + output.getData(0) + ",ideal =" + pair.getIdeal().getData(0));
        }
        
        
        for (int y = 0; y < image.getHeight(); y++) {
           
            
            
            for (int x = 0; x < image.getWidth(); x++) {
                final int clr = image.getRGB(x, y);
                final int red = (clr & 0x00ff0000) >> 16;
                final int green = (clr & 0x0000ff00) >> 8;
                final int blue = clr & 0x000000ff;
                
                inputs[x] = new double[]{red,green,blue};
                    
                    //System.out.println("Hello");
                    
                
                
                
               
                
                
            }
            
            
             trainingSet = new BasicMLDataSet(inputs, outputs);
             for(MLDataPair pair: trainingSet){
                    final MLData output = network.compute(pair.getInput());
                    if(output.getData(0)>=0.5){
                        System.out.print("X");
                    }
                    else{
                        
                        System.out.print("O");
                    }
            //System.out.println(pair.getInput().getData(0) + ", " + pair.getInput().getData(1) + ", actual=" + output.getData(0) + ",ideal =" + pair.getIdeal().getData(0));
            }
             System.out.println();

        }
        EncogDirectoryPersistence.saveObject(new File(FILENAME), network);
        
        
        Encog.getInstance().shutdown();
    }
}
        
