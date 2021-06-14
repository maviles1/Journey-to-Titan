package titan.landing;

import titan.flight.State;
import titan.flight.Vector3d;

public class LandingState extends State {
    private Vector3d shuttle_direction;
    private Vector3d wind_direction;

    public LandingState(Vector3d[] positions, Vector3d[] velocities, Vector3d shuttle_direction, Vector3d wind_direction, double time) {
        super(positions, velocities, time);
        this.shuttle_direction = shuttle_direction;
        this.wind_direction = wind_direction;
    }
}
