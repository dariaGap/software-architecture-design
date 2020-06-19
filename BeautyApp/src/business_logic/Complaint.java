package business_logic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Complaint {
    public enum ComplaintState { OPENED, WAIT_EXPLANATORY, WAIT_DECISION, CLOSED;
        public static String complaintStateToString(ComplaintState st){
            switch (st){
                case OPENED:
                    return "OPENED";
                case WAIT_EXPLANATORY:
                    return "WAIT_EXPLANATORY";
                case CLOSED:
                    return "CLOSED";
                case WAIT_DECISION:
                    return "WAIT_DECISION";
                default:
                    return null;
            }
        }

        public static ComplaintState stringToComplaintState(String st){
            switch (st){
                case "OPENED":
                    return OPENED;
                case "WAIT_EXPLANATORY":
                    return WAIT_EXPLANATORY;
                case "CLOSED":
                    return CLOSED;
                case "WAIT_DECISION":
                    return WAIT_DECISION;
                default:
                    return null;
            }
        }
    }

    private Integer id;
    private Entry entry;
    private String text;
    private String explanatory;
    private String decisionMaster;
    private String decisionClient;
    private ComplaintState state;

    public Complaint(Integer id, Entry entry, String text, String explanatory, String decisionMaster,
                     String decisionClient, ComplaintState state){
        this.id = id;
        this.entry = entry;
        this.text = text;
        this.explanatory = explanatory;
        this.decisionMaster = decisionMaster;
        this.decisionClient = decisionClient;
        this.state = state;
    }

    public Complaint(Entry entry, String text){
        this.entry = entry;
        this.text = text;
        this.state = ComplaintState.OPENED;
    }

    public void setExplanatory(String expl){
        explanatory = expl;
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText(){
        return text;
    }

    public Entry getEntry(){
        return entry;
    }

    public String getExplanatory(){
        return explanatory;
    }

    public ComplaintState getState(){
        return state;
    }

    public void setState(ComplaintState state){
        this.state = state;
    }

    public String getDecisionMaster(){
        return decisionMaster;
    }

    public String getDecisionClient(){
        return decisionClient;
    }

    @Override
    public String toString() {
        DateFormat format = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        String date = "Время: " + format.format(this.entry.getDate().getTime());
        String master = "мастер: " + this.entry.getMaster();
        String client = "клиент: " + this.entry.getClient();
        return date + "; " + client + "; " + master;
    }
}
