import titan.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Testing {
    public static void main(String[] args) {

        double absMinDist = Double.MAX_VALUE;
        Vector3d startingPos = new Vector3d(0,0,0);
        Vector3d startingVel = new Vector3d(0,0,0);
        //2.147483647E9;

        for (int i = 0; i < 5000; i++) {
            SpaceObjectBuilder builder = new SpaceObjectBuilder("src/solar_system_data-2020_04_01.txt");
            ArrayList<SpaceObject> spaceObjects = new ArrayList<>(builder.getSpaceObjects());
            ProbeSimulator sim = new ProbeSimulator(spaceObjects);

            double a = (Math.random()*1000);
            a *= Math.random() < 0.5 ? 1 : -1;
            double b = (Math.random()*1000);
            b *= Math.random() < 0.5 ? 1 : -1;
            double c = (Math.random()*1000);
            c *= Math.random() < 0.5 ? 1 : -1;

            Vector3d vel = new Vector3d(a,b,c);
            vel = vel.mul(1/vel.norm());
            vel = vel.mul(60000);

            a = Math.random() * 1000;
            a *= Math.random() < 0.5 ? 1 : -1;
            b = Math.random()*1000;
            b *= Math.random() < 0.5 ? 1 : -1;
            c = Math.random()*1000;
            c *= Math.random() < 0.5 ? 1 : -1;

            Vector3d pos = new Vector3d(a,b,c);
            pos = pos.mul(1/pos.norm());
            pos = pos.mul(6371000);

            pos = new Vector3d(6371000, 1, 1);

            //Vector3d pos = new Vector3d(6371000, 1, 1);
            sim.trajectory(pos, vel,31556926, 1000);

            int minDistanceStateIndex = 0;
            double minDistance = Double.MAX_VALUE;

            for (int j = 0; j < sim.getStates().length; j++) {
                State state = (State)sim.getStates()[j];
                double dist = state.getPosition()[8].dist(state.getPosition()[11]);
                double distFromRadius = dist - 2574700;
                if (distFromRadius < minDistance) {
                    minDistance = distFromRadius;
                    minDistanceStateIndex = j;
                }

            }

            if (minDistance < absMinDist) {
                absMinDist = minDistance;
                startingPos = pos;
                startingVel = vel;
            }
        }

        if (absMinDist < 1.1451975395278286E7) {
            System.out.println("NEW");
        } else if (absMinDist == 2.7017760706283194E8) {
            System.out.println("equal");
        }
        System.out.println("Absolute minimum: " + absMinDist);
        System.out.println("Starting pos " + startingPos);
        System.out.println("Starting vel " + startingVel);


    }
}
