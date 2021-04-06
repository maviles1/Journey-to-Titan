package titan;

import titan.interfaces.RateInterface;

public class Rate implements RateInterface {

    private Vector3d[] positionRates;
    private Vector3d[] velocityRates;


    public Rate(Vector3d[] rateP, Vector3d[] rateV) {
        this.positionRates = rateP;
        this.velocityRates = rateV;
    }

    public Vector3d[] getRatePosition() {
        return positionRates;
    }

    public Vector3d[] getRateVelocity() {
        return velocityRates;
    }
}
