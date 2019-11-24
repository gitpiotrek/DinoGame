package neural;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetwork {
    // Distance to next obstacle
    // Height of obstacle
    //Width of obstacle
    //Bird height
    //Speed
    //Players Y position
    //Gap between obstacles

    List<Synapse> firstLayerSynapses = new ArrayList<>();
    List<Synapse> secondLayerSynapses = new ArrayList<>();
    List<Neuron> inputLayer = new ArrayList<>();
    List<Neuron> hiddenLayer = new ArrayList<>();
    List<Neuron> outputLayer = new ArrayList<>();
    List<List<Synapse>> network = new ArrayList<List<Synapse>>();


    Neuron bias = new Neuron();



    public void initializeNetwork(int inputLayerSize, int hiddenLayerSize, int outputLayerSize){

        bias.setInputValue(1);

        for(int i=0;i<inputLayerSize;i++){
            inputLayer.add(new Neuron());
        }
        for(int i=0;i<hiddenLayerSize;i++){
            hiddenLayer.add(new Neuron());
        }
        for(int i=0;i<outputLayerSize;i++){
            outputLayer.add(new Neuron());
        }
        hiddenLayer.add(bias);
        outputLayer.add(bias);
        Synapse temp;
        for(Neuron n:inputLayer){
            for(Neuron k:hiddenLayer){
                temp =  new Synapse(n,k);
                firstLayerSynapses.add(temp);
                n.addOutputSynapse(temp);
                k.addInputSynapse(temp);
            }
        }
        for(Neuron n:hiddenLayer){
            for(Neuron k:outputLayer){
                temp =  new Synapse(n,k);
                secondLayerSynapses.add(temp);
                n.addOutputSynapse(temp);
                k.addInputSynapse(temp);
            }
        }
        outputLayer.get(outputLayer.size()-1).deltaHiddenLayer();
        hiddenLayer.get(hiddenLayer.size()-1).deltaHiddenLayer();


        network.add(firstLayerSynapses);
        network.add(secondLayerSynapses);

    }
    public void transferValueFromSenderNeutron(Synapse s){
        s.getSenderNeuron().activationFunction();
        s.transferValue();
    }

    public void forwardPropagation(){


        for(List<Synapse> e:network){
            for(Synapse s: e){
                transferValueFromSenderNeutron(s);
            }
        }

        int k = 0;
        for(Neuron neuron: outputLayer){
            neuron.activationFunction();
            System.out.println("Output "+k+" value: "  + neuron.getOutputValue());
        }

    }
    public void setInputs(double[] inputs){
        int i=0;
        for(Neuron neuron: inputLayer){
            neuron.setInputValue(inputs[i]);
            i++;
        }
    }

    public void backPropagation(double[] values){

        for(int i=0;i<outputLayer.size()-1;i++){
            outputLayer.get(i).deltaOutputLayer(values[i]);
        }
        for(Neuron neuron: hiddenLayer){
            neuron.deltaHiddenLayer();
        }
        /*
        for(Neuron neuron: inputLayer){
            neuron.deltaHiddenLayer();
        }
        *
         */

        bias.deltaHiddenLayer();

        for(List<Synapse> e:network){
            for(Synapse s: e){
                s.updateWeightValue();
            }
        }
    }
    public void train(double[] values){
        boolean more = true;
        while(true) {

                if (outputLayer.get(0).getOutputValue() > (values[0]-0.05) &&outputLayer.get(0).getOutputValue() < (values[0]
                +0.05)
                && outputLayer.get(1).getOutputValue() > (values[1]-0.05) &&outputLayer.get(1).getOutputValue() < (values[1]
                        +0.05)
                && outputLayer.get(2).getOutputValue() > (values[2]-0.05) &&outputLayer.get(2).getOutputValue() < (values[2]
                        +0.05)
                ){
                   break;
                }else{
                    forwardPropagation();
                    backPropagation(values);
                }

            }
        }

    }

