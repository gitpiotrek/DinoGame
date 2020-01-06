package ai.neural;

public class Synapse {

    private double value = Math.random();
    private double weight = Math.random();
    private Neuron senderNeuron;
    private Neuron receiverNeuron;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Neuron getSenderNeuron() {
        return senderNeuron;
    }

    public void setSenderNeuron(Neuron senderNeuron) {
        this.senderNeuron = senderNeuron;
    }

    public Neuron getReceiverNeuron() {
        return receiverNeuron;
    }

    public void setReceiverNeuron(Neuron receiverNeuron) {
        this.receiverNeuron = receiverNeuron;
    }

    public Synapse(Neuron senderNeuron, Neuron receiverNeuron) {
        this.senderNeuron = senderNeuron;
        this.receiverNeuron = receiverNeuron;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void transferValue(){
        this.value = senderNeuron.getOutputValue();
    }

    public void updateWeightValue(){
    weight = weight + 0.4 * receiverNeuron.getDelta()* senderNeuron.getOutputValue();
    }
}
