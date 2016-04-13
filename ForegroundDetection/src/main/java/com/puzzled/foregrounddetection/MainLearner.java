/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzzled.foregrounddetection;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Reetoo
 */
public class MainLearner {
    public static void main(String[] args){
        RGBLearner rgbLearner = new RGBLearner("encogexample.eg", "encogexample2.eg");
        IntensityLearner intLearner = new IntensityLearner("intensitystart.eg","intensitytrain.eg");
        File folder = new File("assets//trainingset");
        

        for(int i = 0; i< 5; i++){
            System.out.println(i);
            
            for (final File fileEntry : folder.listFiles()) {
                if(!fileEntry.isDirectory())
                try {
                    
                    rgbLearner.train(fileEntry.getPath());
                    intLearner.train(fileEntry.getPath());
                    //System.out.println(fileEntry.getPath());
                
                } catch (IOException ex) {
                    
                    System.out.println("Could not find image at "+fileEntry.getPath());
                    
                }
            //rgbLearner.endTraining();
            }
            
        }
        
        rgbLearner.endTraining();
        intLearner.endTraining();
     
        
       

    }
}
