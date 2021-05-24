package titan;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import titan.interfaces.StateInterface;

public class SimpleODESolverTest {

    double startHeight = 1103.2;
    double initialVel = 10;
    double solutionX = 150;
    double solutionY = 0;
    double duration = 15;

    double delta = 0.05; //accuracy

    double stepSize = 0.01;

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
}
