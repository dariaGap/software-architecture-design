package database;

import business_logic.Master;
import business_logic.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterMapper {
    private static Map<Integer, Master> loadedMasters = new HashMap<>();
    private static Connection connection;
    private static MasterMapper masterMapper;

    private MasterMapper(){
        connection = DatabaseConfig.getInstance().getConnection();
    }

    public static MasterMapper getInstance(){
        if(masterMapper == null)
            masterMapper = new MasterMapper();
        return masterMapper;
    }

    public Master addMaster(Master master) {
        try {
            String query = "INSERT INTO masters (name, login, password, services) "
                    + "VALUES (?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, master.getName());
            statement.setString(2, master.getLogin());
            statement.setString(3, master.getPassword());
            statement.setString(4, Service.ServiceType.serviceTypeToString(master.getServices()));
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                master.setId(id);
            } else {
                return null;
            }
            loadedMasters.put(master.getId(), master);
            return master;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Master> getAll(){
        try {
            String query = "SELECT * FROM masters;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            return mapRes(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Master getById(Integer id){
        try {
            Master master = loadedMasters.get(id);
            if(master != null){
                return master;
            }
            String query = "SELECT * FROM masters WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String login = rs.getString("login");
                String password = rs.getString("password");
                String services = rs.getString("services");
                master = new Master(id, name, login, password, Service.ServiceType.stringToServiceType(services));
                loadedMasters.put(id, master);
                return master;
            }
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Master getByLogin(String login){
        try {
            for(Master master: loadedMasters.values()){
                if(master.getLogin().equals(login))
                    return master;
            }
            String query = "SELECT * FROM masters WHERE login = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String services = rs.getString("services");
                Master master = new Master(id, name, login, password, Service.ServiceType.stringToServiceType(services));
                loadedMasters.put(id, master);
                return master;
            }
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Master> getByServiceType(Service.ServiceType service){
        try {
            String query = "SELECT * FROM masters WHERE services = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, Service.ServiceType.serviceTypeToString(service));
            ResultSet rs = statement.executeQuery();
            return mapRes(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Master> mapRes(ResultSet rs) throws SQLException{
        ArrayList<Master> masters = new ArrayList<>();
        while(rs.next()){
            Master master = loadedMasters.get(rs.getInt("id"));
            if(master != null){
                masters.add(master);
            }
            else {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                String login = rs.getString("login");
                String password = rs.getString("password");
                String services = rs.getString("services");
                master = new Master(id, name,login,password, Service.ServiceType.stringToServiceType(services));
                loadedMasters.put(id, master);
                masters.add(master);
            }
        }
        return masters;
    }

    public Boolean remove(Integer id){
        try {
            Master master = loadedMasters.get(id);
            if(master != null){
                loadedMasters.remove(master);
            }
            String query = "DELETE FROM masters WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
