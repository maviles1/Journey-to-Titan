package titan.landing;

import titan.flight.Rate;
import titan.flight.Vector3d;
import titan.interfaces.RateInterface;

public class LandingRate extends Rate {
    private Vector3d positionRate;
    private Vector3d velocityRate;
    private Vector3d shuttle_directionRate;
    private Vector3d wind_directionRate;
    public LandingRate(Vector3d posR, Vector3d velR, Vector3d s_dR, Vector3d w_dR) {
        super(null, null);
        positionRate = posR;
        velocityRate = velR;
        shuttle_directionRate = s_dR;
        wind_directionRate = w_dR;
    }

    @Override
    public LandingRate mul(double scalar){
        Vector3d newPosition = positionRate.mul(scalar);
        Vector3d newVelocity = velocityRate.mul(scalar);
        Vector3d newShuttle_directionRate = null;
        Vector3d newWind_directionRate = null;
        return new LandingRate(newPosition, newVelocity, newShuttle_directionRate, newWind_directionRate);
    }

    @Override
    public LandingRate add(RateInterface rate){
        LandingRate r = (LandingRate) rate;
        Vector3d newPosition = positionRate.add(r.getPositionRate());
        Vector3d newVelocity = velocityRate.add(r.getVelocityRate());
        Vector3d newShuttle_directionRate = null;
        Vector3d newWind_directionRate = null;
        return new LandingRate(newPosition, newVelocity, newShuttle_directionRate, newWind_directionRate);
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

//    public String toString() {
//        String s = "";
//        for (int i = 0; i < size; i++) {
//            s += " Values: { x=" + positionRates[i].toString()
//                    + ", y=" + positionRates[i].toString()
//                    + ", z=" + positionRates[i].toString()
//                    + " vx=" + velocityRates[i].toString()
//                    + ", vy=" + velocityRates[i].toString()
//                    + ", vz=" + velocityRates[i].toString()
//                    + " }\n";
//        }
//        return s;
//    }
//
//    public boolean equals(Object other) {
//        if (other instanceof Rate) {
//            Rate otherRate = (Rate) other;
//            boolean equals = true;
//            for (int i = 0; i < positionRates.length; i++) {
//                if (!positionRates[i].equals(otherRate.getPosRates()[i]) || !velocityRates[i].equals(otherRate.getVelRates()[i]))
//                    equals = false;
//            }
//            return equals;
//            //return Arrays.equals(this.positionRates, otherRate.positionRates) && Arrays.equals(this.velocityRates, otherRate.velocityRates);
//        } else return false;
//    }
}
