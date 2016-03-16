/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzzled.foregrounddetection;

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
        
        for(int i = 0; i< 100; i++){
            System.out.println(i);
            try {
                rgbLearner.train("assets//images//3002154.jpg");
                rgbLearner.train("assets//images//9002577.jpg");
                rgbLearner.train("assets//images//1001685.jpg");
                rgbLearner.train("assets//images//9000868.jpg"); //blue car
                rgbLearner.train("assets//images//1100007.jpg");
                
                
            } catch (IOException ex) {
                System.out.println("Could not find image at "+ex.getMessage());
            }
            //rgbLearner.endTraining();
        }
        rgbLearner.endTraining();
    }
}
