package business_logic;

import database.Repository;
import database.RepositoryInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NewEntryTest {
    RepositoryInterface repo;
    Entry newEntry;
    Service service;
    Master master;
    Client client;

    @BeforeEach
    public void setUp() {
        repo = Repository.getInstance();

        master = new Master("testMaster","testMaster","testMaster", Service.ServiceType.HAIRDRESSING);
        master = repo.addMaster(master);

        service = new Service("testService", Service.ServiceType.HAIRDRESSING,45);
        service = repo.addService(service);
    }

    @AfterEach
    public void tearDown() {
        client = repo.getClient(client.getPhone());
        repo.removeClient(client);
        repo.removeMaster(master);
        repo.removeService(service);
        repo.removeEntry(newEntry);
    }

    @Test
    public void testNewEntry() throws Exception {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.YEAR,2020);
        date.set(Calendar.MONTH,Calendar.JUNE);
        date.set(Calendar.DAY_OF_MONTH,21);

        DateFormat format = new SimpleDateFormat("HH:mm");
        List<String> possibleTime = new ArrayList<>();
        for(int i = 10; i<= 18; i++){
            possibleTime.add(i + ":00");
            possibleTime.add(i + ":30");
        }
        possibleTime = master.getFreeTime(possibleTime,format,date);
        Calendar calendarTime = new GregorianCalendar();
        calendarTime.setTime(format.parse(possibleTime.get(0)));
        date.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));
        date.set(Calendar.MINUTE,calendarTime.get(Calendar.MINUTE));

        List<Service> possibleServices = master.getPossibleServices();
        assertTrue(possibleServices.get(0).getType().equals(master.getServices()));
        assertTrue(possibleServices.contains(service));

        client = new Client("testClient", "0(000)-000-00-00");

        newEntry = new Entry(date, master,possibleServices, client);;

        assertTrue(newEntry.addEntry());
    }
}