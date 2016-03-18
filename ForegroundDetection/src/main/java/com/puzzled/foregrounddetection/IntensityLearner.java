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

/**
 *
 * @author User
 */
public class IntensityLearner {
    public double learn(String imagePath) throws IOException{
            
            String regionPath = Utils.convertToRegionFile(imagePath);
            
            final File imageFile = new File(imagePath);
            final File regionFile = new File(regionPath);
            
            final BufferedImage image = ImageIO.read(imageFile);
            
            BufferedReader br = new BufferedReader(new FileReader(regionFile));
            
            double[][] inputs = new double[image.getWidth()][9];
            double[][] outputs = new double[image.getWidth()][1];
            
    }
}
