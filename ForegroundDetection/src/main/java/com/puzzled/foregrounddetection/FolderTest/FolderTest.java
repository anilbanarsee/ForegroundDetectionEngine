/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puzzled.foregrounddetection.FolderTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Reetoo
 */
public class FolderTest {
    public static void main(String[] args) throws IOException{
        File folder = new File("assets//images");
    for (final File fileEntry : folder.listFiles()) {

            System.out.println(fileEntry.getPath());
        }
    }
    
}
