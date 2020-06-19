package database;

import business_logic.Client;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ClientMapper {
    private static Map<Integer, Client> loadedClients = new HashMap<>();
    private static Connection connection;
    private static ClientMapper clientMapper;

    private ClientMapper(){
        connection = DatabaseConfig.getInstance().getConnection();
    }

    public static ClientMapper getInstance(){
        if(clientMapper == null)
            clientMapper = new ClientMapper();
        return clientMapper;
    }

    public Client addClient(Client client) {
        try {
            String query = "INSERT INTO clients (name, phone)"
                    + "VALUES (?, ?);";
            PreparedStatement statement = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, client.getName());
            statement.setString(2, client.getPhone());
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                client.setId(id);
            } else {
                return null;
            }
            loadedClients.put(client.getId(), client);
            return client;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Client getById(Integer id){
        try {
            Client client = loadedClients.get(id);
            if(client != null){
                return client;
            }
            String query = "SELECT * FROM clients WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                client = new Client(id, name, phone);
                loadedClients.put(id, client);
                return client;
            }
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Client getByPhone(String phone){
        try {
            Client client;
            for(Client cl: loadedClients.values()){
                if(cl.getPhone().equals(phone)) {
                    return cl;
                }
            }
            String query = "SELECT * FROM clients WHERE phone = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, phone);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                client = new Client(id, name,phone);
                loadedClients.put(id, client);
                return client;
            }
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean remove(Integer id){
        try {
            Client client = loadedClients.get(id);
            if(client != null){
                loadedClients.remove(client);
            }
            String query = "DELETE FROM clients WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
