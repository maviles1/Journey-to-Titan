package titan.landing;

import titan.flight.Vector3d;

import java.util.Random;

public class WindModel {
    private Vector3d windarrows;
    private Random gen;
    private final int maxangle = 25;
    private final double ws = 0.92857; //Linear

    //wind speed in m/s is altitude divided by ws

    public WindModel() {
        windarrows = new Vector3d();
        gen = new Random();
    }

    public Vector3d CreateWindVector(Vector3d previous, double altitude) {
        // altitude in meters or km?
        // new wind vector is created between 0 and max based on altitude

        //Create 3 random angles between -max <-> max degrees
        // use them as aplha, beta, gamma
        double[] angles = new double[3];
        for(int i = 0; i < 3; i++)
        {
            double angle = gen.nextDouble() * maxangle;
            angle = gen.nextBoolean() == true ? angle : (angle * -1);
            angles[i] = angle;
        }

        //Create new wind strength
        Vector3d windvector = previous.mul((altitude/ws)/previous.norm());

        //Create new direction
        windvector = RefactorVector(windvector, angles[0], angles[1], angles[2]);

        return windvector;
    }

    //UNUSED METHOD
    public void CalculateWindRotation(Vector3d windvector, Shuttle sh)
    {
        // Select a random point where it hits the probe (on the y-axis)
        double radius = sh.getHeight()/2;
        double Ymax = sh.getPosition().getY() + radius;
        double Ymin = sh.getPosition().getY() - radius;
        double Xmax = sh.getPosition().getX() + radius;
        double Xmin = sh.getPosition().getX() - radius;
        double newY = (Ymax-Ymin) * gen.nextDouble() + Ymin;
        double newX = (Xmax-Xmin) * gen.nextDouble() + Xmin;

        // Point where the wind hits the probe
        Vector3d windimpact = new Vector3d(newX, newY, sh.getPosition().getZ());

        // Calculate the strength of the wind vector in rotations per timestep

    }


    public Vector3d RefactorVector(Vector3d v, double a, double b, double g)
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

    public Vector3d getWindVectors() {
        return windarrows;
    }

}
