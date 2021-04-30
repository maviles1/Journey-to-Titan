package titan;

public class Probe extends SpaceObject {

    double fuelMass;
    double massToMeters = 2;
    public Probe(String n, double m, Vector3d pos, Vector3d vel) {
        super(n, m, pos, vel);
        fuelMass = m;
    }

    @Override
    public void attract(SpaceObject other) {

    }

    @Override
    public void update() {

    }

    public void setFuelMass(double mass){
        this.fuelMass = mass;
    }

    public void thrust(Vector3d thrustVector) {
        this.setVelocity(getVelocity().add(thrustVector));
//        System.out.println(fuelMass);
//        double dist = this.getPosition().dist(getPosition().add(getVelocity().add(thrustVector)));
//        if (fuelMass == 0){
//            return;
//        }
//        if (fuelMass - calcFuelBurn(dist) < 0) {
//            double distForMass = fuelMass * massToMeters;
//            this.setVelocity(getVelocity().add(thrustVector.mul(distForMass/dist)));
//            fuelMass -= calcFuelBurn(distForMass);
//        }
//        else {
//            fuelMass -= calcFuelBurn(dist);
//            this.setVelocity(getVelocity().add(thrustVector));
//        }
    }

    public double calcFuelBurn(double distance){
        return distance/massToMeters;
    }

    public double getFuelMass(){
        return fuelMass;
    }
}
