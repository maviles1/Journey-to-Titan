package titan.landing;

import titan.flight.Rate;
import titan.flight.Vector3d;
import titan.interfaces.RateInterface;

public class LandingRate implements RateInterface {
    private Vector3d positionRate;
    private Vector3d velocityRate;
    private Vector3d shuttle_directionRate;
    private Vector3d wind_directionRate;
    private Vector3d prevwindvec;
    private double angularAcceleration;
    private double angularVelocity;

    public LandingRate(Vector3d posR, Vector3d velR, Vector3d s_dR, Vector3d w_dR, Vector3d pwv, double angularAcceleration) {
        positionRate = posR;
        velocityRate = velR;
        shuttle_directionRate = s_dR;
        wind_directionRate = w_dR;
        prevwindvec = pwv;
        this.angularAcceleration = angularAcceleration;
    }

    public LandingRate(Vector3d posRate, Vector3d velRate, double angularVelocity, double angularAcceleration) {
        positionRate = posRate;
        velocityRate = velRate;
        this.angularVelocity = angularVelocity;
        this.angularAcceleration = angularAcceleration;
    }

    public LandingRate mul(double scalar) {
        Vector3d newPosition = positionRate.mul(scalar);
        Vector3d newVelocity = velocityRate.mul(scalar);
        double newAngularVel = angularVelocity * scalar;
        double newAngularAccel = angularAcceleration * scalar;
//        Vector3d newShuttle_directionRate = null;
//        Vector3d newWind_directionRate = null;
//        Vector3d prevwv = null;
//        return new LandingRate(newPosition, newVelocity, newShuttle_directionRate, newWind_directionRate, prevwv, angularAcceleration);
        return new LandingRate(newPosition, newVelocity, newAngularVel, newAngularAccel);
    }

    public LandingRate add(RateInterface rate){
        LandingRate r = (LandingRate) rate;
        Vector3d newPosition = positionRate.add(r.getPositionRate());
        Vector3d newVelocity = velocityRate.add(r.getVelocityRate());
        double newAngularVel = angularVelocity + r.getAngularVelocity();
        double newAngularAccel = angularAcceleration + r.getAngularAcceleration();

//        //TODO: why are these null?
//        Vector3d newShuttle_directionRate = null;
//        Vector3d newWind_directionRate = null;
//        Vector3d prevwv = null;
        return new LandingRate(newPosition, newVelocity, newAngularVel, newAngularAccel);
    }

    public double getAngularAcceleration() {
        return angularAcceleration;
    }
    public double getAngularVelocity() {
        return angularVelocity;
    }

    public Vector3d getPositionRate() {
        return positionRate;
    }
    public void setPositionRate(Vector3d positionRate) {
        this.positionRate = positionRate;
    }

    public Vector3d getVelocityRate() {
        return velocityRate;
    }
    public void setVelocityRate(Vector3d velocityRate) {
        this.velocityRate = velocityRate;
    }

    public Vector3d getShuttle_directionRate() {
        return shuttle_directionRate;
    }
    public void setShuttle_directionRate(Vector3d shuttle_directionRate) {
        this.shuttle_directionRate = shuttle_directionRate;
    }

    public Vector3d getWind_directionRate() {
        return wind_directionRate;
    }
    public void setWind_directionRate(Vector3d wind_directionRate) {
        this.wind_directionRate = wind_directionRate;
    }

    public String toString() {
        String s = "";
        s += " Values: { x=" + positionRate.toString()
                + ", y=" + positionRate.toString()
                + ", z=" + positionRate.toString()

                + " vx=" + velocityRate.toString()
                + ", vy=" + velocityRate.toString()
                + ", vz=" + velocityRate.toString()

                + " s_dx=" + shuttle_directionRate.toString()
                + ", s_dy=" + shuttle_directionRate.toString()
                + ", s_dz=" + shuttle_directionRate.toString()

                + " w_dx=" + wind_directionRate.toString()
                + ", w_dy=" + wind_directionRate.toString()
                + ", w_dz=" + wind_directionRate.toString() + " }\n";
        return s;
    }
}
