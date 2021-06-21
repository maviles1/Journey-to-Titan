package titan.flight;

public class Probe extends SpaceObject {

    double fuelMass;
    final double G = 6.67430E-11;
    public Probe(String n, double m, Vector3d pos, Vector3d vel) {
        super(n, m, pos, vel);
    }

    @Override
    public void attract(SpaceObject other) {

    }

    @Override
    public void update() {

    }

    public void setFuelMass(double fuelMass)
    {
        this.fuelMass = fuelMass;
    }

    @Override
    public double getMass(){
        return super.getMass() + getFuelMass();
    }

    public void thrust(Vector3d thrustVector) {
        //Total mass
//        System.out.println("Vector used: " + thrustVector);
        double totalmass = this.getMass();
        Vector3d oldvel = this.getVelocity();
        Vector3d newvel = getVelocity().copy().add(thrustVector);
        double FinalForce = totalmass * thrustVector.norm()/3600;
        double usedmass = FinalForce / newvel.magnitude();
        fuelMass -= usedmass;
//        System.out.println( "used mass: " + usedmass);
        this.setFuelMass(getFuelMass() - usedmass);
//        System.out.println("Force used: " + (1/1e6)*(getMass() + usedmass)*thrustVector.norm()/3600);
        this.setVelocity(getVelocity().add(thrustVector));
    }

    public void land(Vector3d thrustVector) {
        //Total mass
        this.setVelocity(getVelocity().add(thrustVector));
    }

    public double getFuelMass(){
        return fuelMass;
    }

    public boolean isInOrbit(Vector3d probeAcceleration, Vector3d other, double otherMass){
        double dist = other.dist(getPosition());
        double sqrDst = Math.sqrt(other.sub(getPosition()).norm());
        Vector3d forceDir = (other.sub(getPosition())).normalize();
        Vector3d force = forceDir.mul( G * getMass() * otherMass/sqrDst);
        Vector3d acceleration = force.mul(1/getMass());
        boolean isPerpendicular = forceDir.dot(getVelocity()) == 0;
        System.out.println(forceDir.dot(getVelocity()));
        if (!isPerpendicular){
            return false;
        }
        else{
            return probeAcceleration.norm() == Math.sqrt(G*otherMass/dist);
        }
    }
}