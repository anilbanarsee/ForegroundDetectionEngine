/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzzled.foregrounddetection;

/**
 *
 * @author User
 */
public class Utils {
    public static String convertToRegionFile(String filePath){
        String code = filePath.substring(19,filePath.length()-4);
        return "assets//labels//"+code+".regions.txt";
    }
}
