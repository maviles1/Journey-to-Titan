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
        assertEquals(2.0970401199802953, wm.Refactor3DVector( v, 10, 20, 30).getX() );
    }

    @Test
    void testGetY() {
        Vector3d v = new Vector3d(1, 2, 3);
        WindModel wm = new WindModel();
        assertEquals(0.6053953180956588, wm.Refactor3DVector( v, 10, 20, 30).getY() );
    }

    @Test
    void testGetZ() {
        Vector3d v = new Vector3d(1, 2, 3);
        WindModel wm = new WindModel();
        assertEquals(3.039065521508361, wm.Refactor3DVector( v, 10, 20, 30).getZ() );
    }

    //    5.9767     12.8171
    @Test
    void testGet2DX() {
        Vector3d v = new Vector3d(10, 10, 0);
        WindModel wm = new WindModel();
        assertEquals(5.976724774602398, wm.refactor2DVector( v, 20).getX() );
    }

    @Test
    void testGet2DY() {
        Vector3d v = new Vector3d(10, 10, 0);
        WindModel wm = new WindModel();
        assertEquals(12.817127641115771, wm.refactor2DVector( v, 20).getY() );
    }

    @Test
    void testWindVector()
    {
        WindModel wm = new WindModel();
        Vector3d tv = wm.getStartingWindVector(100000);
        Vector3d rv1 = wm.CreateNewWindVector(tv,100000);
        Vector3d rv2 = wm.CreateNewWindVector(tv,100000);

        assertEquals(1, Math.round(rv1.norm()/rv2.norm()) );
    }

    @Test
    void testWindForceVector()
    {
        WindModel wm = new WindModel();
        Vector3d tv = wm.getStartingWindVector(100000);
        assertEquals(1, wm.getForceVector(tv, 100000));
    }

    @Test
    void testStartingVector()
    {
        WindModel wm = new WindModel();
        Vector3d tv = wm.getStartingWindVector(100);
        assertEquals(wm.getStartingWindVector(100), wm.getStartingWindVector(100));
    }

}
