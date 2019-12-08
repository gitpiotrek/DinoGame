package ai.communication;

public class DataEmiter {
    private DataFlow dataFlow;

    public DataEmiter(){}

    public DataEmiter(DataFlow dataFlow){
        this.dataFlow = dataFlow;
    }

    public void emitData(NodeInput nodeInput){
        dataFlow.receiveData(nodeInput);
    }

    public void setDataFlow(DataFlow dataFlow) {
        this.dataFlow = dataFlow;
    }
}
