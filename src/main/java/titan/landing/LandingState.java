package titan.landing;

import titan.flight.*;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

import java.util.Vector;

public class LandingState implements StateInterface {

    private Vector3d position;
    private Vector3d velocity;
    private Vector3d shuttle_direction;
    private Vector3d wind_direction;
    private Vector3d prevWindVector;
    private double time;
    private double angle;
    private double angularVelocity;

    public LandingState(Vector3d position, Vector3d velocity, Vector3d shuttle_direction, Vector3d wind_direction, Vector3d prevWindVec, double angle, double angularVelocity, double time) {
        this.position = position;
        this.velocity = velocity;
        this.shuttle_direction = shuttle_direction;
        this.wind_direction = wind_direction;
        this.prevWindVector = prevWindVec;
        this.time = time;
        this.angle = angle;
        this.angularVelocity = angularVelocity;
    }

    public LandingState(Vector3d position, Vector3d velocity, Vector3d prevWindVec, double angle, double angularVelocity, double time) {
        this.position = position;
        this.velocity = velocity;
        this.prevWindVector = prevWindVec;
        this.time = time;
        this.angle = angle;
        this.angularVelocity = angularVelocity;
    }

    @Override
    public StateInterface addMul(double step, RateInterface r) {
        LandingRate rate = (LandingRate) r;

        Vector3d newPosition = position.addMul(step, rate.getPositionRate());
        Vector3d newVelocity = velocity.addMul(step, rate.getVelocityRate());
        double newAngularVel = angularVelocity + step* rate.getAngularAcceleration();

//        //TODO: why are these null?
//        Vector3d newShuttle_directionRate = null;
//        Vector3d newWind_directionRate = null;
//        Vector3d prevwindvec = null;

        return new LandingState(newPosition, newVelocity, prevWindVector, angle, newAngularVel, getTime() + step);
    }

    public double getAngle() {
        return angle;
    }

    public double getAngularVelocity() {
        return angularVelocity;
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

    public Vector3d getPrevWindVector() {
        return prevWindVector;
    }
    public void setPrevWindVector(Vector3d prevWindVector) {
        this.prevWindVector = prevWindVector;
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
        Vector3d prevwindvec = this.prevWindVector;

        return new LandingState(position, velocity, shuttle_direction, wind_direction, prevwindvec, angle, angularVelocity, getTime());
    }
}
