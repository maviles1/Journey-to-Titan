package titan;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import titan.interfaces.Vector3dInterface;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.System;
import titan.interfaces.StateInterface;


public class TestSolver {


    //EULER passes all tests with ACCURACY of 1e5
    //VERLET passes all tests with ACCURACY of 1e4
    //RK_KUTTA passes all tests with ACCURACY of 1e3
    static final double ACCURACY = 1e4; // 1 meter (might need to tweak that)

    @Test
    public void testSolverOneDayX() {
        Vector3dInterface[] trajectory = simulateOneDay();
        // 2020-Apr-02 00:00:00.0000
        double x1 = 6.335748551935359E+08; // reference implementation

        assertEquals(x1, trajectory[1].getX(), ACCURACY); // delta +-ACCURACY

    }

    @Test
    public void testSolverOneDayY() {
        Vector3dInterface[] trajectory = simulateOneDay();
        // 2020-Apr-02 00:00:00.0000
        double y1 = -1.357822481323319E+09; // reference implementation
        assertEquals(y1, trajectory[1].getY(), ACCURACY); // delta +-ACCURACY

    }

    @Test
    public void testSolverOneDayZ() {

        Vector3dInterface[] trajectory = simulateOneDay();
        // 2020-Apr-02 00:00:00.0000
        double z1 = -1.612884301640511E+06; // reference implementation
        assertEquals(z1, trajectory[1].getZ(), ACCURACY); // delta +-ACCURACY

    }

    @Test
    public void testSolverOneYearX() {

        Vector3dInterface[] trajectory = simulateOneYear();
        // 2021-Apr-01 00:00:00
        double x366 = 8.778981939996121E+08; // reference implementation
        assertEquals(x366, trajectory[365].getX(), ACCURACY); // delta +-ACCURACY

    }

    @Test
    public void testSolverOneYearY() {

        Vector3dInterface[] trajectory = simulateOneYear();
        // 2021-Apr-01 00:00:00
        double y366 = -1.204478262290766E+09; // reference implementation
        assertEquals(y366, trajectory[365].getY(), ACCURACY); // delta +-ACCURACY

    }

    @Test
    public void testSolverOneYearZ() {

        Vector3dInterface[] trajectory = simulateOneYear();
        // 2021-Apr-01 00:00:00
        double z366 = -1.400829719307184E+07; // reference implementation
        assertEquals(z366, trajectory[365].getZ(), ACCURACY); // delta +-ACCURACY
    }


    public static Vector3dInterface[] simulateOneDay() {
        double day = 24*60*60;

        ProbeSimulator p = new ProbeSimulator();
        State universe = p.initUn();
        Solver solver = new Solver(new RKSolver());
        StateInterface[] s = solver.solve(new ODEFunction(), universe, day, day);

        Vector3d[] planet = new Vector3d[s.length];
        for(int i=0;i<s.length;i++){
            State ph = (State) s[i];
            planet[i] = ph.getPosition()[7].mul(0.001);
        }
        return planet;
    }


    public static Vector3dInterface[] simulateOneYear() {
        double day = 24*60*60;
        double year = 365*day; //changed to 365

        ProbeSimulator p = new ProbeSimulator();
        State universe = p.initUn();
        Solver solver = new Solver(new EulerSolver());
        StateInterface[] s = solver.solve(new ODEFunction(), universe, year, day);


        Vector3d[] planet = new Vector3d[s.length];
        for(int i=0;i<s.length;i++){
            State ph = (State) s[i];
            planet[i] = ph.getPosition()[7].mul(0.001);
        }

        return planet;
    }

}
