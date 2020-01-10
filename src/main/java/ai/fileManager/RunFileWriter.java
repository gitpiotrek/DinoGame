package ai.fileManager;

import ai.communication.DataReceiver;
import ai.communication.NodeInput;
import game.MainGameStarter;

import java.io.*;

public class RunFileWriter {
    private DataReceiver dataReceiver;
    private FileWriter  csvWriter = null;
    private  File file = new File("./" + MainGameStarter.playerName +".csv");

    public RunFileWriter(DataReceiver dataReceiver){
        this.dataReceiver = dataReceiver;
    }

    public void writeToFile(){
       // executorService = new ScheduledThreadPoolExecutor(2);
       // executorService.scheduleAtFixedRate(this, 0, 1000/60, TimeUnit.MILLISECONDS);

        //ioThread.setName("Thread for io calculation");
        file.delete();
        try {
            file.createNewFile();
            csvWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread ioThread = new Thread(() -> {
            while (true) {
                if (!dataReceiver.isEmpty()) {
                    NodeInput nodeInput = dataReceiver.getData();
                    try {
                        csvWriter.append(appendReceivedData(nodeInput));
                        csvWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        ioThread.start();
    }

    public void finishWriting(File dest){
        //executorService.shutdownNow();
        // ioThread.suspend();
        try {
        csvWriter.close();
        if (dest != null) {
            //Files.copy(file.toPath(), dest.toPath());
            NeuralNetworkFileManager neuralNetworkFileManager = new NeuralNetworkFileManager(file);
            neuralNetworkFileManager.writeNeuralNetworkToFile(dest);
            //deleteTempFile();
            }
        }catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    private String appendReceivedData(NodeInput nodeInput){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(nodeInput.getDistanceToNextObstacle());
        stringBuilder.append(",");
        stringBuilder.append(nodeInput.getHeightOfObstacle());
        stringBuilder.append(",");
        stringBuilder.append(nodeInput.getWidthOfObstacle());
        stringBuilder.append(",");
        stringBuilder.append(nodeInput.getPlayerYPosition());
        stringBuilder.append(",");
        stringBuilder.append(nodeInput.getVelocity());
        stringBuilder.append(",");
        stringBuilder.append(nodeInput.getDistanceBetweenObstacles());
        stringBuilder.append(",");
        stringBuilder.append(nodeInput.getPterodactylHeight());
        stringBuilder.append(",");

        stringBuilder.append(nodeInput.getState());
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private void deleteTempFile(){
        file.delete();
    }
}
