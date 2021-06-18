package titan.landing;

import titan.flight.Vector3d;

import java.util.Random;
import java.util.Vector;

public class WindModel {
    private Vector3d windarrows;
    private Random gen;
    private final double airdensity = 1.229 * 4.4; //currently earths m/s (titan is 4.4x)
    private final int maxangle = 25; //Still need to find proper value for this
    private final double ws = 928.57; //windspeed  -> Linear in m/s based on altitude in m

    //wind speed in m/s is altitude divided by ws

    public WindModel() {
        windarrows = new Vector3d();
        gen = new Random();
    }

    public Vector3d createNewWindVector(Vector3d previous, double altitude) {
        // altitude in meters or km?
        // new wind vector is created between 0 and max based on altitude

        //Create a random angle between -max <-> max degrees
        double angle = gen.nextDouble() * maxangle;
        angle = gen.nextBoolean() ? angle : (angle * -1);

        //Create new wind strength
        //System.out.println(previous);
        double windstrength = gen.nextDouble() * (altitude/ws);
        Vector3d windvector = previous.mul(windstrength/previous.norm());

        //Create new direction
        windvector = refactor2DVector(windvector, angle); //Wind is only 2d

        return windvector;
    }

    public Vector3d getForceVector(Vector3d windvector)
    {
        //Convert vector to a Force vector that adds to the velocity
        // F=(1 m2)×(1.229 kg/m3)×(2.24 m/s)2=6.17 N
//        double windforce = (6*6) * (airdensity) * windvector.norm(); // 6x6 is shuttle dimensions
        double windforce = (Math.PI * 3 * 3) * (airdensity) * windvector.norm(); // area of circle is pi*r^2
        windforce = windforce / 6000; //shuttle mass
        //apply force to windvector
        return windvector.mul(windforce/windvector.norm());
    }

    public Vector3d Refactor3DVector(Vector3d v, double a, double b, double g)
    {
        Vector3d rvec = new Vector3d(0,0,0);
        a = Math.toRadians(a);
        b = Math.toRadians(b);
        g = Math.toRadians(g);

        double x = v.getX();
        double y = v.getY();
        double z = v.getZ();

        rvec.setX( (Math.cos(a) * Math.cos(b) * x) +
                ( ( (Math.cos(a) * Math.sin(b) * Math.sin(g)) - (Math.sin(a) * Math.cos(g)) ) * y) +
                ( ( (Math.cos(a) * Math.sin(b) * Math.cos(g)) + (Math.sin(a) * Math.sin(g)) ) * z));

        rvec.setY( (Math.sin(a) * Math.cos(b) * x) +
                ( ( (Math.sin(a) * Math.sin(b) * Math.sin(g)) + (Math.cos(a) * Math.cos(g)) ) * y) +
                ( ( (Math.sin(a) * Math.sin(b) * Math.cos(g)) - (Math.cos(a) * Math.sin(g)) ) * z));

        rvec.setZ( (-Math.sin(b) * x) + (Math.cos(b) * Math.sin(g) * y) + (Math.cos(b) * Math.cos(g) * z) );

        return  rvec;
    }

    public Vector3d refactor2DVector(Vector3d v , double a)
    {
        Vector3d rvec = new Vector3d(0,0,0);
        a = Math.toRadians(a);

        double x = v.getX();
        double y = v.getY();

        rvec.setX( (x * Math.cos(a)) - (y * Math.sin(a)) );

        rvec.setY( (x * Math.sin(a)) + (y * Math.cos(a)) );

        return rvec;
    }

    public Vector3d getStartingWindVector(double altitude)
    {
        //wind more common west to east (not implemented yet)
        Vector3d swv = new Vector3d();

        //Create random Vector
        double[] xyz = new double[3];
        for(int i = 0; i < 2; i++)
        {
            double temp = gen.nextDouble();
            if(temp > 0.5)
                temp -= 1;

            xyz[i] = temp;
        }

        swv = new Vector3d(xyz[0], xyz[1], 0); //vector in 2d
        swv = swv.mul((gen.nextDouble() * (altitude/ws))/swv.norm());


        //make y value always negative
        if(swv.getY() > 0)
            swv.setY(swv.getY()*-1);

        return swv;
    }

    public Vector3d getWindVectors() {
        return windarrows;
    }
}
