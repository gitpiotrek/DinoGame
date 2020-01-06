package ai.fileMenager;

import ai.communication.NodeInput;
import ai.neural.NeuralNetwork;

import java.io.*;
import java.nio.file.Files;

public class NeuralNetworkFileWriter {

    private NeuralNetwork neuralNetwork = new NeuralNetwork();;
    private File file = null;
    private long numberLinesToRemove = 30;

    public NeuralNetworkFileWriter(File file){
        this.file = file;
    }

    public void writeNeuralNetworkToFile(File dest) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dest));
        NodeInput nodeInput;
        String row;
        StringBuilder stringBuilder = new StringBuilder();

       long linesToTrain = Files.lines(file.toPath()).count() - numberLinesToRemove;

        for(int i = 0; i < linesToTrain; i++){
            row = bufferedReader.readLine();
            nodeInput = new NodeInput(row);
            neuralNetwork.train(nodeInput);
        }
        double[] synapsesWeights = neuralNetwork.getSynapsesWeights();
        for(double synapseWeight : synapsesWeights){
            stringBuilder.append(synapseWeight);
            stringBuilder.append(",");
        }
        bufferedWriter.write(stringBuilder.toString());
        bufferedWriter.close();
        bufferedReader.close();
    }
}