/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foregrounddetection;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;

/**
 *
 * @author Reetoo
 */
public class LearnOR {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NeuralNetwork neuralNetwork = new Perceptron(2, 1);
        
        DataSet tSet = new DataSet(2, 1);
        
        tSet.addRow(new DataSetRow(new double[]{0, 0, 0}, new double[]{1}));

        
        neuralNetwork.learn(tSet);
        
        neuralNetwork.save("or_perceptron.nnet");
    }
    
}
