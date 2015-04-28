package Misc;

/**
 * Created by Jorgen on 28/04/15.
 */
public class Config {
    public static final String COORD_ADRESS = "rmi://localhost:2020/coordinatorImpl";

    public static final String CAR_DB_ADRESS= "jdbc:mysql://10.0.1.7:8889/Bil";
    public static final String CAR_DB_USER = "db";
    public static final String CAR_DB_PASS = "db";

    public static final String FLIGHT_DB_ADRESS= "jdbc:mysql://10.0.1.7:8889/Fly";
    public static final String FLIGHT_DB_USER = "db";
    public static final String FLIGHT_DB_PASS = "db";

    public static final int THREAD_WAIT_TIME = 200;
}