package titan;

import java.util.HashMap;
import java.util.Map;

public class State implements StateInterface {

    Vector3d[] positions;
    Vector3d[] velocities;
    static double[] mass;
    static Map<Integer, String> names;

    double time;

    public State(Vector3d[] positions, Vector3d[] velocities, double time) {
        this.positions = positions;
        this.velocities = velocities;
        this.time = time;
    }



    @Override
    public StateInterface addMul(double step, RateInterface r) {
        Rate rate = (Rate) r;
        Vector3d[] newPositions = new Vector3d[positions.length];
        Vector3d[] newVelocities = new Vector3d[velocities.length];
        for (int i = 0; i < positions.length; i++) {
            newPositions[i] = positions[i].addMul(step, rate.getRatePosition()[i]);
            newVelocities[i] = velocities[i].addMul(step, rate.getRateVelocity()[i]);
        }
        return new State(newPositions, newVelocities, time + step);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < positions.length; i++) {
            s.append("").append(names.get(i)).append(" { x=").append(positions[i].getX()).append(", y=").append(positions[i].getY()).append(", z=").append(positions[i].getZ()).append(" vx=").append(velocities[i].getX()).append(", vy=").append(velocities[i].getY()).append(", vz=").append(velocities[i].getZ()).append(" }\n");
        }
        return s.toString();
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

    public double getTime() {
        return this.time;
    }
}
