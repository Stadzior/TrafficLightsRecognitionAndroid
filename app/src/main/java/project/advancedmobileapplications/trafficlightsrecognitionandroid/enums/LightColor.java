package project.advancedmobileapplications.trafficlightsrecognitionandroid.enums;

/**
 * Created by patry on 15.10.2017.
 */

public enum LightColor {

    GREEN(1),
    RED(2),
    MISS(3);

    private int id;

    LightColor(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}