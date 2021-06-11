package titan.landing;

import titan.flight.Vector3d;

import java.util.Random;

public class WindModel {
    private Vector3d[] windarrows;
    private Random gen;
    private final double ws = 0.92857; //Linear

    //wind speed in m/s is altitude divided by ws

    public WindModel() {
        windarrows = new Vector3d[5];
        gen = new Random();
    }

    public Vector3d CreateWindVector(Vector3d previous, double altitude) {
        // altitude in meters?
        // new wind vector is created between 0 and max based on altitude

        //Create a random angle with X degrees either positive or negative
        double angle = gen.nextDouble() * 25;
        angle = gen.nextBoolean() == true ? angle : (angle * -1);


        Vector3d windvector = previous.mul((altitude/ws)/previous.norm());



        return windvector;
    }

    public Vector3d RefactorVector(Vector3d v, double a, double b, double g)
    {
        Vector3d rvec = new Vector3d(0,0,0);
        double x = v.getX();
        double y = v.getY();
        double z = v.getZ();

        rvec.setX( (Math.cos(a) * Math.cos(b) * x) +
                ( ( (Math.cos(a) * Math.sin(b) * Math.sin(g)) - (Math.sin(a) * Math.cos(g)) ) * y) +
                ( ( (Math.cos(a) * Math.sin(b) * Math.cos(g)) + (Math.sin(a) * Math.sin(g)) ) * z));

        rvec.setY( (Math.sin(a) * Math.cos(b) * x) +
                ( ( (Math.sin(a) * Math.cos(b) * Math.sin(g)) + (Math.cos(a) * Math.cos(g)) ) * y) +
                ((Math.sin(a) * Math.sin(b) * Math.cos(g) - Math.cos(a) * Math.sin(g)) * z));

        rvec.setZ( (-Math.sin(b) * x) + (Math.cos(b) * Math.sin(g) * y) + (Math.cos(b) * Math.cos(g) * z) );

        return  rvec;
    }

    public Vector3d[] getWindVectors() {
        return windarrows;
    }

}
