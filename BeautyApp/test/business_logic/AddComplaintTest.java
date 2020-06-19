package business_logic;

import database.Repository;
import database.RepositoryInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddComplaintTest {
    RepositoryInterface repo;
    Entry entry;
    Service service;
    Master master;
    Client client;
    Complaint complaint;

    @BeforeEach
    public void setUp() {
        repo = Repository.getInstance();

        master = new Master("testMaster","testMaster","testMaster", Service.ServiceType.HAIRDRESSING);
        master = repo.addMaster(master);

        service = new Service("testService", Service.ServiceType.HAIRDRESSING,45);
        service = repo.addService(service);
        List<Service> services = new ArrayList<>();
        services.add(service);

        client = new Client("testClient", "0(000)-000-00-00");
        client = repo.addClient(client);

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar date = new GregorianCalendar();
            date.setTime(format.parse("2020-06-22 15:30"));
            entry = new Entry(date,master,services,client);
            entry.addEntry();

            complaint = new Complaint(entry,"Test text");
            complaint.setState(Complaint.ComplaintState.WAIT_EXPLANATORY);
            complaint = repo.addComplaint(complaint);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        repo.removeClient(client);
        repo.removeMaster(master);
        repo.removeService(service);
        repo.removeEntry(entry);
        repo.removeComplaint(complaint);
    }

    @Test
    public void testAddComplaint() throws Exception {
        assertTrue(master.authorize("testMaster"));
        master.addExplanatory(complaint, "test explanatory");
        assertTrue(complaint.getState().equals(Complaint.ComplaintState.WAIT_DECISION));

        complaint = repo.getComplaint(complaint.getId());
        assertTrue(complaint.getState().equals(Complaint.ComplaintState.WAIT_DECISION));
    }
}
