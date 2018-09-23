package usermanagement;


import java.sql.*;

public class UserAuthentificator {
        private final String url =  "jdbc:postgresql://localhost:5432/postgres";
        private final String user = "postgres";
        private final String password = "mysecretpassword";

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

        public int findUserId(String uName) {
            String SQL = "select * from _users WHERE username=" + uName;
           try(Connection conn = connect();
               Statement stmt = conn.createStatement();
               ResultSet rs = stmt.executeQuery(SQL)) {
               System.out.println(rs);

           } catch (SQLException ex) {
               System.out.println(ex.getMessage());
           }
            return 3;
        }

    public static void main(String[] args) {
        UserAuthentificator userAuthentificator = new UserAuthentificator();
        userAuthentificator.connect();
    }
}
