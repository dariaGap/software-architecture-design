package database;

import business_logic.*;

import java.util.Calendar;
import java.util.List;

public class Repository implements RepositoryInterface {
    private static MasterMapper masterMapper;
    private static ServiceMapper serviceMapper;
    private static EntryMapper entryMapper;
    private static ClientMapper clientMapper;
    private static AdminMapper adminMapper;
    private static ComplaintMapper complaintMapper;

    private static Repository repository;

    public static Repository getInstance(){
        if (repository == null) {
            repository = new Repository();
            if (masterMapper == null)
                masterMapper = MasterMapper.getInstance();
            if (serviceMapper == null)
                serviceMapper = serviceMapper.getInstance();
            if (entryMapper == null)
                entryMapper = entryMapper.getInstance();
            if (clientMapper == null)
                clientMapper = clientMapper.getInstance();
            if (adminMapper == null)
                adminMapper = adminMapper.getInstance();
            if (complaintMapper == null)
                complaintMapper = complaintMapper.getInstance();
        }
        return repository;
    }


    public Client addClient(Client client){
        return clientMapper.addClient(client);
    }

    public Client getClient(Integer id){
        return clientMapper.getById(id);
    }

    public Client getClient(String phone){
        return clientMapper.getByPhone(phone);
    }

    public Boolean removeClient(Client client){
        return clientMapper.remove(client.getId());
    }


    public Master addMaster(Master master){
        return masterMapper.addMaster(master);
    }

    public Master getMaster(Integer id){
        return masterMapper.getById(id);
    }

    public Master getMaster(String login){
        return masterMapper.getByLogin(login);
    }

    public List<Master> getMasters(){
        return masterMapper.getAll();
    }

    public List<Master> getMasters(Service.ServiceType service){
        return masterMapper.getByServiceType(service);
    }

    public Boolean removeMaster(Master master){
        return masterMapper.remove(master.getId());
    }


    public Admin addAdmin(Admin admin){
        return adminMapper.addAdmin(admin);
    }

    public Admin getAdmin(String login){
        return adminMapper.getByLogin(login);
    }

    public Boolean removeAdmin(Admin admin){
        return adminMapper.remove(admin.getId());
    }


    public Service addService(Service service){
        return serviceMapper.addService(service);
    }

    public List<Service> getServices(){
        return serviceMapper.getAll();
    }

    public List<Service> getServices(Service.ServiceType service){
        return serviceMapper.getByServiceType(service);
    }

    public Boolean removeService(Service service){
        return serviceMapper.remove(service.getId());
    }


    public Entry addEntry(Entry entry){
        return entryMapper.addEntry(entry);
    }

    public Boolean updateEntry(Entry entry){
        return entryMapper.updateEntry(entry);
    }

    public Entry getEntry(Integer id){
        return entryMapper.getById(id);
    }

    public List<Entry> getEntries(){
        return entryMapper.getAll();
    }

    public List<Entry> getEntries(Master master){
        return entryMapper.getByMaster(master);
    }

    public List<Entry> getEntries(Calendar date){
        return entryMapper.getByDate(date);
    }

    public List<Entry> getEntries(Master master, Calendar date){
        return entryMapper.getByMasterAndDate(date, master);
    }

    public List<Entry> getEntries(Master master, Calendar date, Entry.EntryState state){
        return entryMapper.getByMasterStateAndDate(date, state, master);
    }

    public Boolean removeEntry(Entry entry){
        return entryMapper.remove(entry.getId());
    }


    public Complaint addComplaint(Complaint complaint){
        return complaintMapper.addComplaint(complaint);
    }

    public Boolean updateComplaint(Complaint complaint){
        return complaintMapper.updateComplaint(complaint);
    }

    public Complaint getComplaint(Integer id){
        return complaintMapper.getById(id);
    }

    public List<Complaint> getComplaints(Complaint.ComplaintState state){
        return complaintMapper.getByState(state);
    }

    public Boolean removeComplaint(Complaint complaint){
        return complaintMapper.remove(complaint.getId());
    }
}
