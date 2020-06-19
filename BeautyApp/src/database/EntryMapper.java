package database;

import business_logic.*;

import java.sql.*;
import java.util.*;

public class EntryMapper {
    private static Map<Integer, Entry> loadedEntries = new HashMap<>();
    private static Connection connection;
    private static EntryMapper entryMapper;

    private EntryMapper(){
        connection = DatabaseConfig.getInstance().getConnection();
    }

    public static EntryMapper getInstance(){
        if(entryMapper == null)
            entryMapper = new EntryMapper();
        return entryMapper;
    }

    public Entry addEntry(Entry entry) {
        Service[] sr = entry.getServices().toArray(Service[]::new);
        Integer[] ids = new Integer [sr.length];
        for (int i=0;i<sr.length;i++){
            ids[i] = sr[i].getId();
        }
        try {
            String query = "INSERT INTO entries (entry_date, master, state, services, client) "
                    + "VALUES (?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setTimestamp(1, new java.sql.Timestamp(entry.getDate().getTimeInMillis()));
            statement.setInt(2, entry.getMaster().getId());
            statement.setString(3, Entry.EntryState.entryStateToString(entry.getState()));
            statement.setArray(4, connection.createArrayOf("integer",ids));
            statement.setInt(5,entry.getClient().getId());
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt("id");
                entry.setId(id);
            } else {
                return null;
            }
            loadedEntries.put(entry.getId(), entry);
            return entry;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean updateEntry(Entry entry){
        Service[] sr = entry.getServices().toArray(Service[]::new);
        Integer[] ids = new Integer [sr.length];
        for (int i=0;i<sr.length;i++){
            ids[i] = sr[i].getId();
        }
        try {
            String query = "UPDATE entries SET entry_date = ?, master = ?, state = ?, services = ?, client = ?" +
                    " WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setTimestamp(1, new java.sql.Timestamp(entry.getDate().getTimeInMillis()));
            statement.setInt(2, entry.getMaster().getId());
            statement.setString(3, Entry.EntryState.entryStateToString(entry.getState()));
            statement.setArray(4, connection.createArrayOf("integer",ids));
            statement.setInt(5,entry.getClient().getId());
            statement.setInt(6,entry.getId());
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                loadedEntries.put(entry.getId(), entry);
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Entry> getAll(){
        try {
            String query = "SELECT * FROM entries;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            return mapRes(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Entry> getByMaster(Master master){
        try {
            String query = "SELECT * FROM entries WHERE master = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, master.getId());
            ResultSet rs = statement.executeQuery();
            return mapRes(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Entry getById(Integer id){
        Entry entry = loadedEntries.get(id);
        if(entry != null){
            return entry;
        }
        try {
            String query = "SELECT * FROM entries WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                Integer client_id = rs.getInt("client");
                Integer master_id = rs.getInt("master");
                Calendar date = new GregorianCalendar();
                date.setTime(rs.getTimestamp("entry_date"));
                Entry.EntryState state = Entry.EntryState.stringToEntryState(rs.getString("state"));
                Integer[] services_id = (Integer[]) rs.getArray("services").getArray();

                Master master = MasterMapper.getInstance().getById(master_id);
                List<Service> services = new ArrayList<>();
                for (int i = 0; i < services_id.length; i++){
                    services.add(ServiceMapper.getInstance().getById(services_id[i]));
                }
                Client client = ClientMapper.getInstance().getById(client_id);
                entry = new Entry(id, date, master, services, client, state);
                loadedEntries.put(id, entry);
                return entry;
            }
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Entry> getByDate(Calendar date){
        try {
            Calendar start = date;
            String query = "SELECT * FROM entries WHERE DATE_TRUNC('day', entry_date) = ?::timestamp;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTimestamp(1, new java.sql.Timestamp(start.getTimeInMillis()));
            ResultSet rs = statement.executeQuery();
            return mapRes(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Entry> getByMasterAndDate(Calendar date, Master master){
        try {
            Calendar start = date;
            String query = "SELECT * FROM entries WHERE (master = ?) AND DATE_TRUNC('day', entry_date) = ?::timestamp;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, master.getId());
            statement.setTimestamp(2, new java.sql.Timestamp(start.getTimeInMillis()));
            ResultSet rs = statement.executeQuery();
            return mapRes(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Entry> getByMasterStateAndDate(Calendar date, Entry.EntryState state, Master master){
        try {
            Calendar start = date;
            String query = "SELECT * FROM entries WHERE (master = ?) AND DATE_TRUNC('day', entry_date) = ?::timestamp " +
                    "AND state = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, master.getId());
            statement.setTimestamp(2, new java.sql.Timestamp(start.getTimeInMillis()));
            statement.setString(3, Entry.EntryState.entryStateToString(state));
            ResultSet rs = statement.executeQuery();
            return mapRes(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Entry> mapRes(ResultSet rs) throws SQLException{
        ArrayList<Entry> entries = new ArrayList<>();
        while(rs.next()){
            Entry entry = loadedEntries.get(rs.getInt("id"));
            if(entry != null){
                entries.add(entry);
            }
            else {
                Integer id = rs.getInt("id");
                Integer client_id = rs.getInt("client");
                Integer master_id = rs.getInt("master");
                Calendar date = new GregorianCalendar();
                date.setTime(rs.getTimestamp("entry_date"));
                Entry.EntryState state = Entry.EntryState.stringToEntryState(rs.getString("state"));
                Integer[] services_id = (Integer[]) rs.getArray("services").getArray();

                Master master = MasterMapper.getInstance().getById(master_id);
                List<Service> services = new ArrayList<>();
                for (int i = 0; i < services_id.length; i++){
                    services.add(ServiceMapper.getInstance().getById(services_id[i]));
                }
                Client client = ClientMapper.getInstance().getById(client_id);
                entry = new Entry(id, date, master, services, client, state);
                loadedEntries.put(id, entry);
                entries.add(entry);
            }
        }
        return entries;
    }

    public Boolean remove(Integer id){
        try {
            Entry entry = loadedEntries.get(id);
            if(entry != null){
                loadedEntries.remove(entry);
            }
            String query = "DELETE FROM entries WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
