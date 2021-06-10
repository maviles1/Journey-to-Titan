package titan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class CSVReader {

    Vector3d [] probePositions;
    public CSVReader(String filePath){
        probePositions = new Vector3d[367];
        try{
            File file = new File(filePath);
            FileReader reader = new FileReader(file);
            Scanner in = new Scanner(reader);
            in.nextLine();
            while (in.hasNext()){
                String [] coordinates = in.nextLine().split(",");
                probePositions[Integer.parseInt(coordinates[0])] = new Vector3d(Double.parseDouble(coordinates[1]),Double.parseDouble(coordinates[2]), Double.parseDouble(coordinates[3]));
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Vector3d [] getProbePositions(){
        return probePositions;
    }
}
