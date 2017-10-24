package hansyuan.cruzrojamobile;

import java.util.ArrayList;

/**
 * Created by dchickey on 10/24/2017.
 * Represents an ambulance pulled from the database
 */

public class Ambulance {
    private Integer id;
    private String licensePlate;


    public Ambulance(Integer id, String licensePlate) {
        this.id = id;
        this.licensePlate = licensePlate;
    }

    /**
     * Getter for the license plate of the ambulance
     * @return the license plate String
     */
    public String getLicensePlate() { return licensePlate; }

    //public void setLicensePlate(String license) { this.licensePlate = license; }

    public int getId() { return id; }

    //public void setId(int id) { this.id = id; }
}

