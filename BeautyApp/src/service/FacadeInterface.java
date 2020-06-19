package service;

import business_logic.*;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public interface FacadeInterface {
    ObservableList<Master> getMastersNames();
    ObservableList<Master> getMastersNames(Service service);
    ObservableList<Entry> getEntriesByDate(LocalDate localDate);
    ObservableList<Entry> getEntriesForMaster(LocalDate localDate, Integer master_id);
    ObservableList<Service> getServicesNames();
    ObservableList<Service> getServicesNames(Master master);
    ObservableList<Service> getServicesNames(Service service);
    ObservableList<String> getFreeTime(LocalDate localDate, Master master);
    ObservableList<String> getAllTime();
    ObservableList<Complaint> getOpenedComplaints();
    ObservableList<Complaint> getClosedComplaints();
    ObservableList<Complaint> getClosedComplaints(Integer masterId);
    ObservableList<Complaint> getWaitingDecComplaints();
    ObservableList<Complaint> getWaitingDecComplaints(Integer masterId);
    ObservableList<Complaint> getWaitingExplComplaints();
    ObservableList<Complaint> getWaitingExplComplaints(Integer masterId);

    String getComplaintInfo(Complaint complaint);
    String getClientDecText(Complaint complaint);
    String getMasterDecText(Complaint complaint);
    String getComplaintText(Complaint complaint);
    String getComplaintExpl(Complaint complaint);

    Boolean saveEntry(String clientName, String clientPhone, Master master, List<Service> services, Calendar date);
    Boolean transferEntry(Entry entry);
    Boolean transferComplaint(Complaint complaint);
    Employee authorize(String login, String password);
    Calendar combineEntryDateTime(String time, LocalDate localDate);
    String getStringTime(Calendar date);
}
