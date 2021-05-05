package titan;

public class Probe extends SpaceObject {

    double fuelMass;
    double mass;
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
    public Boolean checkTitanOrbit(Probe probe, Vector3d titan)
    {
        Vector3d probeVec = probe.getPosition();
        double dist =  probeVec.dist(titan);
        double titanRadius = 2575500; //currently static and in M
        if(dist >= titanRadius + 100000 && dist <= titanRadius + 300000)
            return true;
        else
            return false;

    }

    public void setFuelMass(double mass){
        this.fuelMass = mass;
    }

    public void thrust(Vector3d thrustVector) {
        this.setVelocity(getVelocity().add(thrustVector));
        double momentum = thrustVector.magnitude() * fuelMass;
        this.fuelMass--;
    }


    public double getFuelMass(){
        return fuelMass;
    }
}
