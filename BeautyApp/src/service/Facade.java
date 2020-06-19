package service;

import business_logic.*;
import database.Repository;
import database.RepositoryInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class Facade implements FacadeInterface{
    private RepositoryInterface repo;
    private static Facade facade;

    public Facade() {
        repo = Repository.getInstance();
    }

    public static Facade getInstance(){
        if (facade == null) {
            facade = new Facade();
        }
        return facade;
    }

    public ObservableList<Master> getMastersNames(){
        List<Master> masters = repo.getMasters();
        return FXCollections.observableArrayList(masters);
    }

    public ObservableList<Entry> getEntriesByDate(LocalDate localDate){
        Calendar calendar = new GregorianCalendar();
        Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        calendar.setTime(date);
        List<Entry> entries = repo.getEntries(calendar);
        return FXCollections.observableArrayList(entries);
    }

    public ObservableList<Entry> getEntriesForMaster(LocalDate localDate, Integer master_id){
        Calendar calendar = new GregorianCalendar();
        Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        calendar.setTime(date);
        Master master = repo.getMaster(master_id);
        List<Entry> entries = repo.getEntries(master, calendar, Entry.EntryState.EXECUTING);
        return FXCollections.observableArrayList(entries);
    }

    public ObservableList<Master> getMastersNames(Service service) {
        return FXCollections.observableArrayList(repo.getMasters(service.getType()));
    }

    public ObservableList<Service> getServicesNames(){
        return FXCollections.observableArrayList(repo.getServices());
    }

    public ObservableList<Service> getServicesNames(Master master) {
        List<Service> services = master.getPossibleServices();
        return FXCollections.observableArrayList(services);
    }

    public ObservableList<Service> getServicesNames(Service service) {
        List<Service> services = repo.getServices(service.getType());
        return FXCollections.observableArrayList(services);
    }

    public ObservableList<String> getFreeTime(LocalDate localDate, Master master){
        DateFormat format = new SimpleDateFormat("HH:mm");
        Calendar calendar = new GregorianCalendar();
        Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        calendar.setTime(date);
        List<String> time = getAllTime();
        return FXCollections.observableArrayList(master.getFreeTime(time,format,calendar));
    }

    public ObservableList<String> getAllTime(){
        List<String> time = new ArrayList<>();
        for(int i = 10; i<= 18; i++){
            time.add(i + ":00");
            time.add(i + ":30");
        }
        return FXCollections.observableArrayList(time);
    }

    public Boolean saveEntry(String clientName, String clientPhone, Master master, List<Service> services, Calendar date){
        Entry entry = new Entry(date, master,services,new Client(clientName, clientPhone));
        return entry.addEntry();
    }

    public Calendar combineEntryDateTime(String time, LocalDate localDate){
        Calendar calendar = new GregorianCalendar();
        DateFormat format = new SimpleDateFormat("HH:mm");
        Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        calendar.setTime(date);
        try {
            Calendar calendarTime = new GregorianCalendar();
            calendarTime.setTime(format.parse(time));
            calendar.add(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
            calendar.add(Calendar.MINUTE,calendarTime.get(Calendar.MINUTE));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public String getStringTime(Calendar date){
        return date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE);
    }

    public Employee authorize(String login, String password){
        Master master = repo.getMaster(login);
        if((master != null)&&(master.authorize(password))){
            return master;
        }
        Admin admin = repo.getAdmin(login);
        if((admin != null)&&(admin.authorize(password))){
            return admin;
        }
        return null;
    }

    public Boolean transferEntry(Entry entry){
        Admin admin = new Admin();
        return admin.transferEntry(entry);
    }

    public ObservableList<Complaint> getOpenedComplaints(){
        List<Complaint> complaints = repo.getComplaints(Complaint.ComplaintState.OPENED);
        return FXCollections.observableArrayList(complaints);
    }

    public ObservableList<Complaint> getClosedComplaints(){
        List<Complaint> complaints = repo.getComplaints(Complaint.ComplaintState.CLOSED);
        return FXCollections.observableArrayList(complaints);
    }

    public ObservableList<Complaint> getClosedComplaints(Integer masterId){
        List<Complaint> complaints = repo.getComplaints(Complaint.ComplaintState.CLOSED);
        List<Complaint> res = new ArrayList<>();
        for (Complaint complaint: complaints){
            if(complaint.getEntry().getMaster().getId().equals(masterId))
                res.add(complaint);
        }
        return FXCollections.observableArrayList(res);
    }

    public ObservableList<Complaint> getWaitingDecComplaints(){
        List<Complaint> complaints = repo.getComplaints(Complaint.ComplaintState.WAIT_DECISION);
        return FXCollections.observableArrayList(complaints);
    }

    public ObservableList<Complaint> getWaitingDecComplaints(Integer masterId){
        List<Complaint> complaints = repo.getComplaints(Complaint.ComplaintState.WAIT_DECISION);
        List<Complaint> res = new ArrayList<>();
        for (Complaint complaint: complaints){
            if(complaint.getEntry().getMaster().getId().equals(masterId))
                res.add(complaint);
        }
        return FXCollections.observableArrayList(res);
    }

    public ObservableList<Complaint> getWaitingExplComplaints(){
        List<Complaint> complaints = repo.getComplaints(Complaint.ComplaintState.WAIT_EXPLANATORY);
        return FXCollections.observableArrayList(complaints);
    }

    public ObservableList<Complaint> getWaitingExplComplaints(Integer masterId){
        List<Complaint> complaints = repo.getComplaints(Complaint.ComplaintState.WAIT_EXPLANATORY);
        List<Complaint> res = new ArrayList<>();
        for (Complaint complaint: complaints){
            if(complaint.getEntry().getMaster().getId().equals(masterId))
                res.add(complaint);
        }
        return FXCollections.observableArrayList(res);
    }

    public String getComplaintInfo(Complaint complaint){
        String res = "";
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        res += "Дата: " + format.format(complaint.getEntry().getDate().getTime()) + "\n";
        res += "Клиент: " + complaint.getEntry().getClient() + "\n";
        res += "Мастер: " + complaint.getEntry().getMaster() + "\n";
        res += "Статус: " + complaint.getState().toString() + "\n";
        return res;
    }

    public String getClientDecText(Complaint complaint){
        if(complaint.getDecisionClient() != null)
            return complaint.getDecisionClient();
        return "";
    }

    public String getMasterDecText(Complaint complaint){
        if(complaint.getDecisionMaster() != null)
            return complaint.getDecisionMaster();
        return "";
    }

    public String getComplaintText(Complaint complaint){
        if(complaint.getText() != null)
            return complaint.getText();
        return "";
    }

    public String getComplaintExpl(Complaint complaint){
        if(complaint.getExplanatory() != null)
            return complaint.getExplanatory();
        return "";
    }

    public Boolean transferComplaint(Complaint complaint){
        Admin admin = new Admin();
        return admin.transferComplaint(complaint);
    }

    public Boolean saveExplanatory(Complaint complaint, String expl){
        Master master = new Master();
        return master.addExplanatory(complaint, expl);
    }

}
