package titan;

import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

import java.util.HashMap;
import java.util.Map;

public class State implements StateInterface {

    static double[] mass;
    static Map<Integer, String> names;
    public Vector3d[] positions;
    public Vector3d[] prevPos;
    static double[] radius;

    double time;
    private Vector3d[] velocities;

    public State(Vector3d[] positions, Vector3d[] velocities, double time) {
        this.positions = positions;
        this.velocities = velocities;
        this.time = time;
    }

    /**
     * @param step The time-step of the update
     * @param r the change in the current state
     * @return the new state
     */
    @Override
    public StateInterface addMul(double step, RateInterface r) {
        Rate rate = (Rate) r;

        Vector3d[] newPositions = new Vector3d[positions.length];
        Vector3d[] newVelocities = new Vector3d[velocities.length];


        for (int i = 0; i < velocities.length; i++) {
            newVelocities[i] = velocities[i].addMul(step, rate.getRateVelocity()[i]); // vel(t+1)=p(t)+h*acc
            // updated the positions with the position with the previous vel instead of the change in velocity
            newPositions[i] = positions[i].addMul(step, velocities[i]); // p(t+1)=p(t)+h*vel
        }
        return new State(newPositions, newVelocities, time + step);
    }




    public String toString() {
        String s = "";
        for (int i = 0; i < positions.length; i++) {
            s += names.get(i)
                    + " { x=" + Renderer.toScreenCoordinates(positions[i].getX())
                    + ", y=" + Renderer.toScreenCoordinates(positions[i].getY())
                    + ", z=" + Renderer.toScreenCoordinates(positions[i].getZ())
                    + " vx=" + velocities[i].getX()
                    + ", vy=" + velocities[i].getY()
                    + ", vz=" + velocities[i].getZ() + " }\n";
        }
        return s;
    }

    public static void setMass(double[] mass) {
        State.mass = mass;
    }

    public static void setNames() {
        names = new HashMap<>();
        for (int i = 0; i < SpaceObjectBuilder.spaceObjects.size(); i++) {
            names.put(i, SpaceObjectBuilder.spaceObjects.get(i).getName());
        }
    }

    public State copy(){
        Vector3d [] positions = new Vector3d[this.positions.length];
        Vector3d [] velocities = new Vector3d[this.velocities.length];
        for (int i = 0; i < this.positions.length; i++){
            positions[i] = this.positions[i];
            velocities[i] = this.velocities[i];
        }
        return new State(positions, velocities, 0);
    }

    public static void setRadius(double [] radius){
        State.radius = radius;
    }

    public Vector3d[] getPosition(){
        return positions;
    }

    public Vector3d[] getVelocities(){
        return velocities;
    }

    public void setPositions(double step, Vector3d[] newPos) {
        for (int i = 0; i < positions.length; i++) {
            positions[i] = positions[i].addMul(step, newPos[i]);
        }
        //sets the previously calculated velocities
       // velocities=newPos;
    }
    public void setPrevPosition(Vector3d [] newPrevPos){ prevPos=newPrevPos;}

    public void setPosition(Vector3d [] newPos){ positions=newPos;}

    public void setVelocities(Vector3d [] newVel){ velocities=newVel;}

    public Vector3d[] getPrevPosition(){ return prevPos; }

    public double getTime() {
        return this.time;
    }


}
