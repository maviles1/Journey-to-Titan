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

    public void setFuelMass(double fuelMass)
    {
        this.setMass(this.getMass() + fuelMass);
        this.fuelMass = fuelMass;
    }

    public void thrust(Vector3d thrustVector)
    {
        //Total mass
        double totalmass = this.getMass()+this.getFuelMass();
        Vector3d oldvel = this.getVelocity();
        Vector3d newvel = getVelocity().copy().add(thrustVector);

        double FinalForce = totalmass * Math.abs(newvel.magnitude() - oldvel.magnitude());

        System.out.println("TotalMass: " + totalmass);
        System.out.println("Fuelmass: " + this.getFuelMass());
        System.out.println("probemass: " + this.getMass());
//        System.out.println("newvel: " + newvel.magnitude());
//        System.out.println("oldvel: " + oldvel.magnitude());
//        System.out.println("vectormag: " + thrustVector.magnitude());
//        System.out.println("before-> F: " + FinalForce);
        double usedmass = FinalForce / newvel.magnitude();
//        System.out.println("after-> m: " + usedmass);

        fuelMass -= usedmass;

        this.setVelocity(getVelocity().add(thrustVector));

    }

    public double getFuelMass(){
        return fuelMass;
    }
}