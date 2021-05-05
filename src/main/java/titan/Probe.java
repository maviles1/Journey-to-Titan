package titan;

public class Probe extends SpaceObject {

    double fuelMass;
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
        double totalmass = this.getMass()+this.getFuelMass();
        Vector3d oldvel = this.getVelocity();
        Vector3d newvel = getVelocity().copy().add(thrustVector);

        double FinalForce = totalmass * Math.abs(newvel.magnitude() - oldvel.magnitude());

        double usedmass = FinalForce / newvel.magnitude();
        fuelMass -= usedmass;

        this.setVelocity(getVelocity().add(thrustVector));
    }

    public double getFuelMass(){
        return fuelMass;
    }
}
