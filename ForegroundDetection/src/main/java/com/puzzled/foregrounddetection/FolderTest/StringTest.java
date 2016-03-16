/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzzled.foregrounddetection.FolderTest;

/**
 *
 * @author Reetoo
 */
public class StringTest {
    public static void main(String[] args){
        String imagePath = "assets//images//3002154.jpg";
        String regionPath = imagePath.substring(16,imagePath.length()-4);

        System.out.println(regionPath);
    }
}
