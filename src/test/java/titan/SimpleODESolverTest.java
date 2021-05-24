package titan;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import titan.interfaces.StateInterface;
import titan.ui.PolySim;

import java.util.ArrayList;
import java.util.Arrays;

public class SimpleODESolverTest {

    double startHeight = 1103.2;
    double initialVel = 10;
    double solutionX = 150;
    double solutionY = 0;
    double duration = 15;

//    double startHeight = 100;
//    double initialVel = 10;
//    double solutionX = 45.1601;
//    double solutionY = 0;
//    double duration = 4.52;

    double delta = 0.05; //accuracy

    double stepSize = 0.25;

    @Test
    void simpleODETestEulerX() {
        Solver eulerSolver = new Solver(new EulerSolver());
        Vector3d[] pos = new Vector3d[]{new Vector3d(0, startHeight, 0)};
        Vector3d[] vel = new Vector3d[]{new Vector3d(initialVel, 0, 0)};

        StateInterface[] states = eulerSolver.solve(new SimpleODE(), new State(pos, vel, 0), duration, stepSize, true);

        Assertions.assertEquals(solutionX, ((State)states[states.length - 1]).getPosition()[0].getX(), delta);
    }

    @Test
    void simpleODETestEulerY() {
        Solver eulerSolver = new Solver(new EulerSolver());
        Vector3d[] pos = new Vector3d[]{new Vector3d(0, startHeight, 0)};
        Vector3d[] vel = new Vector3d[]{new Vector3d(initialVel, 0, 0)};

        StateInterface[] states = eulerSolver.solve(new SimpleODE(), new State(pos, vel, 0), duration, stepSize, true);

        Assertions.assertEquals(solutionY, ((State)states[states.length - 1]).getPosition()[0].getY(), delta);
    }

    @Test
    void simpleODETestVerletX() {
        Solver verletSolver = new Solver(new VerletSolver());
        Vector3d[] pos = new Vector3d[]{new Vector3d(0, startHeight, 0)};
        Vector3d[] vel = new Vector3d[]{new Vector3d(initialVel, 0, 0)};

        StateInterface[] states = verletSolver.solve(new SimpleODE(), new State(pos, vel, 0), duration, stepSize, true);
        Assertions.assertEquals(solutionX, ((State)states[states.length - 1]).getPosition()[0].getX(), delta);
    }

    @Test
    void simpleODETestVerletY() {
        Solver verletSolver = new Solver(new VerletSolver());
        Vector3d[] pos = new Vector3d[]{new Vector3d(0, startHeight, 0)};
        Vector3d[] vel = new Vector3d[]{new Vector3d(initialVel, 0, 0)};

        StateInterface[] states = verletSolver.solve(new SimpleODE(), new State(pos, vel, 0), duration, stepSize, true);
        Assertions.assertEquals(solutionY, ((State)states[states.length - 1]).getPosition()[0].getY(), delta);
    }

    @Test
    void simpleODETestRKClassicX() {
        Solver rkSolver = new Solver(new RKSolver());
        Vector3d[] pos = new Vector3d[]{new Vector3d(0, startHeight, 0)};
        Vector3d[] vel = new Vector3d[]{new Vector3d(initialVel, 0, 0)};

        StateInterface[] states = rkSolver.solve(new SimpleODE(), new State(pos, vel, 0), duration, stepSize, true);
        Assertions.assertEquals(solutionX, ((State)states[states.length - 1]).getPosition()[0].getX(), delta);
    }

    @Test
    void simpleODETestRKClassicY() {
        Solver rkSolver = new Solver(new RKSolver());
        Vector3d[] pos = new Vector3d[]{new Vector3d(0, startHeight, 0)};
        Vector3d[] vel = new Vector3d[]{new Vector3d(initialVel, 0, 0)};

        StateInterface[] states = rkSolver.solve(new SimpleODE(), new State(pos, vel, 0), duration, stepSize, true);
        Assertions.assertEquals(solutionY, ((State)states[states.length - 1]).getPosition()[0].getY(), delta);
    }

    @Test
    void simpleODETestRKogX() {
        Solver rk2Solver = new Solver(RKSolver.getRK2());
        Vector3d[] pos = new Vector3d[]{new Vector3d(0, startHeight, 0)};
        Vector3d[] vel = new Vector3d[]{new Vector3d(initialVel, 0, 0)};

        StateInterface[] states = rk2Solver.solve(new SimpleODE(), new State(pos, vel, 0), duration, stepSize, true);
        Assertions.assertEquals(solutionX, ((State)states[states.length - 1]).getPosition()[0].getX(), delta);
    }

    @Test
    void simpleODETestRKogY() {
        Solver rk2Solver = new Solver(RKSolver.getRK2());
        Vector3d[] pos = new Vector3d[]{new Vector3d(0, startHeight, 0)};
        Vector3d[] vel = new Vector3d[]{new Vector3d(initialVel, 0, 0)};

        StateInterface[] states = rk2Solver.solve(new SimpleODE(), new State(pos, vel, 0), duration, stepSize, true);
        Assertions.assertEquals(solutionY, ((State)states[states.length - 1]).getPosition()[0].getY(), delta);
    }

    @Test
    void displayResults() {
        Solver eulerSolver = new Solver(new EulerSolver());
        Solver verletSolver = new Solver(new VerletSolver());
        Solver rkSolver = new Solver(new RKSolver());
        Solver rk2Solver = new Solver(RKSolver.getRK2());

        Vector3d[] pos = new Vector3d[]{new Vector3d(0, startHeight, 0)};
        Vector3d[] vel = new Vector3d[]{new Vector3d(initialVel, 0, 0)};

        ArrayList<StateInterface[]> polyStates = new ArrayList<>();
        polyStates.add(eulerSolver.solve(new SimpleODE(), new State(pos, vel, 0), duration, stepSize, true));
        polyStates.add(verletSolver.solve(new SimpleODE(), new State(pos, vel, 0), duration, stepSize, true));
        polyStates.add(rkSolver.solve(new SimpleODE(), new State(pos, vel, 0), duration, stepSize, true));
        polyStates.add(rk2Solver.solve(new SimpleODE(), new State(pos, vel, 0), duration, stepSize, true));

        //System.out.println(polyStates);
        for (StateInterface[] states : polyStates) {
            System.out.println("-----------");
            for (int i = 0; i < states.length; i++) {
                State y = (State) states[i];
                System.out.println(y.getPosition()[0].getY());
            }
            System.out.println("-----------");
        }
    }
}
