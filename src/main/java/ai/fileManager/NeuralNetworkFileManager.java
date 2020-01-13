package ai.fileManager;

import ai.communication.NodeInput;
import ai.neural.NeuralNetwork;
import game.controllers.MenuController;
import javafx.concurrent.Task;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class NeuralNetworkFileManager{

    private File file = null;

    public NeuralNetworkFileManager(File file) {
        this.file = file;
    }

    void writeNeuralNetworkToFile(File dest) throws IOException {

        NeuralNetwork neuralNetwork = new NeuralNetwork();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dest));
        NodeInput nodeInput;
        String row;
        StringBuilder stringBuilder = new StringBuilder();

        long numberLinesToRemove = 100;
        long linesToTrain = Files.lines(file.toPath()).count() - numberLinesToRemove;
        ArrayList<NodeInput> dataToTrain = new ArrayList<>();
        int iterationToTeach;

        for (int i = 0; i < linesToTrain; i++) {
            row = bufferedReader.readLine();
            nodeInput = new NodeInput(row);
            dataToTrain.add(nodeInput);
            //neuralNetwork.train(nodeInput);
        }

        neuralNetwork.trainWithShuffledData(dataToTrain);

        double[] synapsesWeights = neuralNetwork.getSynapsesWeights();
        for (double synapseWeight : synapsesWeights) {
            stringBuilder.append(synapseWeight);
            stringBuilder.append(",");
        }
        stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");
        bufferedWriter.write(stringBuilder.toString());
        bufferedWriter.close();
        bufferedReader.close();
        MenuController.selectedNeuralFile = dest;
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