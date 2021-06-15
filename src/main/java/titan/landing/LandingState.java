package titan.landing;

import titan.flight.*;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class LandingState implements StateInterface {

    private Vector3d position;
    private Vector3d velocity;
    private Vector3d shuttle_direction;
    private Vector3d wind_direction;
    private double time;

    public LandingState(Vector3d position, Vector3d velocity, Vector3d shuttle_direction, Vector3d wind_direction, double time) {
        this.position = position;
        this.velocity = velocity;
        this.shuttle_direction = shuttle_direction;
        this.wind_direction = wind_direction;
        this.time = time;
    }

    @Override
    public StateInterface addMul(double step, RateInterface r) {
        LandingRate rate = (LandingRate) r;

        Vector3d newPosition = position.addMul(step, rate.getPositionRate());
        Vector3d newVelocity = velocity.addMul(step, rate.getVelocityRate());

        //TODO: why are these null?
        Vector3d newShuttle_directionRate = null;
        Vector3d newWind_directionRate = null;

        return new LandingState(newPosition, newVelocity, newShuttle_directionRate, newWind_directionRate, getTime() + step);
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

    public String toString() {
        String s = "";
        s += "Landing Module"
                + " { x=" + position.getX()
                + ", y=" + position.getY()
                + ", z=" + position.getZ()

                + " vx=" + velocity.getX()
                + ", vy=" + velocity.getY()
                + ", vz=" + velocity.getZ()

                + " s_dx=" + shuttle_direction.getX()
                + ", s_dy=" + shuttle_direction.getY()
                + ", s_dz=" + shuttle_direction.getZ()

                + " w_dx=" + wind_direction.getX()
                + ", w_dy=" + wind_direction.getY()
                + ", w_dz=" + wind_direction.getZ() + " }\n";
        return s;
    }

    public double getTime() {
        return this.time;
    }


    @Override
    public Vector3d[] getPositions() {
        return new Vector3d[]{position};
    }

    @Override
    public Vector3d[] getVelocities() {
        return new Vector3d[]{velocity};
    }

    public LandingState copy(){
        Vector3d position = this.position;
        Vector3d velocity = this.velocity;
        Vector3d shuttle_direction = this.shuttle_direction;
        Vector3d wind_direction = this.wind_direction;

        return new LandingState(position, velocity, shuttle_direction, wind_direction, getTime());
    }
}