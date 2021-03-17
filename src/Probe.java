import titan.SpaceObject;
import titan.Vector3d;

public class Probe extends SpaceObject {

    public Probe(String n, double m, Vector3d pos, Vector3d vel) {
        super(n, m, pos, vel);
    }

    @Override
    public void attract(SpaceObject other) {

    }

    @Override
    public void update() {

    }
}
