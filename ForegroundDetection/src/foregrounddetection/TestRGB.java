/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foregrounddetection;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSetRow;

/**
 *
 * @author Reetoo
 */
public class TestRGB {
    public static void main(String[] args) throws IOException{
        
        int rgbRed = 255;
        rgbRed = (rgbRed << 8) + 0;
        rgbRed = (rgbRed << 8) + 0;
        
        final File file = new File("assets//images//3002154.jpg");
        BufferedImage image = ImageIO.read(file);
    
        NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile("rgb_perceptron.nnet");
        
       
           // while ((line = br.readLine()) != null) {
                 // process the line.
       
        for (int y = 0; y < image.getHeight(); y++) {
           
            
            
            for (int x = 0; x < image.getWidth(); x++) {
                final int clr = image.getRGB(x, y);
                final int red = (clr & 0x00ff0000) >> 16;
                final int green = (clr & 0x0000ff00) >> 8;
                final int blue = clr & 0x000000ff;
                
                neuralNetwork.setInput(red,green,blue);
                neuralNetwork.calculate();
                
                double[] networkOutput = neuralNetwork.getOutput();
                
                if(networkOutput[0]!=1){
                // Color Red get cordinates
                    image.setRGB(x, y, rgbRed);
                  
                
                }
                else{
                    
                }
            }
        
        }
    }
}

