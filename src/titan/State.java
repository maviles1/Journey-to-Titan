package titan;

public class State implements StateInterface {

    SpaceObject[] spaceObjects;

    public State(SpaceObject[] spaceObjects) {
        this.spaceObjects = spaceObjects;
    }

    @Override
    public StateInterface addMul(double step, RateInterface rate) {
        return null;
    }
}
