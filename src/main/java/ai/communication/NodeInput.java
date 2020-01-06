package ai.communication;

public class NodeInput {
    private double distanceToNextObstacle;
    private double heightOfObstacle;
    private double widthOfObstacle;
    private double velocity;
    private double playerYPosition;
    private double pterodactylHeight;
    private double distanceBetweenObstacles;

    private State state;

    public NodeInput(){}

    public NodeInput(String line){
        parseDataToNodeInput(line);
    }

    private void parseDataToNodeInput(String line){
    String[] data = line.split(",");
        this.distanceToNextObstacle = Double.parseDouble(data[0]);
        this.heightOfObstacle = Double.parseDouble(data[1]);
        this.widthOfObstacle = Double.parseDouble(data[2]);
        this.playerYPosition = Double.parseDouble(data[3]);
        this.velocity = Double.parseDouble(data[4]);
        this.distanceBetweenObstacles = Double.parseDouble(data[5]);
        this.pterodactylHeight = Double.parseDouble(data[6]);

        switch (data[7]){
            case "RUN" : this.state = State.RUN; break;
            case "JUMP" : this.state = State.JUMP; break;
            case "SMALL_JUMP" : this.state = State.SMALL_JUMP; break;
            case "DUCK" : this.state = State.DUCK; break;
        }
    }

    public double[] toInputData(){
        return new double[]{
                distanceToNextObstacle,
        heightOfObstacle,
        widthOfObstacle,
        velocity,
        playerYPosition,
        pterodactylHeight,
        distanceBetweenObstacles
        };
    }

    public double getDistanceToNextObstacle() {
        return distanceToNextObstacle;
    }

    public void setDistanceToNextObstacle(double distanceToNextObstacle) {
        this.distanceToNextObstacle = distanceToNextObstacle;
    }

    public double getHeightOfObstacle() {
        return heightOfObstacle;
    }

    public void setHeightOfObstacle(double heightOfObstacle) {
        this.heightOfObstacle = heightOfObstacle;
    }

    public double getWidthOfObstacle() {
        return widthOfObstacle;
    }

    public void setWidthOfObstacle(double widthOfObstacle) {
        this.widthOfObstacle = widthOfObstacle;
    }

    public double getPterodactylHeight() {
        return pterodactylHeight;
    }

    public void setPterodactylHeight(double pterodactylHeight) {
        this.pterodactylHeight = pterodactylHeight;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getPlayerYPosition() {
        return playerYPosition;
    }

    public void setPlayerYPosition(double playerYPosition) {
        this.playerYPosition = playerYPosition;
    }

    public double getDistanceBetweenObstacles() {
        return distanceBetweenObstacles;
    }

    public void setDistanceBetweenObstacles(double distanceBetweenObstacles) {
        this.distanceBetweenObstacles = distanceBetweenObstacles;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
