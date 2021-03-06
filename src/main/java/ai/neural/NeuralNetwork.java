package ai.neural;

import ai.communication.NodeInput;
import ai.communication.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeuralNetwork {

    private List<Synapse> firstLayerSynapses = new ArrayList<>();
    private List<Synapse> secondLayerSynapses = new ArrayList<>();
    private List<Neuron> inputLayer = new ArrayList<>();
    private List<Neuron> hiddenLayer = new ArrayList<>();
    private List<Neuron> outputLayer = new ArrayList<>();
    private List<List<Synapse>> network = new ArrayList<List<Synapse>>();

    private Neuron biasInputLayer = new Neuron();
    private Neuron biasHiddenLayer = new Neuron();

    public NeuralNetwork() {
        initializeNetwork(7, 6, 4);
    }

    private void initializeNetwork(int inputLayerSize, int hiddenLayerSize, int outputLayerSize) {

        biasInputLayer.setInputValue(1);
        biasHiddenLayer.setInputValue(1);

        for (int i = 0; i < inputLayerSize; i++) {
            inputLayer.add(new Neuron());
        }
        for (int i = 0; i < hiddenLayerSize; i++) {
            hiddenLayer.add(new Neuron());
        }
        for (int i = 0; i < outputLayerSize; i++) {
            outputLayer.add(new Neuron());
        }

        Synapse temp;
        for (Neuron n : inputLayer) {
            for (Neuron k : hiddenLayer) {
                temp = new Synapse(n, k);
                firstLayerSynapses.add(temp);
                n.addOutputSynapse(temp);
                k.addInputSynapse(temp);
            }
        }
        // hiddenLayer.add(biasInputLayer);

        for (Neuron n : hiddenLayer) {
            for (Neuron k : outputLayer) {
                temp = new Synapse(n, k);
                secondLayerSynapses.add(temp);
                n.addOutputSynapse(temp);
                k.addInputSynapse(temp);
            }
        }
        // outputLayer.add(biasHiddenLayer);

        network.add(firstLayerSynapses);
        network.add(secondLayerSynapses);
    }

    private void transferValueFromSenderNeutron(Synapse s) {
        s.getSenderNeuron().activationFunction();
        s.transferValue();
    }

    private void setInputs(NodeInput nodeInput) {
        int i = 0;
        double[] data = nodeInput.toInputData();
        for (Neuron neuron : inputLayer) {
            neuron.setInputValue(data[i]);
            i++;
        }
    }

    private void forwardPropagation() {

        for (List<Synapse> e : network) {
            for (Synapse s : e) {
                transferValueFromSenderNeutron(s);
            }
        }

        for (Neuron neuron : outputLayer) {
            neuron.activationFunction();
        }
    }

    private void backPropagation(NodeInput nodeInput) {

        double[] values = outputValues(nodeInput);

        for (int i = 0; i < outputLayer.size(); i++) {
            outputLayer.get(i).deltaOutputLayer(values[i]);
        }

        for (Neuron neuron : hiddenLayer) {
            neuron.deltaHiddenLayer();
        }

        for (Neuron neuron : inputLayer) {
            neuron.deltaHiddenLayer();
        }

        for (List<Synapse> e : network) {
            for (Synapse s : e) {
                s.updateWeightValue();
            }
        }
    }

    public double train(NodeInput nodeInput) {
        double middleError = 0;
        setInputs(nodeInput);
        forwardPropagation();
        try {
            if (nodeInput.getState() != getStateResponse(nodeInput)) {
                backPropagation(nodeInput);
            }
            if (getStateResponse(nodeInput) == State.RUN) {
                middleError=(Math.pow((1-outputLayer.get(0).getOutputValue()),2) + Math.pow(outputLayer.get(1).getOutputValue(),2)+ Math.pow(outputLayer.get(2).getOutputValue(),2) + Math.pow(outputLayer.get(3).getOutputValue(),2));
            } else if (getStateResponse(nodeInput) == State.JUMP) {
                middleError=(Math.pow((1-outputLayer.get(1).getOutputValue()),2) + Math.pow(outputLayer.get(0).getOutputValue(),2) + Math.pow(outputLayer.get(2).getOutputValue(),2) + Math.pow(outputLayer.get(3).getOutputValue(),2));
            } else if (getStateResponse(nodeInput) == State.SMALL_JUMP) {
                middleError=(Math.pow((1-outputLayer.get(2).getOutputValue()),2) + Math.pow(outputLayer.get(0).getOutputValue(),2) + Math.pow(outputLayer.get(1).getOutputValue(),2) + Math.pow(outputLayer.get(3).getOutputValue(),2));
            }else if (getStateResponse(nodeInput) == State.DUCK) {
                middleError=(Math.pow((1-outputLayer.get(3).getOutputValue()),2) + Math.pow(outputLayer.get(0).getOutputValue(),2) + Math.pow(outputLayer.get(1).getOutputValue(),2) + Math.pow(outputLayer.get(2).getOutputValue(),2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return middleError;
    }

    public void trainWithShuffledData(List<NodeInput> dataToTrain) {
      //  int iterationToTeach = (10000 / dataToTrain.size()) + 100;

        double middleError = 0;
        double previousMiddleError = Double.MAX_VALUE;
        double currentMinimalError = Double.MAX_VALUE;;
        double[] currentWeightsOfMinimalError = new double[0];
        boolean firstTime = true;
        while(middleError<previousMiddleError){
            if(!firstTime){
                previousMiddleError = middleError;
            }

            middleError = 0;
            for(int i = 0;i<100;i++){
                Collections.shuffle(dataToTrain);
                for(NodeInput node: dataToTrain){
                    middleError += this.train(node);
                }
                if(middleError < currentMinimalError){
                    currentMinimalError = middleError;
                    currentWeightsOfMinimalError = this.getSynapsesWeights();
                }
                /*
        for (NodeInput node : dataToTrain) {
            this.train(node);
        }

        for (int j = 0; j < iterationToTeach; j++) {
            Collections.shuffle(dataToTrain);
            for (NodeInput node : dataToTrain) {
                this.train(node);
            }
        }

                 */
    }
        firstTime = false;}}

    private double[] outputValues(NodeInput nodeInput) throws IllegalStateException {
        double[] outputValue;
        switch (nodeInput.getState()) {
            case RUN:
                outputValue = new double[]{1.0, 0, 0, 0};
                break;
            case JUMP:
                outputValue = new double[]{0, 1.0, 0, 0};
                break;
            case SMALL_JUMP:
                outputValue = new double[]{0, 0, 1.0, 0};
                break;
            case DUCK:
                outputValue = new double[]{0, 0, 0, 1.0};
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + nodeInput.getState());
        }
        return outputValue;
    }

    public void setSynapsesWeights(Double[] weightData) {
        int i = 0;
        for (Synapse synapse : firstLayerSynapses) {
            synapse.setWeight(weightData[i]);
            i++;
        }

        for (Synapse synapse : secondLayerSynapses) {
            synapse.setWeight(weightData[i]);
            i++;
        }
    }

    public double[] getSynapsesWeights() {
        int i = 0;
        double[] weightData = new double[firstLayerSynapses.size() + secondLayerSynapses.size()];
        for (Synapse synapse : firstLayerSynapses) {
            weightData[i] = synapse.getWeight();
            i++;
        }

        for (Synapse synapse : secondLayerSynapses) {
            weightData[i] = synapse.getWeight();
            i++;
        }
        return weightData;
    }

    private double[] getOutputData(NodeInput nodeInput) {
        double[] outputData = new double[outputLayer.size()];
        setInputs(nodeInput);
        forwardPropagation();
        for (int i = 0; i < outputLayer.size(); i++) {
            outputData[i] = outputLayer.get(i).getOutputValue();
        }
        return outputData;
    }

    public State getStateResponse(NodeInput nodeInput) throws Exception {
        double[] outputData = getOutputData(nodeInput);
        double max = outputData[0];
        for (int i = 1; i < outputData.length; i++) {
            if (outputData[i] > max) max = outputData[i];
        }
        if (outputData[0] == max) {
            return State.RUN;
        } else if (outputData[1] == max) {
            return State.JUMP;
        } else if (outputData[2] == max) {
            return State.SMALL_JUMP;
        } else if (outputData[3] == max) {
            return State.DUCK;
        } else throw new Exception();
    }
}