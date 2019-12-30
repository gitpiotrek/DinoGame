package neural;

import ai.communication.State;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeuralNetwork {
    // Distance to next obstacle
    // Height of obstacle
    //Width of obstacle
    //Bird height
    //Speed
    //Players Y position
    //Gap between obstacles
    private List<List> neuronTrainingData = new ArrayList<>();
    private List<Double> distanceToNextObstacle = new ArrayList<>();
    private List<Double> heightOfObstacle = new ArrayList<>();
    private List<Double> widthOfObstacle = new ArrayList<>();
    private List<Double> playerYPosition = new ArrayList<>();
    private List<Double> velocity = new ArrayList<>();
    private List<Double> pterodactylHeight = new ArrayList<>();
    private List<Double> distanceBetweenObstacles = new ArrayList<>();
    private List<State> state = new ArrayList<>();

    List<Synapse> firstLayerSynapses = new ArrayList<>();
    List<Synapse> secondLayerSynapses = new ArrayList<>();
    List<Neuron> inputLayer = new ArrayList<>();
    List<Neuron> hiddenLayer = new ArrayList<>();
    List<Neuron> outputLayer = new ArrayList<>();
    List<List<Synapse>> network = new ArrayList<List<Synapse>>();
    private List<List> data;

    Neuron biasInputLayer = new Neuron();
    Neuron biasHiddenLayer = new Neuron();



    public void initializeNetwork(int inputLayerSize, int hiddenLayerSize, int outputLayerSize){
        neuronTrainingData.add(distanceToNextObstacle);
        neuronTrainingData.add(heightOfObstacle);
        neuronTrainingData.add(widthOfObstacle);
        neuronTrainingData.add(playerYPosition);
        neuronTrainingData.add(velocity);
        neuronTrainingData.add(pterodactylHeight);
        neuronTrainingData.add(distanceBetweenObstacles);
        neuronTrainingData.add(state);



        for(int i=0;i<inputLayerSize;i++){
            inputLayer.add(new Neuron());
        }
        for(int i=0;i<hiddenLayerSize;i++){
            hiddenLayer.add(new Neuron());
        }
        for(int i=0;i<outputLayerSize;i++){
            outputLayer.add(new Neuron());
        }

        inputLayer.add(biasInputLayer);
        Synapse temp;
        for(Neuron n:inputLayer){
            for(Neuron k:hiddenLayer){
                temp =  new Synapse(n,k);
                firstLayerSynapses.add(temp);
                n.addOutputSynapse(temp);
                k.addInputSynapse(temp);
            }
        }
        hiddenLayer.add(biasHiddenLayer);
        for(Neuron n:hiddenLayer){
            for(Neuron k:outputLayer){
                temp =  new Synapse(n,k);
                secondLayerSynapses.add(temp);
                n.addOutputSynapse(temp);
                k.addInputSynapse(temp);
            }
        }
        biasInputLayer.setOutputValue(1);
        biasHiddenLayer.setOutputValue(1);
        //outputLayer.get(outputLayer.size()-1).deltaHiddenLayer();
        //hiddenLayer.get(hiddenLayer.size()-1).deltaHiddenLayer();
        // dlaczego?

        network.add(firstLayerSynapses);
        network.add(secondLayerSynapses);

    }
    public void transferValueFromSenderNeutron(Synapse s){
        if(s.getSenderNeuron() != biasInputLayer && s.getSenderNeuron() != biasHiddenLayer){
            s.getSenderNeuron().activationFunction();
        }
        s.transferValue();
    }

    public int forwardPropagation(){


        for(List<Synapse> e:network){
            for(Synapse s: e){
                transferValueFromSenderNeutron(s);
            }
        }

        int k = 0;
        for(Neuron neuron: outputLayer){
            neuron.activationFunction();
         //    System.out.println("Output "+k+" value: "  + neuron.getOutputValue());
            k++;
        }

        return getMaxiumumOutputIndex();

    }
    public int getMaxiumumOutputIndex(){
        if(outputLayer.get(0).getOutputValue()>outputLayer.get(1).getOutputValue() && outputLayer.get(0).getOutputValue() > outputLayer.get(2).getOutputValue()){
                return 0;
        }else if(outputLayer.get(1).getOutputValue()>outputLayer.get(0).getOutputValue() && outputLayer.get(1).getOutputValue() > outputLayer.get(2).getOutputValue()){
                return 1;
        }else if(outputLayer.get(2).getOutputValue()>outputLayer.get(1).getOutputValue() && outputLayer.get(2).getOutputValue() > outputLayer.get(0).getOutputValue()){
                return 2;
        }else{
            return 4;
        }
    }
    public void setInputs(Double[] inputs){
        int i=0;
        for(Neuron neuron: inputLayer){
            if(i<inputLayer.size()-1) {
                neuron.setInputValue(inputs[i]);
            }
            i++;
        }
    }

    public void backPropagation(double[] values){

        for(int i=0;i<outputLayer.size();i++){
            outputLayer.get(i).deltaOutputLayer(values[i]);
        }
        for(Neuron neuron: hiddenLayer){
            neuron.deltaHiddenLayer();
        }

        for(Neuron neuron: inputLayer){
            neuron.deltaHiddenLayer();
        }


        //bias.deltaHiddenLayer();

        for(List<Synapse> e:network){
            for(Synapse s: e){
                s.updateWeightValue();
            }
        }
    }
    public void train(){
        //prepareData();
        FileWriter csvWriter = null;
        try {
            csvWriter = new FileWriter("new.csv", false);
            for(int i=0;i<neuronTrainingData.get(0).size();i++) {
                for (List a : neuronTrainingData) {
                    csvWriter.write(a.get(i) + ",");
                }
                csvWriter.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            csvWriter.flush();
            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Double[] values = new Double[neuronTrainingData.get(0).size()-1];
        boolean changed = true;
        int iter = 0;
        int MAX_ITERS = 1000000000;
        int numberOfChanged = 0;
        while(changed && iter < MAX_ITERS || numberOfChanged < 14610){
            numberOfChanged =0;
            changed = false;
            shuffleData();
        for(int i=0;i<neuronTrainingData.get(0).size() ;i++) {
            for (int k = 0; k < neuronTrainingData.size() - 1; k++) {
                values[k] = (Double) neuronTrainingData.get(k).get(i % neuronTrainingData.get(0).size());
            }
            setInputs(values);
            forwardPropagation();
            if (("RUN".equals((String) neuronTrainingData.get(7).get(i % neuronTrainingData.get(0).size()))) && (outputLayer.get(0).getOutputValue() < outputLayer.get(1).getOutputValue() || outputLayer.get(0).getOutputValue() < outputLayer.get(2).getOutputValue())) {
                backPropagation(new double[]{1.0, 0, 0});
                changed = true;
            } else if (("JUMP".equals((String) neuronTrainingData.get(7).get(i % neuronTrainingData.get(0).size()))) && (outputLayer.get(1).getOutputValue() < outputLayer.get(0).getOutputValue() || outputLayer.get(1).getOutputValue() < outputLayer.get(2).getOutputValue())) {
                backPropagation(new double[]{0, 1.0, 0});
                changed = true;
            } else if (("DUCK".equals((String) neuronTrainingData.get(7).get(i % neuronTrainingData.get(0).size()))) && (outputLayer.get(2).getOutputValue() < outputLayer.get(0).getOutputValue() || outputLayer.get(2).getOutputValue() < outputLayer.get(1).getOutputValue())) {
                backPropagation(new double[]{0, 0, 1.0});
                changed = true;
            }else{
                numberOfChanged++;
            }
            iter++;
        }

           /* for(Synapse synapse:inputLayer.get(0).getOutputSynapses()){
                System.out.println(synapse.getWeight());
            }
            */
            System.out.println("["+numberOfChanged+"/"+neuronTrainingData.get(0).size()+"]");
        }

        }
    public void loadTrainData(){
        String row;
        String[] line= new String[8];
        data = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("new.csv"));
            int k=0;
            while((row = br.readLine())!=null){
                line = row.split(",");
                for(int i=0;i<line.length;i++){
                    if(i!=7){
                        neuronTrainingData.get(i).add(Double.parseDouble(line[i]));
                    }else
                        neuronTrainingData.get(7).add(line[7]);
                }
                k++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void prepareData(){
        for(int i=0;i<neuronTrainingData.get(0).size();i++){
            for(int k=i+1;k<neuronTrainingData.get(0).size();k++){
                    if(     neuronTrainingData.get(0).get(i).equals(neuronTrainingData.get(0).get(k)) &&
                            neuronTrainingData.get(1).get(i).equals(neuronTrainingData.get(1).get(k)) &&
                            neuronTrainingData.get(2).get(i).equals(neuronTrainingData.get(2).get(k)) &&
                            neuronTrainingData.get(3).get(i).equals(neuronTrainingData.get(3).get(k)) &&
                            neuronTrainingData.get(4).get(i).equals(neuronTrainingData.get(4).get(k)) &&
                            neuronTrainingData.get(5).get(i).equals(neuronTrainingData.get(5).get(k)) &&
                            neuronTrainingData.get(6).get(i).equals(neuronTrainingData.get(6).get(k)) &&
                            neuronTrainingData.get(7).get(i).equals(neuronTrainingData.get(7).get(k))
                    ){

                        neuronTrainingData.get(0).remove(k);
                        neuronTrainingData.get(1).remove(k);
                        neuronTrainingData.get(2).remove(k);
                        neuronTrainingData.get(3).remove(k);
                        neuronTrainingData.get(4).remove(k);
                        neuronTrainingData.get(5).remove(k);
                        neuronTrainingData.get(6).remove(k);
                        neuronTrainingData.get(7).remove(k);

                        k--;
                        System.out.println("Removed: "+k);
                    }

            }
        }

    }
    public void shuffleData(){
        Object temp;
        int random;
        for(int i=0;i<neuronTrainingData.size()/2;i++){
            random = (int) Math.random()*neuronTrainingData.size();
            for(List l:neuronTrainingData){
                temp = l.get(random);
                l.remove(random);
                l.add(temp);
            }

        }

    }
    }
