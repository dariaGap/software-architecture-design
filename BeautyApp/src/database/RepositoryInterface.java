package database;

import business_logic.*;

import java.util.Calendar;
import java.util.List;

public interface RepositoryInterface {

    Client addClient(Client client);
    Client getClient(Integer id);
    Client getClient(String phone);
    Boolean removeClient(Client client);

    Master addMaster(Master master);
    Master getMaster(Integer id);
    Master getMaster(String login);
    List<Master> getMasters();
    List<Master> getMasters(Service.ServiceType service);
    Boolean removeMaster(Master master);

    Admin addAdmin(Admin admin);
    Admin getAdmin(String login);
    Boolean removeAdmin(Admin admin);

    Service addService(Service service);
    List<Service> getServices();
    List<Service> getServices(Service.ServiceType service);
    Boolean removeService(Service service);

    Entry addEntry(Entry entry);
    Boolean updateEntry(Entry entry);
    Entry getEntry(Integer id);
    List<Entry> getEntries();
    List<Entry> getEntries(Master master);
    List<Entry> getEntries(Calendar date);
    List<Entry> getEntries(Master master, Calendar date);
    List<Entry> getEntries(Master master, Calendar date, Entry.EntryState state);
    Boolean removeEntry(Entry entry);

    Complaint addComplaint(Complaint complaint);
    Boolean updateComplaint(Complaint complaint);
    Complaint getComplaint(Integer id);
    List<Complaint> getComplaints(Complaint.ComplaintState state);
    Boolean removeComplaint(Complaint complaint);
}
