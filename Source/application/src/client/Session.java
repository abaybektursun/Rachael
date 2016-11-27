package client;

import java.util.ArrayList;

public class Session {
    private final String firstName;
    private final String lastName;
    private final int defaultPort;
    private final int    userID;
    private final String IP;

    private int     status;
    private boolean callAccepted;

    public final int OFFLINE = 0;
    public final int AVAILABLE = 1;
    public final int BUSY = 2;


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
