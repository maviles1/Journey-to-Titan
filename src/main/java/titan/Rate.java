package titan;

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

    public Vector3d[] getRatePosition() {
        return positionRates;
    }

    public Vector3d[] getRateVelocity() {
        return velocityRates;
    }

    public Rate mul(double scalar){
        Vector3d[] newPositions = new Vector3d[size];
        Vector3d[] newVelocities = new Vector3d[size];
        for (int i = 0; i < size; i++){
            newPositions[i] = positionRates[i].mul(scalar);
            newVelocities[i] = velocityRates[i].mul(scalar);
        }
        return new Rate(newPositions,newVelocities);
    }

    public Rate add(RateInterface rate){
        Rate r = (Rate) rate;
        Vector3d[] newPositions = new Vector3d[size];
        Vector3d[] newVelocities = new Vector3d[size];
        for (int i = 0; i < size; i++){
            newPositions[i] = positionRates[i].add(r.getRatePosition()[i]);
            newVelocities[i] = velocityRates[i].add(r.getRateVelocity()[i]);
        }
        return new Rate(newPositions,newVelocities);
    }

    public String toString(){
        String s = "";
        //for (int i = 0; i < size; i++)
        //{
                s += " Values: { x=" + positionRates[3].getX()
                        + ", y=" + positionRates[3].getY()
                        + ", z=" + positionRates[3].getZ()
                        //+ " vx=" + velocityRates[i].getX()
                        //+ ", vy=" + velocityRates[i].getY()
                        //+ ", vz=" + velocityRates[i].getZ()
                        + " }\n";
        //}
        return s; //this is unused
    }
}
