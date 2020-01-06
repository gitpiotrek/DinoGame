package ai.communication;

import java.util.concurrent.LinkedBlockingQueue;

//Anty-back-pressure pattern
public class DataReceiver implements DataFlow {

    private LinkedBlockingQueue<NodeInput> queue = new LinkedBlockingQueue();
@Override
    public void receiveData(NodeInput nodeInput){
        try {
            queue.put(nodeInput);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
@Override
    public NodeInput getData() throws NullPointerException{
       return queue.poll();
    }
@Override
    public boolean isEmpty(){
    return queue.isEmpty();
    }
}
