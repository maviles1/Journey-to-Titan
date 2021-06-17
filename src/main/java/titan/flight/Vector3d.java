package titan.flight;

import titan.interfaces.RateInterface;
import titan.interfaces.Vector3dInterface;

public class Vector3d implements Vector3dInterface {
    private double x;
    private double y;
    private double z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d(){}

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double dot(Vector3d other){
        double x = getX()*other.getX();
        double y = getY()*other.getY();
        double z = getZ()*other.getZ();
        return x + y + z;
    }


    public Vector3d add(Vector3dInterface other) {

        return new Vector3d(getX() + other.getX(), getY() + other.getY(), getZ() + other.getZ());
    }

    public double magnitude(){
        return Math.sqrt(getX()*getX() + getY()*getY() + getZ()*getZ());
    }

    public Vector3d sub(Vector3dInterface other) {
        return new Vector3d(getX() - other.getX(), getY() - other.getY(), getZ() - other.getZ());

    }

    public Vector3d mul(double scalar) {

        return new Vector3d(getX() * scalar, getY() * scalar, getZ() * scalar);

    }

    public Vector3d addMul(double scalar, Vector3dInterface other) {
        double x = (getX() + scalar * other.getX());
        double y = (getY() + scalar * other.getY());
        double z = (getZ() + scalar * other.getZ());

        return new Vector3d(x, y, z);
    }

    public Vector3d multiply(Vector3d other){
        return new Vector3d(getX()*other.getX(), getY()*other.getY(), getZ()*other.getZ());
    }

    public double norm(){
        return Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
    }

//    public double dist(Vector3dInterface other) {
//        return Math.sqrt((getX() - other.getX()) * (getX() - other.getX()) + (getY() - other.getY()) * (getY() - other.getY()));
//    }

    public double dist(Vector3dInterface other){
        //return Math.sqrt( (getX()-other.getX())*(getX()-other.getX()) + (getY()-other.getY())*(getY()-other.getY()) + (getZ()-other.getZ())*(getZ()-other.getZ()) );
        return Math.sqrt(Math.pow(getX() - other.getX(), 2) +
                Math.pow(getY()- other.getY(), 2) +
                Math.pow(getZ() - other.getZ(), 2));
    }

    public Vector3d rotate(double a, double b, double g){
        Vector3d rvec = new Vector3d(0,0,0);
        a = Math.toRadians(a);
        b = Math.toRadians(b);
        g = Math.toRadians(g);
        double x = getX();
        double y = getY();
        double z = getZ();

        rvec.setX( (Math.cos(a) * Math.cos(b) * x) +
                ( ( (Math.cos(a) * Math.sin(b) * Math.sin(g)) - (Math.sin(a) * Math.cos(g)) ) * y) +
                ( ( (Math.cos(a) * Math.sin(b) * Math.cos(g)) + (Math.sin(a) * Math.sin(g)) ) * z));

        rvec.setY( (Math.sin(a) * Math.cos(b) * x) +
                ( ( (Math.sin(a) * Math.sin(b) * Math.sin(g)) + (Math.cos(a) * Math.cos(g)) ) * y) +
                ( ( (Math.sin(a) * Math.sin(b) * Math.cos(g)) - (Math.cos(a) * Math.sin(g)) ) * z));

        rvec.setZ( (-Math.sin(b) * x) + (Math.cos(b) * Math.sin(g) * y) + (Math.cos(b) * Math.cos(g) * z) );

        return rvec;
    }

    public String toString() {
        return ("(" + getX() + "," + getY() + "," + getZ() + ")");
    }

    public Vector3d scale(double mag){
        Vector3d norm = normalize();
         return new Vector3d(norm.getX()*mag, norm.getY()*mag, norm.getZ()*mag);
    }

    public Vector3d copy() {
        return new Vector3d(x, y, z);
    }

    public boolean equals(Vector3dInterface other) {
        Vector3d v = (Vector3d) other;
        return this.x == v.getX() && this.y == v.getY() && this.z == v.getZ();
    }

    public Vector3d normalize(){
        double scalar = norm();
        return new Vector3d(getX()/scalar, getY()/scalar, getZ()/scalar);
    }

    public Vector3d mult(double multiple){
        return new Vector3d(multiple*getX(), multiple*getY(), multiple*getZ());
    }

    public boolean equals(Object o) {
        if (o instanceof RateInterface)
            return equals((Vector3dInterface) o);
        else
            return false;
    }

}