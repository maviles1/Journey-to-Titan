package titan;

public class FeedbackController {

    //requirement is that the probe is in titan orbit
    // we check the distance between titans center and the probe and determine the direction of the vector
    // we then thrust in this direction based on the size of the distance and the probes velocity
    // related to titan ->  (to decrease the distance)
    // we repeat this step until we reach a point where ->
    // the probes distance from titan is titans radius + X (certain value)

    public FeedbackController(State startstate, Probe ship)
    {
        //Step 1
        //Determine strength of thrust vector

    }

    public Vector3d thruster(double dis, Vector3d probevel)
    {


        return new Vector3d(1, 1,1);
    }



}
