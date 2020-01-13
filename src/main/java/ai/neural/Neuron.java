package ai.neural;

import java.util.ArrayList;
import java.util.List;

public class Neuron {

    private int id;
    private double inputValue;
    private boolean isInputSet = false;
    private double outputValue;
    private List<Synapse> inputSynapses = new ArrayList<>();
    private List<Synapse> outputSynapses = new ArrayList<>();
    private double delta;

    private static int nextId = 1;

    public Neuron() {
        this.id = nextId;
        nextId++;
    }

    public double getDelta() {
        return delta;
    }

    public void addInputSynapse(Synapse s) {
        inputSynapses.add(s);
    }

    public void addOutputSynapse(Synapse s) {
        outputSynapses.add(s);
    }

    public int getId() {
        return id;
    }

    public double getInputValue() {
        return inputValue;
    }

    public void setInputValue(double inputValue) {
        isInputSet = true;
        this.inputValue = inputValue;
    }

    public double getOutputValue() {
        return outputValue;
    }

    public void setOutputValue(double outputValue) {
        this.outputValue = outputValue;
    }

    public List<Synapse> getInputSynapses() {
        return inputSynapses;
    }

    public void setInputSynapses(List<Synapse> inputSynapses) {
        this.inputSynapses = inputSynapses;
    }

    public List<Synapse> getOutputSynapses() {
        return outputSynapses;
    }

    public void setOutputSynapses(List<Synapse> outputSynapses) {
        this.outputSynapses = outputSynapses;
    }

    public void activationFunction() {
        if (!isInputSet) {
            double value = 0;
            for (Synapse s : inputSynapses) {
                value += (s.getWeight() * s.getValue());
            }
            outputValue = (1 / (1 + Math.exp(-value)));
        } else {
            outputValue = inputValue;
        }
    }

    void deltaOutputLayer(Double correctValue) {
        this.delta = (correctValue - outputValue) * derivative();
    }

    private double derivative() {
        return outputValue * (1 - outputValue);
    }

    public void deltaHiddenLayer() {
        double temp = 0;
        for (Synapse e : outputSynapses) {
            temp += e.getReceiverNeuron().getDelta() * e.getWeight();
        }
        this.delta = temp * derivative();
    }
}