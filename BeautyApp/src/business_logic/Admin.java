package business_logic;

public class Admin extends Employee {
    public Admin(){ }

    public Admin(String name, String password){
        super(name,password, EmployeeType.ADMIN);
    }

    public Admin(Integer id, String name, String password){
        super(id, name,password, EmployeeType.ADMIN);
    }

    public Boolean transferEntry(Entry entry){
        entry.setState(Entry.EntryState.EXECUTING);
        return repo.updateEntry(entry);
    }

    public Boolean transferComplaint(Complaint complaint){
        complaint.setState(Complaint.ComplaintState.WAIT_EXPLANATORY);
        return repo.updateComplaint(complaint);
    }
}
