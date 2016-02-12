/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foregrounddetection;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;

/**
 *
 * @author Reetoo
 */
public class LearnImage {
   public static void main(final String args[])
        throws IOException
    {
        
        NeuralNetwork neuralNetwork = new Perceptron(3,1);
        
        DataSet tSet = new DataSet(3,1);
        
        
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
        final BufferedImage image = ImageIO.read(file);

        try (BufferedReader br = new BufferedReader(new FileReader(file2))) {
             String line;
           // while ((line = br.readLine()) != null) {
                 // process the line.
       
        for (int y = 0; y < image.getHeight()/100; y++) {
           
            line = br.readLine();
            System.out.println(line);
            String[] splitLine = line.split(" ");
            
            
            for (int x = 0; x < image.getWidth()/100; x++) {
                final int clr = image.getRGB(x, y);
                final int red = (clr & 0x00ff0000) >> 16;
                final int green = (clr & 0x0000ff00) >> 8;
                final int blue = clr & 0x000000ff;
                
                if(Integer.parseInt(splitLine[x])== 7 ){
                // Color Red get cordinates
                    image.setRGB(x, y, rgbRed);
                    tSet.addRow(new DataSetRow(new double[]{red, green, blue}, new double[]{1}));
                
                }
                else{
                    tSet.addRow(new DataSetRow(new double[]{red, green, blue}, new double[]{0}));
                }
            }
        }
        System.out.println("1");
        neuralNetwork.learn(tSet);
        System.out.println("2");
        neuralNetwork.save("rgb_perceptron.nnet");
            }
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        
        ImageIcon img = new ImageIcon(image);
        
        JLabel imgLabel = new JLabel(img);
        
        frame.add(imgLabel);
        frame.setVisible(true);
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
        

        
       // neuralNetwork.learn(tSet);
        
       // neuralNetwork.save("or_perceptron.nnet");
    }
}
