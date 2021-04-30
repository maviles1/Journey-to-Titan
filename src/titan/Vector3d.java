package titan;

public class Vector3d implements Vector3dInterface {
    private double x;
    private double y;
    private double z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

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

    public double norm() {
        return Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
    }

    public double dist(Vector3dInterface other) {
        return Math.sqrt((getX() - other.getX()) * (getX() - other.getX()) + (getY() - other.getY()) * (getY() - other.getY()));
    }

    public String toString() {
        return ("(" + getX() + "," + getY() + "," + getZ() + ")");
    }


    public Vector3d copy() {
        return new Vector3d(x, y, z);
    }

}