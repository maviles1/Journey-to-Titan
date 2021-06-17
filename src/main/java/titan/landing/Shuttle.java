package titan.landing;

import titan.flight.*;

public class Shuttle {
    private Vector3d position; // position of the centre of mass
    private Vector3d velocity;
    private Vector3d direction;
    private double width; // ?
    private double height; // ?
    private double length; // ?
    private final double weight = 8000; //kg
    private final String name = "THE GOAT";


    // direction will only be relevant based on the perpendicular angle to Titan, (no Z axis)
    // Thrusters
    //


    public Shuttle(double width, double height, double length, Vector3d velocity, Vector3d position, Vector3d direction) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.velocity = velocity;
        this.position = position;
        this.direction = direction;
    }

    public double getWidth() {
        return width;
    }
    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }

    public double getLength() {
        return length;
    }
    public void setLength(double length) {
        this.length = length;
    }

    public double getWeight() {
        return weight;
    }

    public String getName() {
        return name;
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
