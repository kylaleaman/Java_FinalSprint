import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HealthDataDao {
  private static final String INSERT_HEALTH_DATA = "INSERT INTO health_data (user_id, weight, height, steps, heart_rate, date) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String SELECT_DATA_BY_ID = "SELECT * FROM health_data WHERE id = ?";
  private static final String SELECT_DATA_BY_USERID = "SELECT * FROM health_data WHERE userId = ?";
  private static final String UPDATE_HEALTH_DATA = "UPDATE health_data SET user_id = ?, weight = ?, height = ?, steps = ?, hearth_rate = ?, date = ? WHERE id = ?";
  private static final String DELETE_HEALTH_DATA = "DELETE FROM health_data WHERE id = ?";

  // Create health data
  public boolean createHealthData(HealthData healthData) {
    boolean success = false;
    try (Connection con = DatabaseConnection.getCon();
        PreparedStatement statement = con.prepareStatement(INSERT_HEALTH_DATA)) {
          statement.setInt(1, healthData.getUserId());
          statement.setDouble(2, healthData.getWeight());
          statement.setDouble(3, healthData.getHeight());
          statement.setInt(4, healthData.getSteps());
          statement.setInt(5, healthData.getHeartRate());
          statement.setString(6, healthData.getDate());

          int rowsUpdated = statement.executeUpdate();
          if (rowsUpdated > 0) {
            success = true;
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return success;
  }

  // Get data by ID
  public HealthData getHealthDataById(int id) {
    HealthData healthData = null;
    try (Connection con = DatabaseConnection.getCon();
        PreparedStatement statement = con.prepareStatement(SELECT_DATA_BY_ID)) {
          statement.setInt(1, id);
          ResultSet rs = statement.executeQuery();

          if (rs.next()) {
            healthData = extractHealthDataFromResultSet(rs);
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return healthData;
  }

  //Get data by userID
  public List<HealthData> getHealthDataByUserId(int userId) {
    List<HealthData> healthDataList = new ArrayList<>();
    try (Connection con = DatabaseConnection.getCon();
        PreparedStatement statement = con.prepareStatement(SELECT_DATA_BY_USERID)) {
          statement.setInt(1, userId);
          ResultSet rs = statement.executeQuery();

          while (rs.next()) {
            HealthData healthData = extractHealthDataFromResultSet(rs);
            healthDataList.add(healthData);
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return healthDataList;
  }

  // Update health data 
  public boolean updateHealthData(HealthData healthData) {
    boolean success = false;
    try (Connection con = DatabaseConnection.getCon();
        PreparedStatement statement = con.prepareStatement(UPDATE_HEALTH_DATA)) {
          statement.setInt(1, healthData.getUserId());
          statement.setDouble(2, healthData.getWeight());
          statement.setDouble(3, healthData.getHeight());
          statement.setInt(4, healthData.getSteps());
          statement.setInt(5, healthData.getHeartRate());
          statement.setString(6, healthData.getDate());
          statement.setInt(7, healthData.getId());

          int rowsUpdated = statement.executeUpdate();
          if (rowsUpdated > 0) {
            success = true;
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return success;
  }

  //Delete health data
  public boolean deleteHealthData(int id) {
    boolean success = false;
    try (Connection con = DatabaseConnection.getCon();
        PreparedStatement statement = con.prepareStatement(DELETE_HEALTH_DATA)) {
          statement.setInt(1, id);

          int rowsUpdated = statement.executeUpdate();
          if (rowsUpdated > 0) {
            success = true;
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return success;
  }

  // Get health data from ResultSet(rs)
  private HealthData extractHealthDataFromResultSet(ResultSet rs) throws SQLException {
    int id = rs.getInt("id");
    int userId = rs.getInt("user_id");
    double weight = rs.getDouble("weight");
    double height = rs.getDouble("height");
    int steps = rs.getInt("steps");
    int heartRate = rs.getInt("heart_rate");
    String date = rs.getString("date");
    return new HealthData(id, userId, weight, height, steps, heartRate, date);
  }
}
