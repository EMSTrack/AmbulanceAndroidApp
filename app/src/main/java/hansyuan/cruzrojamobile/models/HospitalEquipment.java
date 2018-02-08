package hansyuan.cruzrojamobile.models;

/**
 * Created by devinhickey on 1/16/18.
 */

public class HospitalEquipment {

    private String name;
    private String value;

    public HospitalEquipment(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getEntry() {
        return name + " : " + value;
    }
}
