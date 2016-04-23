/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageSegmentation;

import ImageProcessing.ImageProcessing;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Reetoo
 */
public class ImageSegmentation {
    ArrayList<int[]> seeds;
    int[][] regions;
    int[][] links;
    ArrayList<int[]> colors;
    ArrayList<Integer> mColors;
    BufferedImage image;
    BufferedImage mergeImage;
    int mergeThreshold = 40;
    int threshold = 100;
    String filename = "";
    
    
    public ImageSegmentation(String path){
        seeds = new ArrayList<>();
        colors = new ArrayList<>();
        try {
            filename = new File(path).getName();
            BufferedImage tempimage = ImageIO.read(new File(path));
            image = ImageIO.read(new File(path));
            image = ImageProcessing.blur(tempimage, image);
            mergeImage = ImageIO.read(new File(path));
        } catch (IOException ex) {
            System.out.println("Could not find image at "+path);
            
        }
        regions = new int[image.getWidth()][image.getHeight()];
    }
    public void setThreshold(int n){
        threshold = n;
    }
    public void setMergeThreshold(int n){
        mergeThreshold = n;
    }
    public void addSeed(int x, int y){
        seeds.add(new int[]{x,y});
        regions[x][y] = seeds.size();
        colors.add(new int[]{image.getRGB(x, y),1,image.getRGB(x,y)});
    }
    public void generateSeeds(int x){
        Random r = new Random();
        
        for(int i=0; i<x; i++){
            int xp = r.nextInt(image.getWidth());
            int yp = r.nextInt(image.getHeight());
            addSeed(xp,yp);
        }
        links = new int[x][x];
    }
    public void modColor(int region, int color){
        int[] avg = colors.get(region-1);
        int clr = avg[2];
        Color c = new Color(clr);
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        
        Color c2 = new Color(color);
        
        int num = avg[1];
        
        r= ((r*num)+c2.getRed())/(num+1);
        g= ((g*num)+c2.getGreen())/(num+1);
        b= ((b*num)+c2.getBlue())/(num+1);
        
        
        
        c = new Color(r,g,b);
        
        clr = c.getRGB();
        colors.set(region-1,new int[]{avg[0],avg[1]+1,clr});
    }
    public int growRegion(){
        int rgbRed = 255;
        rgbRed = (rgbRed << 8) + 0;
        rgbRed = (rgbRed << 8) + 0;
        System.out.println(regions[50][50]);
        boolean grown = true;
        
        while(grown){
        grown = false;
        ArrayList<int[]> newSeeds = new ArrayList<>();
        
        for(int[] seed: seeds){
            
            int x = seed[0];
            int y = seed[1];
            int region = regions[x][y];
            
            
            
            for(int i = -1; i<2; i++){
                for(int j = -1; j<2; j++){
                    
                    int tx = x+i;
                    int ty = y+j;
                    
                    if((tx>=0 && tx<image.getWidth())&&(ty>=0 && ty<image.getHeight())){
                       
                        if((regions[tx][ty]==0)){
                            
                            final int clr = image.getRGB(tx, ty);
                            final int red = (clr & 0x00ff0000) >> 16;
                            final int green = (clr & 0x0000ff00) >> 8;
                            final int blue = clr & 0x000000ff;
                            
                            final int regionclr = colors.get(region-1)[0];
                            final int rRed = (regionclr & 0x00ff0000) >> 16;
                            final int rGreen = (regionclr & 0x0000ff00) >> 8;
                            final int rBlue = regionclr & 0x000000ff;
                            
                            if((red<rRed+threshold&&red>rRed-threshold)&&(green<rGreen+threshold&&green>rGreen-threshold)&&(blue<rBlue+threshold&&blue>rBlue-threshold)){
                                //System.out.println("hit");
                                modColor(region,clr);
                                regions[tx][ty] = region;
                                //image.setRGB(tx, ty, colors.get(region-1)[0]);
                                newSeeds.add(new int[]{tx,ty});
                                grown = true;
                            }
                            
                        }
                        else if(regions[tx][ty]!=regions[x][y]){
                            int r = regions[tx][ty];
                            int c = regions[x][y];
                            links[r-1][c-1] = 1;
                            //System.out.println(r+" linked with "+c);
                        }
                    }
                    
                }
            }
            
            
            
        }
        seeds = newSeeds;   
        }
        
        for(int x = 0; x<regions.length; x++)
            for(int y = 0; y<regions[0].length; y++){
                
                    if(regions[x][y]!=0){
                        image.setRGB(x, y, colors.get(regions[x][y]-1)[2]);
                    }
                    
                    else{
                        image.setRGB(x, y, 0);
                    }
                    //mergeImage.setRGB(x, y, mergedColors.get(regions[x][y]));
                    


            
            }
        

        
        ArrayList<Integer> singleRegions = new ArrayList<>();
        
        for(int x=0; x<links.length; x++){
            boolean b = false;
            for(int y=0; y<links[0].length; y++){
                if(links[x][y]!=0){
                   
                    
                      final int clr = colors.get(x)[2];
                      final int red = (clr & 0x00ff0000) >> 16;
                      final int green = (clr & 0x0000ff00) >> 8;
                      final int blue = clr & 0x000000ff;
                            
                      final int regionclr = colors.get(y)[2];
                      final int rRed = (regionclr & 0x00ff0000) >> 16;
                      final int rGreen = (regionclr & 0x0000ff00) >> 8;
                      final int rBlue = regionclr & 0x000000ff;
                    
                      if(!((red<rRed+mergeThreshold&&red>rRed-mergeThreshold)&&(green<rGreen+mergeThreshold&&green>rGreen-mergeThreshold)&&(blue<rBlue+mergeThreshold&&blue>rBlue-mergeThreshold))){
                          links[x][y] = 0;
                          links[y][x] = 0;
                          
                          
                           
                      }
                      else{
                          b = true;
                          //links[x][y] = 1;
                          //links[y][x] = 0;
                          //System.out.println(x+" linked with "+y);
                      }
                      
                    
                    
                }
            }
            if(!b){
                singleRegions.add(x);
            }
        }
        /*
        for(int x = 0; x<regions.length; x++)
            for(int y = 0; y<regions[0].length; y++){
                
                    if(regions[x][y]!=0){
                        
                        if(regions[x][y]==1){
                            mergeImage.setRGB(x, y, colors.get(regions[x][y]-1)[2]);
                            
                        }
                            
                        else if(links[0][regions[x][y]-1]==1){
                            mergeImage.setRGB(x, y, colors.get(regions[x][y]-1)[2]);
                        }
                        else
                            mergeImage.setRGB(x, y, 0);
                    }
                    else
                        mergeImage.setRGB(x, y, 0);
                
            }
        */
        ArrayList<int[]> newRegions = new ArrayList<>();
        for(int i=1; i<links.length; i++){
            ArrayList<Integer> debug = new ArrayList<>();
            debug.add(i);
            merge(i, debug);
            int[] array = new int[debug.size()];
            for(int j =0; j<debug.size(); j++){
                array[j] = debug.get(j);
            }
            if(debug.size()==1){
                if(!singleRegions.contains(i)){
                    newRegions.add(array);
                }
            }
            else
                newRegions.add(array);
        }
        System.out.println(newRegions.size());
        /*for(int i = 0; i<newRegions.size(); i++){
            System.out.println(Arrays.toString(newRegions.get(i)));
        }*/
        ArrayList<Integer> mergedColors = new ArrayList<>();
        //Color co = new Color(255,255,255);
        //mergedColors.add(co.getRGB());
        //System.out.println("hello");
        
        for(int x = 0; x<regions.length; x++){
            for(int y = 0; y<regions[0].length; y++){
                
                int n = regions[x][y];
                int index = 0;
                boolean b = false;
                
                for(int i = 0; i<newRegions.size(); i++){
                    int[] array = newRegions.get(i);
                    for(int j = 0; j<array.length; j++){
                        if(array[j]==n){
                            
                            index = i;
                            b = true;
                            break;
                        }
                    }
                    if(b)
                        break;
                    
                }
                
                if(!b){
                    regions[x][y] = 0;
                }
                else{
                    regions[x][y] = index+1;
                }
                if(regions[x][y]==0){
                    int hth = 5;
                }
            }
        }
        
        
        for(int i = 0; i<newRegions.size(); i++){
            
            int[] array = newRegions.get(i);
            int tR = 0;
            int tG = 0;
            int tB = 0;
            
            for(int j =0; j<array.length; j++){
                if(array[j]!=0){
                int clr = colors.get(array[j]-1)[2];
                Color c = new Color(clr);
                tR += c.getRed();
                tG += c.getGreen();
                tB += c.getBlue();
                }
                
            }
            
            tR = tR/array.length;
            tG = tG/array.length;
            tB = tB/array.length;
            
            Color c = new Color(tR, tG, tB);
            
            mergedColors.add(c.getRGB());
        }
        
        

        
        /*ArrayList<Integer> temp = new ArrayList<>();
        for(int x = 0; x<regions.length; x++)
            for(int y = 0; y<regions[0].length; y++){
                
                    if(regions[x][y]!=0){
                        
                        boolean b = false;
                        for(int r : newRegions.get(0)){
                            
                            if(regions[x][y]==r){
                                b = true;
                                if(!temp.contains(r)){
                                    temp.add(r);
                                }
                            }
                        }
                        if(b){
                            mergeImage.setRGB(x, y, rgbRed);
                            
                        }
                        
                            
                        //else if(links[0][regions[x][y]-1]==1){
                       //     mergeImage.setRGB(x, y, colors.get(regions[x][y]-1)[2]);
                        //}
                        else
                            mergeImage.setRGB(x, y, 0);
                        if(regions[x][y]==1){
                            mergeImage.setRGB(x, y, Color.GREEN.getRGB());
                        }
                    }
                    else
                        mergeImage.setRGB(x, y, 0);
                
            }
        
        System.out.println(temp);*/
        
        for(int x = 0; x<regions.length; x++)
            for(int y = 0; y<regions[0].length; y++){
                
                    if(regions[x][y]!=0){
                        mergeImage.setRGB(x, y, mergedColors.get(regions[x][y]-1));
                    }
                    
                    else{
                        mergeImage.setRGB(x, y, 0);
                    }
                    //mergeImage.setRGB(x, y, mergedColors.get(regions[x][y]));
                    


            }
        
      
        
        mColors = mergedColors;
        return mergedColors.size();
        /*
         for(int x = 0; x<regions.length; x++)
            for(int y = 0; y<regions[0].length; y++){

                    if(regions[x][y]!=0){
                        
                        if(regions[x][y]==1){
                            mergeImage.setRGB(x, y, Color.GREEN.getRGB());
                            
                        }
                            
                        else if(links[0][regions[x][y]-1]==1){
                            //System.out.println(regions[x][y]-1);
                           // mergeImage.setRGB(x, y, Color.BLUE.getRGB());
                        }
                        
                    }
                    if(regions[x][y]==0){
                            //mergeImage.setRGB(x, y, Color.WHITE.getRGB());
                            
                     }

                
            }
        
        */

        
        
        
    }
    public void merge(int n, ArrayList<Integer> list){
        ArrayList<Integer> temp = new ArrayList<>();
        
            for(int y = 0; y<links.length; y++){
                if(links[n][y]!=0){
                    list.add(y+1);
                    temp.add(y);
                    links[n][y]=0;
                }
            }
         
        for(Integer i: temp){
             merge(i,list);
        }
         
    }
    public int[][] returnRegions(){
        return regions;
    } 
    public void testImage(){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        
           try {
            ImageIO.write(image, "jpg", new File("mainout/um"+filename));
        } catch (IOException ex) {
            Logger.getLogger(ImageSegmentation.class.getName()).log(Level.SEVERE, null, ex);
        }
        ImageIcon img = new ImageIcon(image);
        
        JLabel imgLabel = new JLabel(img);
        
        frame.add(imgLabel);
        frame.setVisible(true);
        
        frame.setSize(image.getWidth(), image.getHeight());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void testMergeImage(){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        try {
            ImageIO.write(mergeImage, "jpg", new File("mainout/m"+filename));
        } catch (IOException ex) {
            Logger.getLogger(ImageSegmentation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ImageIcon img = new ImageIcon(mergeImage);
        
        JLabel imgLabel = new JLabel(img);
        
        frame.add(imgLabel);
        frame.setVisible(true);
        
        frame.setSize(image.getWidth(), image.getHeight());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
