package user_management;


import hello.Config;

import java.sql.*;

public class UserAuthentificator {

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(Config.DB_HOST, Config.DB_USER, Config.DB_PASSWORD);
            //System.out.println("Connected to PostgreSQL server");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private boolean checkPw(String pw, String passwordResult) {
        if (pw.equals(passwordResult)) {
            return true;
        } else {
            return false;
        }
    }

    public int checkUser(String uName, String uPw) {
        String SQL = "SELECT * FROM _users WHERE username='" + uName + "'";
        System.out.println(SQL);
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            rs.next();

            String idResult = rs.getString("uid");
            String nameResult = rs.getString("username");
            String passwordResult = rs.getString("password");

            // DebugLOG
            System.out.println(idResult);
            System.out.println(nameResult);
            System.out.println(passwordResult);
            //
            if (checkPw(uPw, passwordResult)) {
                return 0;
            }
            return 1;
        } catch (SQLException ex) {
            // System.out.println(ex.getMessage());
            System.out.println("No User found");
            return 2;
        }
    }

    public long addUser(String name, String password) {
        long id = 0;
        String SQL = "insert INTO _users (username, password) VALUES ('" + name + "','" + password + "')";
        System.out.println(SQL);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return -1;
        }
        return id;
    }


    public static void main(String[] args) {
        UserAuthentificator userAuthentificator = new UserAuthentificator();
        userAuthentificator.connect();
    }
}
