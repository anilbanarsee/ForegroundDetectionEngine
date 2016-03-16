/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzzled.foregrounddetection;

import java.io.IOException;

/**
 *
 * @author Reetoo
 */
public class AltMainLearner {
    
     public static void main(String[] args){
        
        
        for(int i = 0; i< 1000; i++){
            System.out.println(i);
            RGBLearner rgbLearner = new RGBLearner("encogexample.eg", "encogexample2.eg");
            try {
                rgbLearner.train("assets//images//3002154.jpg");
                //rgbLearner.train("assets//images//9002577.jpg");
                //rgbLearner.train("assets//images//9003378.jpg");
                
            } catch (IOException ex) {
                System.out.println("Could not find image at "+ex.getMessage());
            }
            rgbLearner.endTraining();
            //rgbLearner.endTraining();
        }
        
    }
}
