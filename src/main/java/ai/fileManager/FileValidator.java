package ai.fileManager;

import sun.plugin.dom.exception.WrongDocumentException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class FileValidator {

    public static void validate(File file) throws WrongDocumentException ,IOException, IllegalArgumentException {
        if(!file.getName().endsWith(".csv")){
            throw new WrongDocumentException("The file should be in CSV format");
        }
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        Double[] data = Arrays.stream(bufferedReader.readLine().split(","))
                .map(Double::parseDouble).toArray(Double[]::new);
        bufferedReader.close();
       if(data.length != 66){
           throw new WrongDocumentException("Neural network should have only 66 synapses");
       }
    }
}
