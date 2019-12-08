package ai.communication;

public class DataEmitter {
    private DataFlow dataFlow;

    public DataEmitter(){}

    public DataEmitter(DataFlow dataFlow){
        this.dataFlow = dataFlow;
    }

    public void emitData(NodeInput nodeInput){
        dataFlow.receiveData(nodeInput);
    }

    public void setDataFlow(DataFlow dataFlow) {
        this.dataFlow = dataFlow;
    }
}
