package titan;

public class Solver implements ODESolverInterface{
    @Override
    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double[] ts) {
        StateInterface[] s = new StateInterface[ts.length];
        s[0]=step(f,ts[0],y0,ts[0]);
        for(int i=1;i<ts.length;i++){
            double stepSize=ts[i]-ts[i-1];
            s[i]=step(f,ts[i],s[i-1],stepSize);
        }
        return s;
    }

    @Override
    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double tf, double h) {
        //check if t should be the total time
        int size= (int) (tf/h);
        StateInterface[] s = new StateInterface[size];
        s[0]=step(f,0,y0,h);
        double t=0;
        for(int i=1;i<size-1;i++){
            t+=h;
            s[i]=step(f,t,s[i-1],h);
        }
        //tf or tf/h
        s[size-1]=step(f,tf,s[size-2],tf%h);
        return s;
    }

    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {

        RateInterface r = f.call(t, y);
        return y.addMul(h,r);
    }
}
