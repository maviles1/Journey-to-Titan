package titan;

public class Planet extends SpaceObject{
    private double radius;

    public Planet(String n, double m, Vector3d pos, Vector3d vel, double radius) {
        super(n, m, pos, vel);
        this.radius = radius;
    }

    //consider including name or not

    public void update(){
        setPosition(getPosition().add(getVelocity()));
    }

    public void attract(SpaceObject other){
        double g = 6.674 * Math.pow(10,-11);
        double r = getPosition().copy().dist(other.getPosition());
        Vector3d vectorToOther = getPosition().copy().sub(other.getPosition());
        double mag = g * (getMass() * other.getMass())/Math.pow(r,2);
        setVelocity(getVelocity().copy().add(vectorToOther.copy().mul(-1*mag)));
    }

    public double getRadius() {
        return this.radius;
    }
}
