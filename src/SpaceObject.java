public interface SpaceObject {
    public void setPosition(Vector3d pos);
    public void setMass(double mass);
    public void setVelocity(Vector3d vel);
    public void setAcceleration(Vector3d ac);
    public void setForce(Vector3d f);
    public Vector3d getPosition();
    public double getMass();
    public Vector3d getVelocity();
    public Vector3d getAcceleration();
    public Vector3d getForce();
    public String getName();
    public double getRadius();
    public void attract(SpaceObject other);

    void update();
}
