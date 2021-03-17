public abstract class SpaceObjects {
    private double mass;
    private String name;
    private Vector3d position;
    private Vector3d velocity;
    private Vector3d force;
    private Vector3d acceleration;

    public SpaceObjects(String n, double m, Vector3d pos, Vector3d vel){
        name=n;
        mass=m;
        position=pos;
        velocity=vel;
    }

    //consider including name or not
    public SpaceObjects(){}

    public void setPosition(Vector3d pos) {
        position=pos;
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

    public Vector3d getForce() {
        return force;
    }
}
