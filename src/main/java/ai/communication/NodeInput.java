package ai.communication;

public class NodeInput {
    private double distanceToNextObstacle;
    private double heightOfObstacle;
    private double widthOfObstacle;
    private double pterodactylHeight;
    private double velocity;
    private double playerYPosition;
    private double distanceBetweenObstacles;
    //????
   // private double bias;
    private State state;

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
