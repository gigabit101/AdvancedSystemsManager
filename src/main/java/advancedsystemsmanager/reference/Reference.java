package advancedsystemsmanager.reference;

public class Reference
{

    // User friendly version of our mods name.
    public static final String NAME = "Advanced Systems Manager";

    // Internal mod name used for reference purposes and resource gathering.
    public static final String ID = "AdvancedSystemsManager";

    //Shorter string for network channel names
    public static final String NETWORK_ID = "ASM";

    public static final String RESOURCE_LOCATION = ID.toLowerCase();
    public static final String ASSETS = "/assets/" + RESOURCE_LOCATION + "/";
    public static final String THEMES = ASSETS + "themes/";

    // Main version information that will be displayed in mod listing and for other purposes.
    public static final String V_MAJOR = "@MAJOR@";
    public static final String V_MINOR = "@MINOR@";
    public static final String V_REVIS = "@REVIS@";
    public static final String VERSION_FULL = V_MAJOR + "." + V_MINOR + "." + V_REVIS;
}
