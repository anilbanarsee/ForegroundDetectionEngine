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
public class RepeatLearner {
    public static void main(String[] args) throws IOException{
        for(int i=0; i<500; i++){
            
            Learner.main(null);
        }
    }
}
