package client;

import java.util.ArrayList;

public class Session {
    private final String firstName;
    private final String lastName;
    private final int    userID;
    private final String IP;

    public ArrayList<Contact> contacts;

    public Session(int id, String firstName, String lastName, String IP) {
        this.firstName = firstName;
        this.lastName  = lastName;
        this.userID    = id;
        this.IP        = IP;
        contacts = new ArrayList<Contact>();
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

}
