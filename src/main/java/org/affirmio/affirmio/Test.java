package org.affirmio.affirmio;

import java.sql.*;

public class Test {
    public static void main(String[] args)
    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loginschema",
                    "root",
                    "TestServer");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

            while (resultSet.next()) {
                System.out.println(resultSet.getString("username"));
                System.out.println(resultSet.getString("password"));

            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
