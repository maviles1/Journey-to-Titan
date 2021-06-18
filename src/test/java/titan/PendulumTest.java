package titan;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import titan.flight.Solver;
import titan.interfaces.StateInterface;

import javax.swing.*;
import java.sql.SQLOutput;

/**
 * This was probably a waste of time
 */
public class PendulumTest {
    double duration = 20;
    double stepSize = 0.01;

    @Test
    void getPendulumData() {
        Solver solver = new Solver(new RKSolver());
        Vector3d[] pos = new Vector3d[]{new Vector3d(0, 0, 0)};
        Vector3d[] vel = new Vector3d[]{new Vector3d(0, 3, 0)};

        StateInterface[] states = solver.solve(new ODEPendulum(), new State(pos, vel, 0), duration, stepSize, true);

        for (StateInterface s : states) {
            //System.out.println(s.getPositions()[0].getX() + " ");
            //System.out.println(s.getPositions()[0].getY() + " ");
            System.out.println(s.getVelocities()[0].getY());
        }

        //Assertions.assertEquals(solutionX, ((State)states[states.length - 1]).getPositions()[0].getX(), delta);
    }
}
