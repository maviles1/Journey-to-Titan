package titan;

import org.junit.jupiter.api.Test;
import titan.flight.Vector3d;
import titan.landing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeedbackControllerTest {

    @Test
    public void testThruster() {
        LandingSimulation lsim = new LandingSimulation();

        Vector3d position = new Vector3d(0, 120000, 0);
        Vector3d velocity = new Vector3d(0, 0, 0);
        Vector3d shuttle_direction = new Vector3d(0, 1, 0);
        Vector3d wind_direction = new WindModel(0).getStartingWindVector(position.getY());
        Vector3d prevWindVec = new Vector3d(0, 0, 0);
        double angle = 0;
        double angularVelocity = 0;
        double time = 0;

        LandingState ls = new LandingState(position, velocity, shuttle_direction, wind_direction, prevWindVec, angle, angularVelocity, time);

        double width = 6;
        double height = 6;
        double length = 6;

        Shuttle s = new Shuttle(width, height, length, velocity, position, shuttle_direction);

        FeedbackController2 fc = new FeedbackController2(ls, s);
        Vector3d v = new Vector3d(0,0,0);

        assertEquals(null, fc.thruster());
    }
}
