package client;


import java.io.Serializable;
import java.util.ArrayList;

public class Session implements Serializable {
    private final String firstName;
    private final String lastName;
    private final int defaultPort;
    private final int    userID;
    private final String IP;

    private int     status;
    private boolean callAccepted;

    public static final int OFFLINE = 0;
    public static final int AVAILABLE = 1;
    public static final int BUSY = 2;

    public static final int CODE_CALL_REQUEST = 44;
    public static final int CODE_ROLL_BACK_CALL_REQUEST = 43;

    public static final int CODE_ACCEPTED_FRAME = 46;

    public static final int NO_RESPONSE = 10;
    public static final int ACCEPTED    = 12;
    public static final int DECLINED    = 23;



    public ArrayList<Contact> contacts;

    public Session(int id, String firstName, String lastName, String IP) {
        this.firstName = firstName;
        this.lastName  = lastName;
        this.userID    = id;
        this.IP        = IP;

        this.contacts = new ArrayList<Contact>();

        this.status = 1;

        this.callAccepted = false;
        this.defaultPort = 2612;
    }

    public String getIP() {
        return IP;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getUserID() {
        return userID;
    }


    public void setCallAccepted(boolean callAccepted) {
        this.callAccepted = callAccepted;
    }

    public int getDefaultPort() {
        return defaultPort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isCallAccepted() {
        return callAccepted;
    }

    public void busy()
    {
        this.status = BUSY;
    }

    public void available()
    {
        this.status = AVAILABLE;
    }
}
