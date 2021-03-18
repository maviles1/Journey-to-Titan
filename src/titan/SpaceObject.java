package titan;

import java.sql.SQLOutput;

public abstract class SpaceObject {

    private String name;
    private double mass;
    private Vector3d position;
    private Vector3d velocity;
    private Vector3d force;
    private Vector3d acceleration;
    private double radius;

    public SpaceObject(String n, double m, Vector3d pos, Vector3d vel) {
        this.name = n;
        setMass(m);
        setPosition(pos);
        setVelocity(vel);
    }

    public void setPosition(Vector3d pos) {
        this.position = pos;
    }
    public void setMass(double mass) {
        this.mass = mass;
    }
    public void setVelocity(Vector3d vel) {
        this.velocity = vel;
    }
    public void setAcceleration(Vector3d ac) {
        this.acceleration = ac;
    }
    public void setForce(Vector3d f) {
        this.force = f;
    }
    public Vector3d getPosition() {
        return this.position;
    }
    public double getMass() {
        return this.mass;
    }
    public Vector3d getVelocity() {
        return this.velocity;
    }
    public Vector3d getAcceleration() {
        return this.acceleration;
    }
    public Vector3d getForce() {
        return this.force;
    }
    public String getName() {
        return this.name;
    }

    public void setRadius(double r) {
        this.radius = r;
    }

    public double getRadius() { return radius;}

    public abstract void attract(SpaceObject other);


    public abstract void update();
}
