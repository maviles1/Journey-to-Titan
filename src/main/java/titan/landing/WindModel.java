package titan.landing;

import titan.flight.Vector3d;

import java.util.Random;

public class WindModel {
    Vector3d[] windarrows;
    Random gen;

    public WindModel() {
        windarrows = new Vector3d[5];
        gen = new Random();
    }

    public Vector3d CreateWindVector(Vector3d previous, double altitude) {
        // altitude in meters?
        // new wind vector is created between 0 and max based on altitude
        //


        return new Vector3d(1, 1, 1);
    }

    public Vector3d[] getWindVectors() {
        return windarrows;
    }

}
