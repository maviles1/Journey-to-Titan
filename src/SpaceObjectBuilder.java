import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Vector;

public class SpaceObjectBuilder {

    private ArrayList<SpaceObject> objects = new ArrayList<>();

    public SpaceObjectBuilder(String source){
        ArrayList<SpaceObject> objects = new ArrayList<>();
        File file = new File(source);
        try {
            Scanner in = new Scanner(file);
            while (in.hasNext()){
                HashMap <String, Double>arguments = new HashMap<>();
                String line = in.nextLine();
                String [] lines = line.split(":");
                String name = lines[0];
                String [] args = lines[1].split(",");
                for (int i = 0; i < args.length; i++){
                    arguments.put(args[i].split("=")[0].replaceAll("[\\W]", ""),Double.parseDouble(args[i].split("=")[1].replaceAll("[\\W]", "")));
                }
                Vector3d pos = new Vector3d(arguments.get("x"), arguments.get("y"), arguments.get("z"));
                Vector3d vel = new Vector3d(arguments.get("vx"), arguments.get("vy"), arguments.get("vz"));
//                Planet planet = new Planet(name, arguments.get("mass"), pos,vel);
//                objects.add(planet);
                this.objects = objects;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static double standardize(double d){
        return d/1000000000/1000000000/7000000;
    }

    public ArrayList<SpaceObject> getSpaceObjects(){
        return objects;
    }

    public static void main(String [] args){
        SpaceObjectBuilder builder = new SpaceObjectBuilder("src/solar_system_data-2020_04_01.txt");
        System.out.println(builder.getSpaceObjects().get(1).getPosition().getY());
    }
}
