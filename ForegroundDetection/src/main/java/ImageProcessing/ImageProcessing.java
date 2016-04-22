/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Reetoo
 */
public class ImageProcessing {
    
    public static BufferedImage blur(BufferedImage image, BufferedImage destImage){
   
        float data[] = { 0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f, 0.0625f, 0.125f, 0.0625f };
    
        //float data[] = { -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f,-1.0f, -1.0f };
    
        Kernel kernel = new Kernel(3, 3, data);
    
        ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP,
            null);
    
        convolve.filter(image, destImage);
        return destImage;
    }
    
    public static void main(String[] args) throws IOException{
        String path = "assets/cow.jpg";
        BufferedImage image = ImageIO.read(new File(path));
        BufferedImage destImage = ImageIO.read(new File(path));
        
        blur(image, destImage);
        
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        
        ImageIcon img = new ImageIcon(destImage);
        
        JLabel imgLabel = new JLabel(img);
        
        frame.add(imgLabel);
        frame.setVisible(true);
        
        frame.setSize(image.getWidth(), image.getHeight());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
                
        image = ImageIO.read(new File(path));
        JFrame frame2 = new JFrame();
        JPanel panel2 = new JPanel();
        
        ImageIcon img2 = new ImageIcon(image);
        
        JLabel imgLabel2 = new JLabel(img);
        
        frame2.add(imgLabel2);
        frame2.setVisible(true);
        
        frame2.setSize(image.getWidth(), image.getHeight());
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        
    }
    
}
