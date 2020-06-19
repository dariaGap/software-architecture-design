package business_logic;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Master extends Employee {

    private String name;
    private Service.ServiceType services;

    public Master(){}

    public Master(String name, String login, String password, Service.ServiceType services){
        super(login,password, EmployeeType.MASTER);
        this.name = name;
        this.services = services;
    }

    public Master(Integer id, String name, String login, String password, Service.ServiceType services){
        super(id, login, password, EmployeeType.MASTER);
        this.name = name;
        this.services = services;
    }

    public String getName(){
        return name;
    }

    public Service.ServiceType getServices(){
        return services;
    }

    @Override
    public String toString() {
        return this.name + " (" + Service.ServiceType.serviceTypeToString(this.services) + ")";
    }

    public List<String> getFreeTime(List<String> posibleTime, DateFormat format, Calendar date){
        List<Entry> masterEntries = repo.getEntries(this, date);
        List<String> busyTimes = new ArrayList<>();

        for (Entry entry: masterEntries){
            for(String t : posibleTime){
                try {
                    Calendar calendarTime = new GregorianCalendar();
                    calendarTime.setTime(date.getTime());
                    Calendar time = new GregorianCalendar();
                    time.setTime(format.parse(t));
                    calendarTime.add(Calendar.HOUR_OF_DAY,time.get(Calendar.HOUR_OF_DAY));
                    calendarTime.add(Calendar.MINUTE,time.get(Calendar.MINUTE));

                    if ((entry.getDate().compareTo(calendarTime)<=0)&&(entry.countEntryEnd().compareTo(calendarTime)>0)){
                        busyTimes.add(t);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        posibleTime.removeAll(busyTimes);
        return posibleTime;
    }

    public List<Service> getPossibleServices(){
        return repo.getServices(services);
    }

    public Boolean addExplanatory(Complaint complaint, String explanatory){
        if(complaint.getState().equals(Complaint.ComplaintState.WAIT_EXPLANATORY)) {
            complaint.setExplanatory(explanatory);
            complaint.setState(Complaint.ComplaintState.WAIT_DECISION);
            return repo.updateComplaint(complaint);
        }
        return false;
    }
}
