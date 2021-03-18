package titan;

import com.sun.jdi.VoidValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class State implements StateInterface {

    public Vector3d[] positions;
    private Vector3d[] velocities;
    static double[] mass;
    static Map<Integer, String> names;
    static double[] radius;

    double time;

    public State(Vector3d[] positions, Vector3d[] velocities, double time) {
        this.positions = positions;
        this.velocities = velocities;
        this.time = time;
//   System.out.println(Arrays.toString(this.positions));
    }

    @Override
    public StateInterface addMul(double step, RateInterface r) {
  //      System.out.println("STEP: " + step);
        Rate rate = (Rate) r;

        Vector3d[] newPositions = new Vector3d[positions.length];
        Vector3d[] newVelocities = new Vector3d[velocities.length];
//        System.out.println(Arrays.toString(positions));
        for (int i = 0; i < velocities.length; i++) {
            newPositions[i] = positions[i].addMul(step, rate.getRatePosition()[i]);
            newVelocities[i] = velocities[i].addMul(step, rate.getRateVelocity()[i]);
        }

     //   System.out.println("NEWW " +Arrays.toString(rate.getRatePosition()));
      //  System.out.println("NEWW " +Arrays.toString(positions));


        return new State(newPositions, newVelocities, time + step);
    }


//    public String toString2() {
//        StringBuilder s = new StringBuilder();
//          for (int i = 0; i < positions.length; i++) {
//            s.append("").append(names.get(i)).append(" { x=").append(Renderer.toScreenCoordinates(positions[i].getX())).append(", y=").append(Renderer.toScreenCoordinates(positions[i].getY())).append(", z=").append(Renderer.toScreenCoordinates(positions[i].getZ())).append(" vx=").append(Renderer.toScreenCoordinates(velocities[i].getX())).append(", vy=").append(Renderer.toScreenCoordinates(velocities[i].getY())).append(", vz=").append(Renderer.toScreenCoordinates(velocities[i].getZ())).append(" }\n");
//            //s.append("").append(names.get(i)).append(" { x=").append(Renderer.toScreenCoordinates(positions[i].getX())).append(", y=").append(Renderer.toScreenCoordinates(positions[i].getY())).append(", z=").append(Renderer.toScreenCoordinates(positions[i].getZ())).append(" vx=").append(Renderer.toScreenCoordinates(velocities[i].getX())).append(", vy=").append(Renderer.toScreenCoordinates(velocities[i].getY())).append(", vz=").append(Renderer.toScreenCoordinates(velocities[i].getZ())).append(" }\n");
//        }
//
//      //  s.append("").append(names.get(0)).append(" { x=").append(positions[0].getX()).append(", y=").append(positions[0].getY()).append(", z=").append(positions[0].getZ()).append(" vx=").append(velocities[0].getX()).append(", vy=").append(velocities[0].getY()).append(", vz=").append(velocities[0].getZ()).append(" }\n");
//        //s.append("").append(names.get(3)).append(" { x=").append(positions[3].getX()).append(", y=").append(positions[3].getY()).append(", z=").append(positions[3].getZ()).append(" vx=").append(velocities[3].getX()).append(", vy=").append(velocities[3].getY()).append(", vz=").append(velocities[3].getZ()).append(" }\n");
//        return s.toString();
//    }

    public String toString() {
        String s = "";
        for (int i = 0; i < positions.length; i++) {
            s += names.get(i)
                    + " { x=" + Renderer.toScreenCoordinates(positions[i].getX())
                    + ", y=" + Renderer.toScreenCoordinates(positions[i].getY())
                    + ", z=" + Renderer.toScreenCoordinates(positions[i].getZ())
                    + " vx=" + velocities[i].getX()
                    + ", vy="+ velocities[i].getY()
                    + ", vz="+ velocities[i].getZ()+" }\n";
        }
        return s;
    }

    public static void setMass(double[] mass) {
        State.mass = mass;
    }

    //TODO add name of the probe
    public static void setNames() {
        names = new HashMap<>();
        for (int i = 0; i < SpaceObjectBuilder.spaceObjects.size(); i++) {
            names.put(i, SpaceObjectBuilder.spaceObjects.get(i).getName());
        }
    }

    public State copy(){
        Vector3d [] positions = new Vector3d[this.positions.length];
        Vector3d [] velocities = new Vector3d[this.velocities.length];
        for (int i = 0; i < this.positions.length; i++){
            positions[i] = this.positions[i];
            velocities[i] = this.velocities[i];
        }
        return new State(positions, velocities, 0);
    }

    public static void setRadius(double [] radius){
        State.radius = radius;
    }

    public Vector3d[] getPosition(){
        return positions;
    }

    public Vector3d[] getVelocities(){
        return velocities;
    }

    public double getTime() {
        return this.time;
    }
}
