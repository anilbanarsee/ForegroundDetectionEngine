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
import java.util.Arrays;
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
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.persist.EncogDirectoryPersistence;

/**
 *
 * @author Reetoo
 */
public class Learner {
     public static void main(final String args[])
        throws IOException
    {
        
        
        
        
        
        int rgbRed = 255;
        rgbRed = (rgbRed << 8) + 0;
        rgbRed = (rgbRed << 8) + 0;
        
         int rgbBlue = 0;
        rgbBlue = (rgbBlue << 8) + 0;
        rgbBlue = (rgbBlue << 8) + 255;
        
       // NeuralNetwork neuralNetwork = new Perceptron(2, 1);
        
       // DataSet tSet = new DataSet(3, 1);

        final File file = new File("assets//images//3002154.jpg");
        final File file2 = new File("assets//labels//3002154.regions.txt");
        
       // final File file = new File("assets//images//9003378.jpg");
       // final File file2 = new File("assets//labels//9003378.regions.txt");
        final BufferedImage image = ImageIO.read(file);
        
        boolean test = true;
        //test = false;
        BasicNetwork network = null;
        if(test){
        network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, 10));
        network.addLayer(new BasicLayer(new ActivationSigmoid(),true,4));
        network.addLayer(new BasicLayer(new ActivationSigmoid(),false,1));
        network.getStructure().finalizeStructure();
        network.reset();
        }
        else{
        network = (BasicNetwork)EncogDirectoryPersistence.loadObject(new File("encogexample.eg"));
        }
        MLDataSet trainingSet = null;
        ResilientPropagation train = null;
        
             
        double[][] inputs = new double[image.getWidth()][3];
        double[][] outputs = new double[image.getWidth()][1];
        
        
        try (BufferedReader br = new BufferedReader(new FileReader(file2))) {
             String line;
           // while ((line = br.readLine()) != null) {
                 // process the line.     
             
             
        for (int y = 0; y < image.getHeight(); y++) {
           
            line = br.readLine();
            //System.out.println(line);
            String[] splitLine = line.split(" ");
            
            
            for (int x = 0; x < image.getWidth(); x++) {
                final int clr = image.getRGB(x, y);
                final int red = (clr & 0x00ff0000) >> 16;
                final int green = (clr & 0x0000ff00) >> 8;
                final int blue = clr & 0x000000ff;
                
                if(Integer.parseInt(splitLine[x])== 7 ){
                // Color Red get cordinates
                    //image.setRGB(x, y, rgbRed);
                    inputs[x] = new double[]{red, green, blue, red*red, green*green, blue*blue, red*green, red*blue, green*blue};
                    outputs[x] = new double[]{1.0};
                    
                }
                else{
                    inputs[x] = new double[]{red, green, blue, red*red, green*green, blue*blue, red*green, red*blue, green*blue};
                    outputs[x] = new double[]{0.0};
                
                }
            }

             trainingSet = new BasicMLDataSet(inputs, outputs);
             train = new ResilientPropagation(network, trainingSet);
             train.iteration();
        }
        
        } catch (FileNotFoundException e){
            System.out.println("ERROR : CANNOT FIND IMAGE FILE");
            System.exit(0);
        }
        train.finishTraining();
        /*JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        
        ImageIcon img = new ImageIcon(image);
        
        JLabel imgLabel = new JLabel(img);
        
        frame.add(imgLabel);
        frame.setVisible(true);
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        */        
        EncogDirectoryPersistence.saveObject(new File("encogexample.eg"), network);
        
        
        Encog.getInstance().shutdown();

        
       // neuralNetwork.learn(tSet);
        
       // neuralNetwork.save("or_perceptron.nnet");
    }
}

