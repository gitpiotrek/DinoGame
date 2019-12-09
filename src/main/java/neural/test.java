package neural;

public class test {
    public static void main(String[] args){
    NeuralNetwork nn = new NeuralNetwork();
    nn.initializeNetwork(6,6,3);
    nn.loadTrainData();
    nn.train();
   // double[] values = {0.3,0.8,0.9};
    //double[] values2 = {0.8,0.9,0.2};
    //ouble[] values3 = {0.1,0.5,0.8};
    //ouble[] inputValues = {0.2,0.32,0.32,0.2,0.32,0.32,0.88};

    // dodac ustawianie wartosci wejsciowych
   // nn.setInputs(inputValues);
    //nn.train(values);

    }
}
