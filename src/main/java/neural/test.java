package neural;

public class test {
    public static void main(String[] args){
    NeuralNetwork nn = new NeuralNetwork();
    nn.initializeNetwork(7,6,3);
    double[] values = {0.3,0.8,0.9};
    double[] values2 = {0.8,0.9,0.2};
    double[] values3 = {0.1,0.5,0.8};
    double[] inputValues = {0.2,0.32,0.32,0.2,0.32,0.32,0.88};

    // dodac ustawianie wartosci wejsciowych
    nn.setInputs(inputValues);
    nn.train(values);
    
    nn.train(values2);
    nn.train(values3);
    }
}
