package titan.landing;

import titan.flight.Rate;
import titan.flight.State;
import titan.flight.Vector3d;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class LandingState extends State {
    private Vector3d position;
    private Vector3d velocity;
    private Vector3d shuttle_direction;
    private Vector3d wind_direction;

    public LandingState(Vector3d position, Vector3d velocity, Vector3d shuttle_direction, Vector3d wind_direction, double time) {
        super(null, null, time);
        this.position = position;
        this.velocity = velocity;
        this.shuttle_direction = shuttle_direction;
        this.wind_direction = wind_direction;
    }

    @Override
    public StateInterface addMul(double step, RateInterface r) {
        Rate rate = (Rate) r;

        Vector3d[] newPositions = new Vector3d[positions.length];
        Vector3d[] newVelocities = new Vector3d[velocities.length];

        for (int i = 0; i < velocities.length; i++) {
            newPositions[i] = positions[i].addMul(step, rate.getPosRates()[i]);
            newVelocities[i] = velocities[i].addMul(step, rate.getVelRates()[i]);
        }

        //return new LandingState(newPositions, newVelocities, getTime() + step);
        return null;
    }

    public Vector3d getPosition() {
        return position;
    }
    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public Vector3d getVelocity() {
        return velocity;
    }
    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
    }

    public Vector3d getShuttle_direction() {
        return shuttle_direction;
    }
    public void setShuttle_direction(Vector3d shuttle_direction) {
        this.shuttle_direction = shuttle_direction;
    }

    public Vector3d getWind_direction() {
        return wind_direction;
    }
    public void setWind_direction(Vector3d wind_direction) {
        this.wind_direction = wind_direction;
    }
}
