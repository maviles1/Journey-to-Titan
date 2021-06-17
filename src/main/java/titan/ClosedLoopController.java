package titan;

import titan.Vector3d;

import java.util.HashMap;
public class ClosedLoopController {
    HashMap <Integer, Vector3d> states;

    public void add(int index, Vector3d thrust){
        states.put(index, thrust);
    }

    public Vector3d thrust(int index){
        return states.get(index);
    }

}
