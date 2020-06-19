package business_logic;

import database.Repository;
import database.RepositoryInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Entry {
    public enum EntryState {
        OPENED, EXECUTING, CLOSED, CANCELED;

        public static String entryStateToString(EntryState st){
            switch (st){
                case OPENED:
                    return "OPENED";
                case EXECUTING:
                    return "EXECUTING";
                case CLOSED:
                    return "CLOSED";
                case CANCELED:
                    return "CANCELED";
                default:
                    return null;
            }
        }

        public static EntryState stringToEntryState(String st){
            switch (st){
                case "OPENED":
                    return OPENED;
                case "EXECUTING":
                    return EXECUTING;
                case "CLOSED":
                    return CLOSED;
                case "CANCELED":
                    return CANCELED;
                default:
                    return null;
            }
        }
    }

    private Integer id;
    private Calendar date;
    private Master master;
    private List<Service> services;
    private Client client;
    private EntryState state;
    protected RepositoryInterface repo;

    public Entry(Integer id, Calendar  date, Master master, List<Service> services, Client client, EntryState state){
        this.id = id;
        this.date = date;
        this.master = master;
        this.services = services;
        this.client = client;
        this.state = state;
        repo = Repository.getInstance();
    }

    public Entry(Calendar date, Master master, List<Service> services, Client client, EntryState state){
        this.date = date;
        this.master = master;
        this.services = services;
        this.client = client;
        this.state = state;
        repo = Repository.getInstance();
    }

    public Entry(Calendar date, Master master, List<Service> services, Client client){
        this.date = date;
        this.master = master;
        this.services = services;
        this.client = client;
        this.state = EntryState.OPENED;
        repo = Repository.getInstance();
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public Calendar getDate(){
        return date;
    }

    public void setDate(Calendar date){
        this.date = date;
    }

    public Master getMaster(){
        return master;
    }

    public List<Service> getServices(){
        return services;
    }

    public Client getClient(){
        return client;
    }

    public void setClient(Client client){
        this.client = client;
    }

    public EntryState getState(){
        return state;
    }

    public void setState(EntryState state){
        this.state = state;
    }

    public Calendar countEntryEnd(){
        Calendar end = new GregorianCalendar();
        end.setTime(date.getTime());
        Integer minutes = 0;
        for(Service s: services){
            minutes += s.getLength();
        }
        end.add(Calendar.MINUTE, minutes);
        return end;
    }

    @Override
    public String toString() {
        DateFormat format = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        String date = "Время: " + format.format(this.date.getTime());
        String master = "мастер: " + this.master;
        String client = "клиент: " + this.client;
        return date + "; " + client + "; " + master;
    }

    public Boolean addEntry(){
        if(client.getId() == null) {
            Client cl = repo.getClient(this.getClient().getPhone());
            if (cl == null)
                cl = repo.addClient(this.getClient());
            if (cl == null)
                return false;
            this.setClient(cl);
        }
        Entry entry = repo.addEntry(this);
        if (entry == null)
            return false;
        this.id = entry.id;
        return true;
    }
}
