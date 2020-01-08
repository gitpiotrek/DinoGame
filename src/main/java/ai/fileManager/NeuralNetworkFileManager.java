package ai.fileManager;

import ai.communication.NodeInput;
import ai.neural.NeuralNetwork;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collector;

public class NeuralNetworkFileManager {

    private File file = null;

    public NeuralNetworkFileManager(File file){
        this.file = file;
    }

    public void writeNeuralNetworkToFile(File dest) throws IOException {

        NeuralNetwork neuralNetwork = new NeuralNetwork();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dest));
        NodeInput nodeInput;
        String row;
        StringBuilder stringBuilder = new StringBuilder();

        long numberLinesToRemove = 30;
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
        stringBuilder.replace(stringBuilder.length()-1, stringBuilder.length(),"");
        bufferedWriter.write(stringBuilder.toString());
        bufferedWriter.close();
        bufferedReader.close();
    }

    public NeuralNetwork loadNeuralNetwork() throws IOException {
        NeuralNetwork neuralNetwork = new NeuralNetwork();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        Double[] data = Arrays.stream(bufferedReader.readLine().split(","))
                .map(Double::parseDouble).toArray(Double[]::new);
        neuralNetwork.setSynapsesWeights(data);
        bufferedReader.close();
        return neuralNetwork;
    }
}