package database;

import business_logic.Admin;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AdminMapper {
    private static Map<Integer, Admin> loadedAdmins = new HashMap<>();
    private static Connection connection;
    private static AdminMapper adminMapper;

    private AdminMapper(){
        connection = DatabaseConfig.getInstance().getConnection();
    }

    public static AdminMapper getInstance(){
        if(adminMapper == null)
            adminMapper = new AdminMapper();
        return adminMapper;
    }

    public Admin addAdmin(Admin admin) {
        try {
            String query = "INSERT INTO admins (login, password) "
                    + "VALUES (?, ?);";
            PreparedStatement statement = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, admin.getLogin());
            statement.setString(2, admin.getPassword());
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                admin.setId(id);
            } else {
                return null;
            }
            loadedAdmins.put(admin.getId(), admin);
            return admin;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Admin getByLogin(String login){
        try {
            for(Admin admin: loadedAdmins.values()){
                if(admin.getLogin().equals(login))
                    return admin;
            }
            String query = "SELECT * FROM admins WHERE login = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Integer id = rs.getInt("id");
                String password = rs.getString("password");
                Admin admin = new Admin(id, login, password);
                loadedAdmins.put(id, admin);
                return admin;
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
            Admin admin = loadedAdmins.get(id);
            if(admin != null){
                loadedAdmins.remove(admin);
            }
            String query = "DELETE FROM admins WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
