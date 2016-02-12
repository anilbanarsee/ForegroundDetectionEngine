/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foregrounddetection;

import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;

/**
 *
 * @author Reetoo
 */
public class TestNetwork {
    public static void main(String[] args){
        
        NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile("or_perceptron.nnet");
        
        neuralNetwork.setInput(1, 1);
        neuralNetwork.calculate();
        
        double[] networkOutput = neuralNetwork.getOutput();
        
        System.out.println(Arrays.toString(networkOutput));
        
    }
}
