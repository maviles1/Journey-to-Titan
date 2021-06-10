package titan.landing;

import titan.flight.*;

public class Shuttle {
    private double width;
    private double height;
    private double radius;
    private double weight = 8000; //kg
    private String name = "THE GOAT";
    private Vector3d velocity;
    private Vector3d position;
    private Vector3d direction;

    // direction will only be relevant based on the perpendicular angle to Titan, (no Z axis)
    // Thrusters
    //


    public Shuttle(double width, double height, double radius, double weight, String name, Vector3d velocity, Vector3d position, Vector3d direction) {
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.weight = weight;
        this.name = name;
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

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
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
