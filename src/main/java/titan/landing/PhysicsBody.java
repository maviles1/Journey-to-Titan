package titan.landing;

import titan.flight.Vector3d;

public class PhysicsBody {

    private double x;
    private double y;
    private double weight;
    private Vector3d velocity;
    private Vector3d position;
    private Vector3d direction; // We need some value to store the direction of the shuttle relative to ?

    public PhysicsBody(double x, double y, double weight, Vector3d velocity, Vector3d position, Vector3d direction) {
        this.x = x;
        this.y = y;
        this.weight = weight;
        this.velocity = velocity;
        this.position = position;
        this.direction = direction;
    }

    public Vector3d getVelocity()
    {
        return velocity;
    }
    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity;
    }

    public Vector3d getPosition() {
        return position;
    }
    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public Vector3d getDirection() {
        return direction;
    }
    public void setDirection(Vector3d direction) {
        this.direction = direction;
    }

}
