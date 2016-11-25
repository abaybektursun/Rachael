package client;

public class Contact{
    public final String firstName;
    public final String lastName;
    public final int status;
    public final int userID;
    public final String IP;

    public Contact(String firstName, String lastName, int status, int userID, String IP) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.userID = userID;
        this.IP = IP;
    }
}