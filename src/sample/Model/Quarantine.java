package sample.Model;

import java.util.ArrayList;

/**
 * This is the Quarantine class for the quarantine slot
 */
public class Quarantine extends Cell{

    //properties
    private String message;
    private ArrayList<Player> patients;

    //constructers
    public Quarantine(String name, double x, double y){
        this.name = name;
        this.message = "Unfornutanelty, Your Covid-19 Test is POSITIVE";
        patients = new ArrayList<>();
        this.visitors = new ArrayList<>();
        this.x = x;
        this.y = y;
    }
    public Quarantine( String name, String message, ArrayList<Player> patients){
        this.name = name;
        this.message = message;
        this.patients = patients;
        this.visitors = new ArrayList<>();
    }

    //methods
    public String getMessage() {

        return message;
    }

    public void setMessage( String message) {

        this.message = message;
    }

    public void setPatients( ArrayList<Player> patients) {

        this.patients = patients;
    }

    public ArrayList<Player> getPatients() {
        return patients;
    }
}
