package titan;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import titan.flight.Vector3d;
import titan.landing.WindModel;


class WindTest
{
    //(2.0970401199802953,0.6053953180956588,3.039065521508361)
    @Test
    void testGetX() {
        Vector3d v = new Vector3d(1, 2, 3);
        WindModel wm = new WindModel();
        assertEquals(2.0970401199802953, wm.RefactorVector( v, 10, 20, 30).getX() );
    }

    @Test
    void testGetY() {
        Vector3d v = new Vector3d(1, 2, 3);
        WindModel wm = new WindModel();
        assertEquals(0.6053953180956588, wm.RefactorVector( v, 10, 20, 30).getY() );
    }

    @Test
    void testGetZ() {
        Vector3d v = new Vector3d(1, 2, 3);
        WindModel wm = new WindModel();
        assertEquals(3.039065521508361, wm.RefactorVector( v, 10, 20, 30).getZ() );
    }

    @Test
    void testWindVector()
    {
        WindModel wm = new WindModel();
        Vector3d tv = wm.getStartingWindVector(100000);
        System.out.println(tv.toString());
        assertEquals(1, wm.CreateWindVector(tv, 100000));
    }

    @Test
    void testStartingVector()
    {
        WindModel wm = new WindModel();
        Vector3d tv = wm.getStartingWindVector(100);
        assertEquals(wm.getStartingWindVector(100), wm.getStartingWindVector(100));
    }

}
