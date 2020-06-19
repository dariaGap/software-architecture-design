package database;

import business_logic.*;

import java.sql.*;
import java.util.*;

public class ComplaintMapper {
    private static Map<Integer, Complaint> loadedComplaints = new HashMap<>();
    private static Connection connection;
    private static ComplaintMapper complaintMapper;

    private ComplaintMapper(){
        connection = DatabaseConfig.getInstance().getConnection();
    }

    public static ComplaintMapper getInstance(){
        if(complaintMapper == null)
            complaintMapper = new ComplaintMapper();
        return complaintMapper;
    }

    public Complaint addComplaint(Complaint complaint) {
        try {
            String query = "INSERT INTO complaints (entry, state, text) "
                    + "VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, complaint.getEntry().getId());
            statement.setString(2, Complaint.ComplaintState.complaintStateToString(complaint.getState()));
            statement.setString(3, complaint.getText());
            statement.execute();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt("id");
                complaint.setId(id);
            } else {
                return null;
            }
            loadedComplaints.put(complaint.getId(), complaint);
            return complaint;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Complaint> getByState(Complaint.ComplaintState state){
        try {
            String query = "SELECT * FROM complaints WHERE state = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, Complaint.ComplaintState.complaintStateToString(state));
            ResultSet rs = statement.executeQuery();
            return mapRes(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Complaint> mapRes(ResultSet rs) throws SQLException{
        ArrayList<Complaint> complaints = new ArrayList<>();
        while(rs.next()){
            Complaint complaint = loadedComplaints.get(rs.getInt("id"));
            if(complaint != null){
                complaints.add(complaint);
            }
            else {
                Integer id = rs.getInt("id");
                Integer entry_id = rs.getInt("entry");
                String text = rs.getString("text");
                String expl = rs.getString("explanatory");
                String dec_cl = rs.getString("decision_client");
                String dec_ms = rs.getString("decision_master");

                Complaint.ComplaintState state = Complaint.ComplaintState.stringToComplaintState(rs.getString("state"));
                Entry entry = EntryMapper.getInstance().getById(entry_id);
                complaint = new Complaint(id,entry, text,expl,dec_ms,dec_cl,state);
                loadedComplaints.put(id, complaint);
                complaints.add(complaint);
            }
        }
        return complaints;
    }

    public Boolean updateComplaint(Complaint complaint){
        try {
            String query = "UPDATE complaints SET entry = ?, state = ?, text = ?, explanatory = ?, " +
                    "decision_master = ?, decision_client = ? WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, complaint.getEntry().getId());
            statement.setString(2, Complaint.ComplaintState.complaintStateToString(complaint.getState()));
            statement.setString(3, complaint.getText());
            statement.setString(4, complaint.getExplanatory());
            statement.setString(5, complaint.getDecisionMaster());
            statement.setString(6, complaint.getDecisionClient());
            statement.setInt(7, complaint.getId());
            statement.execute();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                loadedComplaints.put(complaint.getId(), complaint);
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean remove(Integer id){
        try {
            Complaint complaint = loadedComplaints.get(id);
            if(complaint != null){
                loadedComplaints.remove(complaint);
            }
            String query = "DELETE FROM complaints WHERE id = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Complaint getById(Integer id){
        Complaint complaint = loadedComplaints.get(id);
        if(complaint != null){
            return complaint;
        }
        try {
            String query = "SELECT * FROM complaints WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                Integer entry_id = rs.getInt("entry");
                String text = rs.getString("text");
                String expl = rs.getString("explanatory");
                String dec_cl = rs.getString("decision_client");
                String dec_ms = rs.getString("decision_master");

                Complaint.ComplaintState state = Complaint.ComplaintState.stringToComplaintState(rs.getString("state"));
                Entry entry = EntryMapper.getInstance().getById(entry_id);
                complaint = new Complaint(id,entry, text,expl,dec_ms,dec_cl,state);
                loadedComplaints.put(id, complaint);
                return complaint;
            }
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
