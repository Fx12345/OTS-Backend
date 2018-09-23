package usermanagement;


import java.sql.*;

public class UserAuthentificator {
        private final String url =  "jdbc:postgresql://localhost:5432/postgres";
        private final String user = "postgres";
        private final String password = "mysecretpassword";

        private String idResult;
        private String nameResult;
        private String passwordResult;

        private Connection connect() {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url,user,password);
                System.out.println("Connected to PostgreSQL server");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return conn;
        }

        public boolean checkUser(String uName) {
            String SQL = "select * from _users WHERE username=" + "'" + uName + "'";
            System.out.println(SQL);
           try(Connection conn = connect();
               Statement stmt = conn.createStatement();
               ResultSet rs = stmt.executeQuery(SQL)) {
               rs.next();
               idResult = rs.getString("uid");
               nameResult = rs.getString("username");
               passwordResult = rs.getString("password");

                // DebugLOG
               System.out.println(idResult);
               System.out.println(nameResult);
               System.out.println(passwordResult);
               //


               return true;
           } catch (SQLException ex) {
               System.out.println(ex.getMessage());
           }
            return false;
        }

    public static void main(String[] args) {
        UserAuthentificator userAuthentificator = new UserAuthentificator();
        userAuthentificator.connect();
    }
}
