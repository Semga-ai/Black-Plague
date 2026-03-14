package name.modid.beton.storage.logic.model;

public enum States {
    NONE(0,10),
    FIRST(10,25),
    SECOND(25,30),
    THIRD(30,35),
    FOURTH(35);


    private final int max,min;

    States(int min,int max) {
        this.min = min;
        this.max = max;
    }

    States(int min) {
        this.min = min;
        this.max = 5000;
    }


    public static States getCurrentState(int val) {
        for (States states : States.values()) {
            if (val >= states.min && val < states.max) return states;
        }
        return States.NONE;
    }
}
