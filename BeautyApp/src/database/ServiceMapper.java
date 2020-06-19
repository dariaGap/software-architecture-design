package database;

import business_logic.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceMapper {
    private static Map<Integer, Service> loadedServices = new HashMap<>();
    private static Connection connection;
    private static ServiceMapper serviceMapper;

    private ServiceMapper(){
        connection = DatabaseConfig.getInstance().getConnection();
    }

    public static ServiceMapper getInstance(){
        if(serviceMapper == null)
            serviceMapper = new ServiceMapper();
        return serviceMapper;
    }

    public List<Service> getAll(){
        try {
            String query = "SELECT * FROM services;";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            return mapRes(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Service getById(Integer id){
        try {
            Service service = loadedServices.get(id);
            if(service != null){
                return service;
            }
            String query = "SELECT * FROM services WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                String name = rs.getString("name");
                String type = rs.getString("type");
                Integer length = rs.getInt("length");
                service = new Service(id, name, Service.ServiceType.stringToServiceType(type), length);
                loadedServices.put(id, service);
                return service;
            }
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Service> getByServiceType(Service.ServiceType service){
        try {
            String query = "SELECT * FROM services WHERE type = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, Service.ServiceType.serviceTypeToString(service));
            ResultSet rs = statement.executeQuery();
            return mapRes(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Service> mapRes(ResultSet rs) throws SQLException{
        ArrayList<Service> services = new ArrayList<>();
        while(rs.next()){
            Service service = loadedServices.get(rs.getInt("id"));
            if(service != null){
                services.add(service);
            }
            else {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                Integer length = rs.getInt("length");
                service = new Service(id, name, Service.ServiceType.stringToServiceType(type),length);
                loadedServices.put(id, service);
                services.add(service);
            }
        }
        return services;
    }

    public Service addService(Service service) {
        try {
            String query = "INSERT INTO services (name, type, length) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, service.getName());
            statement.setString(2, Service.ServiceType.serviceTypeToString(service.getType()));
            statement.setInt(3, service.getLength());
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                service.setId(id);
            } else {
                return null;
            }
            loadedServices.put(service.getId(), service);
            return service;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean remove(Integer id){
        try {
            Service service = loadedServices.get(id);
            if(service != null){
                loadedServices.remove(service);
            }
            String query = "DELETE FROM services WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
