package titan;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RateTest {

    @Test
    public void equalsTest() {
        Vector3d[] pos = {new Vector3d(1, 1, 1), new Vector3d(2, 2, 2)};
        Vector3d[] vel = {new Vector3d(1, 1, 1), new Vector3d(2, 2, 2)};
        Vector3d[] nPos = {new Vector3d(1, 1, 1), new Vector3d(2, 2, 2)};
        Vector3d[] nVel = {new Vector3d(1, 1, 1), new Vector3d(2, 2, 2)};
        Rate rate = new Rate(pos, vel);
        Rate otherRate = new Rate(nPos, nVel);
        Assertions.assertTrue(rate.equals(otherRate));
    }

    @Test
    public void notEqualsTest() {
        Vector3d[] pos = {new Vector3d(1, 1, 1), new Vector3d(2, 2, 2)};
        Vector3d[] vel = {new Vector3d(1, 1, 1), new Vector3d(2, 2, 2)};
        Vector3d[] nPos = {new Vector3d(1, 2, 1), new Vector3d(2, 2, 2)};
        Vector3d[] nVel = {new Vector3d(1, 1, 1), new Vector3d(2, 2, 2)};
        Rate rate = new Rate(pos, vel);
        Rate otherRate = new Rate(nPos, nVel);
        Assertions.assertFalse(rate.equals(otherRate));
    }

    @Test
    public void notEqualsTestNotRate() {
        Vector3d[] pos = {new Vector3d(1, 1, 1), new Vector3d(2, 2, 2)};
        Vector3d[] vel = {new Vector3d(1, 1, 1), new Vector3d(2, 2, 2)};
        Rate rate = new Rate(pos, vel);
        Object otherRate = new Object();
        Assertions.assertFalse(rate.equals(otherRate));
    }

    @Test
    public void mulTestWithScalarOne() {
        Vector3d[] positions = {new Vector3d(1, 2, 3), new Vector3d(2, 3, 4), new Vector3d(5, 6, 7)};
        Vector3d[] velocities = {new Vector3d(1, 2, 3), new Vector3d(2, 3, 4), new Vector3d(5, 6, 7)};

        Rate rate = new Rate(positions, velocities);
        Rate otherRate = rate.mul(1);

        Vector3d[] newPositions = {new Vector3d(1, 2, 3), new Vector3d(2, 3, 4), new Vector3d(5, 6, 7)};
        Vector3d[] newVelocities = {new Vector3d(1, 2, 3), new Vector3d(2, 3, 4), new Vector3d(5, 6, 7)};

        Rate solutionRate = new Rate(newPositions, newVelocities);

        Assertions.assertTrue(otherRate.equals(solutionRate));
    }

    @Test
    public void mulTestWithScalarPositive() {
        Vector3d[] positions = {new Vector3d(1, 2, 3), new Vector3d(2, 3, 4), new Vector3d(5, 6, 7)};
        Vector3d[] velocities = {new Vector3d(1, 2, 3), new Vector3d(2, 3, 4), new Vector3d(5, 6, 7)};

        Rate rate = new Rate(positions, velocities);
        Rate otherRate = rate.mul(4);

        Vector3d[] newPositions = {new Vector3d(4, 8, 12), new Vector3d(8, 12, 16), new Vector3d(20, 24, 28)};
        Vector3d[] newVelocities = {new Vector3d(4, 8, 12), new Vector3d(8, 12, 16), new Vector3d(20, 24, 28)};

        Rate solutionRate = new Rate(newPositions, newVelocities);

        Assertions.assertTrue(otherRate.equals(solutionRate));
    }

    @Test
    public void mulTestWithScalarZero() {
        Vector3d[] positions = {new Vector3d(1, 2, 3), new Vector3d(2, 3, 4), new Vector3d(5, 6, 7)};
        Vector3d[] velocities = {new Vector3d(1, 2, 3), new Vector3d(2, 3, 4), new Vector3d(5, 6, 7)};

        Rate rate = new Rate(positions, velocities);
        Rate otherRate = rate.mul(0);

        Vector3d[] newPositions = {new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), new Vector3d(0, 0, 0)};
        Vector3d[] newVelocities = {new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), new Vector3d(0, 0, 0)};

        Rate solutionRate = new Rate(newPositions, newVelocities);

        Assertions.assertTrue(otherRate.equals(solutionRate));
    }

    @Test
    public void mulTestWithScalarNegative() {
        Vector3d[] positions = {new Vector3d(1, 2, 3), new Vector3d(2, 3, 4), new Vector3d(5, 6, 7)};
        Vector3d[] velocities = {new Vector3d(1, 2, 3), new Vector3d(2, 3, 4), new Vector3d(5, 6, 7)};

        Rate rate = new Rate(positions, velocities);
        Rate otherRate = rate.mul(-1);

        Vector3d[] newPositions = {new Vector3d(-1, -2, -3), new Vector3d(-2, -3, -4), new Vector3d(-5, -6, -7)};
        Vector3d[] newVelocities = {new Vector3d(-1, -2, -3), new Vector3d(-2, -3, -4), new Vector3d(-5, -6, -7)};

        Rate solutionRate = new Rate(newPositions, newVelocities);

        Assertions.assertTrue(otherRate.equals(solutionRate));
    }

    @Test
    public void addTestPositive() {
        Vector3d[] positions = {new Vector3d(1, 1, 1), new Vector3d(2, 2, 2), new Vector3d(3, 3, 3)};
        Vector3d[] velocities = {new Vector3d(2, 2, 2), new Vector3d(3, 3, 3), new Vector3d(4, 4, 4)};
        Rate rate = new Rate(positions, velocities);

        Vector3d[] addPos = {new Vector3d(1, 1, 1), new Vector3d(2, 2, 2), new Vector3d(3, 3, 3)};
        Vector3d[] addVel = {new Vector3d(2, 2, 2), new Vector3d(3, 3, 3), new Vector3d(4, 4, 4)};
        Rate addRate = new Rate(addPos, addVel);

        Vector3d[] solPos = {new Vector3d(2, 2, 2), new Vector3d(4, 4, 4), new Vector3d(6, 6, 6)};
        Vector3d[] solVel = {new Vector3d(4, 4, 4), new Vector3d(6, 6, 6), new Vector3d(8, 8, 8)};
        Rate solutionRate = new Rate(solPos, solVel);

        Assertions.assertTrue(rate.add(addRate).equals(solutionRate));
    }

    @Test
    public void addTestNegative() {
        Vector3d[] positions = {new Vector3d(1, 1, 1), new Vector3d(2, 2, 2), new Vector3d(3, 3, 3)};
        Vector3d[] velocities = {new Vector3d(2, 2, 2), new Vector3d(3, 3, 3), new Vector3d(4, 4, 4)};
        Rate rate = new Rate(positions, velocities);

        Vector3d[] addPos = {new Vector3d(-1, -1, -1), new Vector3d(-2, -2, -2), new Vector3d(-3, -3, -3)};
        Vector3d[] addVel = {new Vector3d(-2, -2, -2), new Vector3d(-3, -3, -3), new Vector3d(-4, -4, -4)};
        Rate addRate = new Rate(addPos, addVel);

        Vector3d[] solPos = {new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), new Vector3d(0, 0, 0)};
        Vector3d[] solVel = {new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), new Vector3d(0, 0, 0)};
        Rate solutionRate = new Rate(solPos, solVel);

        Assertions.assertTrue(rate.add(addRate).equals(solutionRate));
    }

    @Test
    public void addTestZero() {
        Vector3d[] positions = {new Vector3d(1, 1, 1), new Vector3d(2, 2, 2), new Vector3d(3, 3, 3)};
        Vector3d[] velocities = {new Vector3d(2, 2, 2), new Vector3d(3, 3, 3), new Vector3d(4, 4, 4)};
        Rate rate = new Rate(positions, velocities);

        Vector3d[] addPos = {new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), new Vector3d(0, 0, 0)};
        Vector3d[] addVel = {new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), new Vector3d(0, 0, 0)};
        Rate addRate = new Rate(addPos, addVel);

        Vector3d[] solPos = {new Vector3d(1, 1, 1), new Vector3d(2, 2, 2), new Vector3d(3, 3, 3)};
        Vector3d[] solVel = {new Vector3d(2, 2, 2), new Vector3d(3, 3, 3), new Vector3d(4, 4, 4)};
        Rate solutionRate = new Rate(solPos, solVel);

        Assertions.assertTrue(rate.add(addRate).equals(solutionRate));
    }

}
