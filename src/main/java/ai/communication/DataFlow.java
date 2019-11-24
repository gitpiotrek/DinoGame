package ai.communication;

public interface DataFlow {

    void receiveData(NodeInput nodeInput);
    NodeInput getData() throws NullPointerException;
    public boolean isEmpty();
}
