package titan.flight;

import titan.interfaces.RateInterface;

public class Rate implements RateInterface {

    private Vector3d[] positionRates;
    private Vector3d[] velocityRates;
    private final int size;

    public Rate(Vector3d[] rateP, Vector3d[] rateV) {
        this.positionRates = rateP;
        this.velocityRates = rateV;
        this.size = positionRates.length;
    }

    // the new velocities
    public Vector3d[] getPosRates() {
        return positionRates;
    }

    //the acceleration
    public Vector3d[] getVelRates() {
        return velocityRates;
    }

    public Rate mul(double scalar){
        Vector3d[] newPositions = new Vector3d[size];
        Vector3d[] newVelocities = new Vector3d[size];
        for (int i = 0; i < size; i++){
            newPositions[i] = positionRates[i].mul(scalar);
            newVelocities[i] = velocityRates[i].mul(scalar);
        }
        return new Rate(newPositions, newVelocities);
    }

    public Rate add(RateInterface rate){
        Rate r = (Rate) rate;
        Vector3d[] newPositions = new Vector3d[size];
        Vector3d[] newVelocities = new Vector3d[size];
        for (int i = 0; i < size; i++){
            newPositions[i] = positionRates[i].add(r.getPosRates()[i]);
            newVelocities[i] = velocityRates[i].add(r.getVelRates()[i]);
        }
        return new Rate(newPositions,newVelocities);
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < size; i++) {
            s += " Values: { x=" + positionRates[i].toString()
                + ", y=" + positionRates[i].toString()
                + ", z=" + positionRates[i].toString()
                + " vx=" + velocityRates[i].toString()
                + ", vy=" + velocityRates[i].toString()
                + ", vz=" + velocityRates[i].toString()
                + " }\n";
        }
        return s;
    }

    public boolean equals(Object other) {
        if (other instanceof Rate) {
            Rate otherRate = (Rate) other;
            boolean equals = true;
            for (int i = 0; i < positionRates.length; i++) {
                if (!positionRates[i].equals(otherRate.getPosRates()[i]) || !velocityRates[i].equals(otherRate.getVelRates()[i]))
                    equals = false;
            }
            return equals;
            //return Arrays.equals(this.positionRates, otherRate.positionRates) && Arrays.equals(this.velocityRates, otherRate.velocityRates);
        } else return false;
    }
}
