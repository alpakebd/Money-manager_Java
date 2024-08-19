package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {

    public static void main(String[] args) {
         String databasePath = "jdbc:sqlite:database/moneyAppFirst.db";
        try {
            Connection connection = DriverManager.getConnection(databasePath );
            String sql = "SELECT * FROM user";
            java.sql.Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("Id");
                String name = resultSet.getString("username");
                String password = resultSet.getString("password");
                System.out.println("Users: " + id + ", " + name + ", " + password);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}