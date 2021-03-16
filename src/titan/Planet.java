package titan;

public class Planet implements SpaceObject{
    private String name;
    private double mass;
    private Vector3d position;
    private Vector3d velocity;
    private Vector3d force;
    private Vector3d acceleration;
    private double radius;

    public Planet(String n, double m, Vector3d pos, Vector3d vel, double radius) {
        name=n;
        mass=m;
        position=pos;
        velocity=vel;
        this.radius = radius;
    }

    //consider including name or not

    public void setPosition(Vector3d pos) {

        position=pos;
    }

    public String getName(){
        return name;
    }


    public void setMass(double m) {
        mass=m;
    }

    public void setVelocity(Vector3d vel) {
        velocity=vel;
    }

    public void setAcceleration(Vector3d ac) {
        acceleration=ac;
    }

    public void setForce(Vector3d f) {
        force=f;
    }

    public Vector3d getPosition() {
        return position;
    }

    public double getMass() {
        return mass;
    }

    public Vector3d getVelocity() {
        return velocity;
    }

    public Vector3d getAcceleration() {

        return acceleration;
    }

    public double getRadius(){
        return radius;
    }

    public void update(){
        position = position.add(velocity);
    }

    public Vector3d getForce() {

        return force;
    }

    public void attract(SpaceObject other){
        double g = 6.674 * Math.pow(10,-11);
        double r = position.copy().dist(other.getPosition());
        Vector3d vectorToOther = position.copy().sub(other.getPosition());
        double mag = g * (mass * other.getMass())/Math.pow(r,2);
        velocity = velocity.copy().add(vectorToOther.copy().mul(-1*mag));
    }
}
