import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;


public class UserDao {
    private static final String INSERT_USER = "INSERT INTO users (first_name, last_name, email, password, is_doctor) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String UPDATE_USER = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ?, is_doctor = ? WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";
    private static final String SELECT_PASSWORD_BY_EMAIL = "SELECT password FROM users WHERE email = ?";
 
    
    // Create User
    public boolean createUser(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        boolean success = false;
        try (Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(INSERT_USER)) {
                statement.setString(1, user.getFirstName());
                statement.setString(2, user.getLastName());
                statement.setString(3, user.getEmail());
                statement.setString(4, hashedPassword);
                statement.setBoolean(5, user.isDoctor());

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    success = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return success;
    };
       

    // Get user by ID
    public User getUserById(int id) { 
        User user = null;
        try (Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(SELECT_USER_BY_ID)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    user = extractUserFromResultSet(rs);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return user;
    };


    // Get user by email
    public User getUserByEmail(String email) { 
        User user = null;
        try (Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(SELECT_USER_BY_EMAIL)) {
                statement.setString(1, email);
                ResultSet rs = statement.executeQuery();

                if(rs.next()) {
                    user = extractUserFromResultSet(rs);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return user;
    };


    // Update user
    public boolean updateUser(User user) {
        boolean success = false;
        try (Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(UPDATE_USER)) {
                statement.setString(1, user.getFirstName());
                statement.setString(2, user.getLastName());
                statement.setString(3, user.getEmail());
                statement.setString(4, user.getPassword());
                statement.setBoolean(5, user.isDoctor());
                statement.setInt(6, user.getId());

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    success = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return success;
    };
    

    // Delete user
    public boolean deleteUser(int id) { 
        boolean success = false;
        try (Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(DELETE_USER)) {
                statement.setInt(1, id);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    success = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return success;
    };


    // Verify password
    public boolean verifyPassword (String email, String password) {
        boolean verified = false;
        try (Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(SELECT_PASSWORD_BY_EMAIL)) {
                statement.setString(1, email);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    verified = BCrypt.checkpw(password, hashedPassword);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return verified;
    }

    // Extract user information from the ResultSet
    private User extractUserFromResultSet (ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        Boolean isDoctor = rs.getBoolean("isDoctor");

        return new User(id, firstName, lastName, email, password, isDoctor);
    }
};
