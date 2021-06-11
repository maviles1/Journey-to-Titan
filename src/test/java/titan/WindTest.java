package titan;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import titan.flight.Vector3d;
import titan.landing.WindModel;


class WindTest
{
    @Test
    void testGetX() {
        Vector3d v = new Vector3d(1, 2, 3);
        WindModel wm = new WindModel();
        assertEquals(1.06742, wm.RefactorVector( v, 10, 20, 30).getX() );
    }

//    @Test
//    void testSetX() {
//        Vector3dInterface v = new Vector3d();
//        v.setX(-1.1);
//        assertEquals(-1.1, v.getX());
//    }



}
