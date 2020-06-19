package business_logic;

import database.Repository;
import database.RepositoryInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TransferEntryTest {
    RepositoryInterface repo;
    Entry entry;
    Service service;
    Master master;
    Client client;
    Admin admin;

    @BeforeEach
    public void setUp() {
        repo = Repository.getInstance();

        admin = new Admin("testAdmin", "testAdmin");
        admin = repo.addAdmin(admin);

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
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        repo.removeAdmin(admin);
        repo.removeClient(client);
        repo.removeMaster(master);
        repo.removeService(service);
        repo.removeEntry(entry);
    }

    @Test
    public void testTransferEntry() throws Exception {
        assertTrue(admin.authorize("testAdmin"));

        admin.transferEntry(entry);
        assertTrue(entry.getState().equals(Entry.EntryState.EXECUTING));

        entry = repo.getEntry(entry.getId());
        assertTrue(entry.getState().equals(Entry.EntryState.EXECUTING));
    }
}