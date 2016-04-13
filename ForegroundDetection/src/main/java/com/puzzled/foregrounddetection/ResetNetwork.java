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
public class ResetNetwork {
    public static void main(String[] args) throws IOException{
        RGBLearner.createNewNetwork("encogexample.eg");
    }
}
