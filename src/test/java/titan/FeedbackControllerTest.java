package titan;

import org.junit.jupiter.api.Test;
import titan.flight.Vector3d;
import titan.landing.FeedbackController;
import titan.landing.LandingState;
import titan.landing.Shuttle;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeedbackControllerTest {

    @Test
    public void testTrajectoryOneDayX() {
        Vector3d position = new Vector3d(0, 0, 0);
        Vector3d velocity = new Vector3d(0, 0, 0);
        Vector3d shuttle_direction = new Vector3d(0, 0, 0);
        Vector3d wind_direction = new Vector3d(0, 0, 0);
        Vector3d prevWindVec = new Vector3d(0, 0, 0);
        double angle = 0;
        double angularVelocity = 0;
        double time = 0;

        LandingState ls = new LandingState(position, velocity, shuttle_direction, wind_direction, prevWindVec, angle, angularVelocity, time);

        double width = 0;
        double height = 0;
        double length = 0;
        Vector3d s_velocity = new Vector3d(0, 0, 0);
        Vector3d s_position = new Vector3d(0, 0, 0);
        Vector3d direction = new Vector3d(0, 0, 0);

        Shuttle s = new Shuttle(width, height, length, s_velocity, s_position, direction);

        FeedbackController fc = new FeedbackController(ls, s);
        Vector3d v = new Vector3d(0,0,0);

        assertEquals(fc.thruster(), v);
    }
}
