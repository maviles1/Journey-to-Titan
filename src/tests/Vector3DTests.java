package tests;

import org.junit.Test;
import titan.Vector3d;

import static org.junit.Assert.*;

/**
 * Test class responsible for validating the Vector3D class and its functionality. The JUnit library is necessary to run
 * this class.
 */
public class Vector3DTests {

    //TODO: may have to make some tests on double imprecision

    @Test
    public void equalsTest() {
        //test basic equals
        Vector3d firstVector = new Vector3d(1, 2, 3);
        Vector3d secondVector = new Vector3d(1, 2, 3);
        assertTrue(firstVector.equals(secondVector));

        //test that negatively-inverted vector not equal to positive vector
        firstVector = new Vector3d(-1, -2, -3);
        assertFalse(firstVector.equals(secondVector));

        //test integer/double equals
        firstVector = new Vector3d(1, 2, 3);
        secondVector = new Vector3d(1.0,2.0,3.0);
        assertTrue(firstVector.equals(secondVector));
    }


    @Test
    public void addVector3DTest() {
        //test addition of positive vectors
        Vector3d positiveVector = new Vector3d(1, 2, 3);
        Vector3d additiveVector = new Vector3d(2, 3, 4);
        Vector3d solution = new Vector3d(3, 5, 7);
        assertTrue(positiveVector.add(additiveVector).equals(solution));

        //test addition of positive and negative vectors
        Vector3d negAdditiveVector = new Vector3d(-2, -3, -4);
        solution = new Vector3d(-1, -1, -1);
        assertTrue(positiveVector.add(negAdditiveVector).equals(solution));

        //test addition of two negative vectors
        Vector3d negVector = new Vector3d(-1, -2, -3);
        solution = new Vector3d(-3, -5, -7);
        assertTrue(negVector.add(negAdditiveVector).equals(solution));

        //test addition with 0 vector
        Vector3d zeroVector = new Vector3d(0, 0, 0);
        assertTrue(positiveVector.add(zeroVector).equals(positiveVector));

        //TODO: double overflow/underflow test

    }

    @Test
    public void subVector3DTest() {
        //subtract positive vector from positive vector
        Vector3d positiveVector = new Vector3d(3, 6, 9);
        Vector3d subtractionVector = new Vector3d(1, 2, 3);
        Vector3d solution = new Vector3d(2, 4, 6);
        assertTrue(positiveVector.sub(subtractionVector).equals(solution));

        //subtract two of the same valued vectors
        subtractionVector = new Vector3d(3, 6, 9);
        Vector3d zeroVector = new Vector3d(0, 0, 0);
        assertTrue(positiveVector.sub(subtractionVector).equals(zeroVector));

        //subtract the same vector from itself
        assertTrue(subtractionVector.sub(subtractionVector).equals(zeroVector));

        //subtract the zero vector from a vector
        assertTrue(subtractionVector.sub(zeroVector).equals(subtractionVector));

        //subtract a vector from the zero vector
        solution = new Vector3d(-3, -6, -9);
        assertTrue(zeroVector.sub(positiveVector).equals(solution));

        //subtract a negative from a positive
        solution = new Vector3d(6, 12, 18);
        subtractionVector = new Vector3d(-3, -6, -9);
        positiveVector = new Vector3d(3, 6, 9);
        assertTrue(positiveVector.sub(subtractionVector).equals(solution));

        //subtract a positive vector from a negative vector
        solution = new Vector3d(-6, -12, -18);
        assertTrue(subtractionVector.sub(positiveVector).equals(solution));
    }

    @Test
    public void mulTest() {
        //multiply by 1
        double scalar = 1.0;
        Vector3d vector = new Vector3d(2, 3, 4);
        Vector3d solution = new Vector3d(2, 3, 4);
        assertTrue(vector.mul(scalar).equals(solution));
        vector = new Vector3d(-2, -3, -4);
        solution = new Vector3d(-2, -3, -4);
        assertTrue(vector.mul(scalar).equals(solution));

        //multiply by -1
        scalar = -1;
        solution = new Vector3d(2, 3, 4);
        assertTrue(vector.mul(scalar).equals(solution));
        vector = new Vector3d(2, 3, 4);
        solution = new Vector3d(-2, -3, -4);
        assertTrue(vector.mul(scalar).equals(solution));

        //multiply by 0
        scalar =  0.0;
        solution = new Vector3d(0, 0, 0);
        assertTrue(vector.mul(scalar).equals(solution));

        //multiply by positive scalar
        scalar = 2.0;
        solution = new Vector3d(4, 6, 8);
        assertTrue(vector.mul(scalar).equals(solution));


    }

    @Test
    public void addMulTest() {

    }

}
