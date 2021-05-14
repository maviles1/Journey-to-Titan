package titan;

import titan.interfaces.ProbeSimulatorInterface;
import titan.interfaces.StateInterface;
import titan.interfaces.Vector3dInterface;

import java.io.File;
import java.util.Scanner;

public class ProbeSim implements ProbeSimulatorInterface {

    private Solver solver;
    private StateInterface[] states;
    static final int EARTH_INDEX = 3;
    static final int PROBE_INDEX = 11;
    static final int AMOUNT_OF_BODIES = 12;

    public ProbeSim() {
       this(new Solver(new EulerSolver()));
    }

    public ProbeSim(Solver solver) {
        this.solver = solver;
    }

    @Override
    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double[] ts) {
        return new Vector3dInterface[0];
    }

    @Override
    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double tf, double h) {
        //fresh copy of data from file
        State initialState = readData();
        //conversion of the input relative to the Solar System barycentre
        Vector3d p = (Vector3d) p0.add(initialState.getPosition()[EARTH_INDEX]); //earth = 3
        Vector3d v = (Vector3d) v0.add(initialState.getVelocities()[EARTH_INDEX]);

        initialState.getPosition()[PROBE_INDEX] = p;
        initialState.getVelocities()[PROBE_INDEX] = v;

        states = solver.solve(new ODEFunction(), initialState, tf, h);
        Vector3d[] trajectory = new Vector3d[states.length];

        for (int i = 0; i < states.length; i++) {
            State ph = (State) states[i];
            trajectory[i] = ph.getPosition()[PROBE_INDEX];
        }

        return trajectory;
    }

    public StateInterface[] getStates() {
        return this.states;
    }

    public State readData() {
        try {
            String url = getClass().getResource("/solar_system_data-2020_04_01.txt").getPath();
            File file = new File(url);
            Scanner scanner = new Scanner(file);
            Vector3d[] positions = new Vector3d[AMOUNT_OF_BODIES];
            Vector3d[] velocities = new Vector3d[AMOUNT_OF_BODIES];
            double earthRadius = 0;
            double titanRadius = 0;
            double[] masses = new double[AMOUNT_OF_BODIES];
            for (int i = 0; scanner.hasNextLine(); i++) {
                //strip the leading name and curly bracket as well as all the whitespace
                String line = scanner.nextLine().replaceFirst("(\\w+):\\s\\{\\s", "").replaceAll("\\s", "");
                String[] data = line.split(",");    //split line into array on commas to extract data

                //remove the substring "mass=" to extract just the number
                double mass = Double.parseDouble(data[0].replaceFirst("\\w+=", ""));

                int offset = 0; //earth and titan have radius, so one extra element in data string
                if (i == 3 || i == 8) { //earth or titan
                    earthRadius = (i==3)?Double.parseDouble(data[1].replaceFirst("\\w+=", "")):0;
                    titanRadius = (i==8)?Double.parseDouble(data[1].replaceFirst("\\w+=", "")):0;
                    offset++;
                }

                //maybe this could have been done in a loop but i'm lazy
                double x = Double.parseDouble(data[1 + offset].replaceFirst("\\w+=", ""));
                double y = Double.parseDouble(data[2 + offset].replaceFirst("\\w+=", ""));
                double z = Double.parseDouble(data[3 + offset].replaceFirst("\\w+=", ""));
                double vx = Double.parseDouble(data[4 + offset].replaceFirst("\\w+=", ""));
                double vy = Double.parseDouble(data[5 + offset].replaceFirst("\\w+=", ""));
                double vz = Double.parseDouble(data[6 + offset].replaceFirst("\\w+=", "").replace("}", ""));

                Vector3d pos = new Vector3d(x, y, z);
                Vector3d vel = new Vector3d(vx, vy, vz);
                positions[i] = pos;
                velocities[i] = vel;
                masses[i] = mass;
            }

            masses[PROBE_INDEX] = 15000;
            State.setMass(masses);
            State.setNames();
            State.setRadius(new double[]{700000, 2439.7, 6051.8, earthRadius, 1737.1, 3389.5, 69911, 58232, titanRadius, 25362, 2462.2, 10, 1});
            return new State(positions, velocities, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
