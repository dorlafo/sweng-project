package ch.epfl.sweng.jassatepfl.constants;


public enum AppActivity {

    MAIN("Welcome Screen"), CREATE_MATCH("Create Match"),
    MAPS("Match Map"), MATCH_LIST("Match List"),
    USER_PROFILE("Profile");

    private final String activityName;

    AppActivity(String activityName) {
        this.activityName = activityName;
    }

    @Override
    public String toString() {
        return activityName;
    }

}
