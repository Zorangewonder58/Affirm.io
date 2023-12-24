package org.affirmio.affirmio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class HelloApplication extends Application {

    public static Stage window;
    static String AuthenticatedUser;
    static int UserID;
    static Scene scene1, scene2;


    public static void main(String[] args) {
        //Method in Application class-Start main java program as javafx application
        launch();

        //Application calls method called start
    }



    @Override
    public void start(Stage stage) throws Exception {

        window = stage;

        Parent test = FXMLLoader.load(getClass().getResource("hello-view.fxml"));


        scene1 = new Scene(test, 400, 400);
        //scene2 = new Scene(layout2, 500, 600);


        window.setScene(scene1);
        window.show();
    }


    public static boolean isValidPassword(String password)
    {
        //Regex to check valid password
        //Password must contain a digit, lowercase letter, uppercaseletter, nospaces, and at least 10 characters
        String regex = "(?=.*[0-9])"
                       + "(?=.*[a-z])"
                       + "(?=.*[A-Z])"
                       + "(?=\\S+$).{10,20}$";

        Pattern pattern = Pattern.compile(regex);

        if(password == null)
            return false;

        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public static String AuthenticatedUser(String Username, String Password)
    {
        boolean GoodUser = SQLUsername(Username);
        boolean GoodPass = SQLPassword(Password, Username);

        if(GoodUser && GoodPass) {
            UserID = GetUserID(Username, Password);
            System.out.println("User ID is " + UserID);

            return GetName(UserID);
        }else {
            System.out.println("Cant find user");
            return null;
        }
    }
    public static void Authentication(String Username, String Password, Stage window, Scene NextScene, StackPane layout2)
    {
        boolean GoodUser = SQLUsername(Username);
        boolean GoodPass = SQLPassword(Password, Username);

        if(GoodUser && GoodPass)
        {
            UserID = GetUserID(Username, Password);
            System.out.println("User ID is " + UserID);

            AuthenticatedUser = GetName(UserID);

            //Greet the User
            //Label Greeting = new Label("Hello " + AuthenticatedUser);
            //layout2.getChildren().addAll(Greeting);

            //Display Motivation
            Label Motivation = new Label("Hello " + AuthenticatedUser + "\n Here is your Motivation :" + Motivation());
            layout2.getChildren().add(Motivation);


            NextScene = new Scene(layout2, 1000, 1000);

            window.setScene(NextScene);

        }else
            System.out.println("Invalid Username");

    }
    public static String Motivation()
    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loginschema",
                    "root",
                    "TestServer");

            String sql = "SELECT Motivations FROM affirmations ORDER BY RAND()";

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            if(result.next())
                return result.getString("Motivations");


        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return "No Motivation found";
    }

    public static void AddtoFile(String filePath, String Motivation) throws IOException {


        String insert = "\n" + Motivation;

        Files.write(Paths.get(filePath), insert.getBytes(), StandardOpenOption.APPEND);

        System.out.println("Motivation added: " + Motivation);

    }

    public static void AddtoDatabase(String insert, String tablename, String columnname) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loginschema",
                    "root",
                    "TestServer");

            String sql = "INSERT INTO " + tablename + " (" + columnname + ") values (?)";

            //Reading data for motivations
            //String filePath = "C:\\Users\\payde\\Documents\\Programming\\Affirm.io\\Affirm.io\\src\\main\\Gems.txt";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, insert);
            statement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


public void FileInsert(String filePath, String tableName, String columnName, String IDcolumn)
{

    try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loginschema",
                "root",
                "TestServer");

        //String absolutePath = Paths.get("").toAbsolutePath().toString();

        //String file = absolutePath + File.separator + filePath;

        String file = filePath;


        //Reading data for motivations
        //String filePath = "C:\\Users\\payde\\Documents\\Programming\\Affirm.io\\Affirm.io\\src\\main\\Gems.txt";
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int n = 1;

            DeleteAllRecords(tableName);


            while ((line = reader.readLine()) != null) {
                String sql = "INSERT INTO " + tableName + " (" + IDcolumn + "," + columnName + ") values (" + n +",?)";
                PreparedStatement Fileinsert = connection.prepareStatement(sql);
                Fileinsert.setString(1, line);
                System.out.println(line);

                Fileinsert.executeUpdate();
                n++;
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }


    }catch(SQLException e)
    {
        e.printStackTrace();
    }

}

private static void DeleteAllRecords(String tableName)
{
    try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loginschema",
                "root",
                "TestServer");

        Statement statement = connection.createStatement();

        String deleteAllRecords = "DELETE FROM " + tableName;

        int rowsAffected = statement.executeUpdate(deleteAllRecords);

        System.out.println(rowsAffected + "record(s) deleted from table");

    }catch(SQLException e)
    {
        e.printStackTrace();
    }

}

    protected static boolean SQLUsername(String Username)
    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loginschema",
                    "root",
                    "TestServer");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT username FROM users");

            while (resultSet.next()) {
                if(Username.equals(resultSet.getString("username")))
                    return true;

            }

            return false;

        }catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    protected static boolean SQLPassword(String Pass, String Username)
    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loginschema",
                    "root",
                    "TestServer");

            Statement statement = connection.createStatement();

            String sql = "SELECT password FROM users WHERE username = ?";
            PreparedStatement prep = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //Set the strings for Insert Statement to add username and password
            prep.setString(1, Username);

            ResultSet resultSet = prep.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString("password"));
                if(Pass.equals(resultSet.getString("password")))
                    return true;

            }

            return false;

        }catch(SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private static int GetUserID(String Username, String Password)
    {
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loginschema",
                    "root",
                    "TestServer");

            String sql = "SELECT UserID FROM users WHERE Username = ? AND Password = ?";

            PreparedStatement FindID = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //Set the strings for Insert Statement to add username and password
            FindID.setString(1, Username);
            FindID.setString(2, Password);

            ResultSet result = FindID.executeQuery();

            while(result.next())
            {
                System.out.println(result.getInt("UserID"));
                return result.getInt("UserID");
            }

        }catch(SQLException e)
        {
            e.printStackTrace();
            return -1;
        }

        return -1;
    }

    private static String GetName(int ID)
    {
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loginschema",
                    "root",
                    "TestServer");

            String sql = "SELECT Username FROM users WHERE UserID = ?";

            PreparedStatement FindName = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //Set the strings for Insert Statement to add username and password
            FindName.setInt(1, ID);

            ResultSet result = FindName.executeQuery();

            while(result.next())
            {
                System.out.println(result.getString("Username"));
                return result.getString("Username");
            }

        }catch(SQLException e)
        {
            e.printStackTrace();
            return "No Name";
        }

        return "No Name";
    }
    public static boolean CreateUser(String NewUsername, String NewPassword)
    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loginschema",
                    "root",
                    "TestServer");

            Statement view = connection.createStatement();
            ResultSet resultSet = view.executeQuery("SELECT * FROM users");

            while (resultSet.next()) {
                String existingUsername = resultSet.getString("username");
                String existingPassword = resultSet.getString("password");

                if(NewUsername.equals(existingUsername))
                {
                    System.out.println("Username already taken..User cannot be created");
                    return false;
                }else if(NewPassword.equals(existingPassword))
                {
                    System.out.println("Password already taken..User cannot be created");
                    return false;
                }


            }

            String prepSQL = "INSERT INTO users(username, password)" + "VALUES(?,?)";
            PreparedStatement statement = connection.prepareStatement(prepSQL, Statement.RETURN_GENERATED_KEYS);

            //Set the strings for Insert Statement to add username and password
            statement.setString(1, NewUsername);

            if(isValidPassword(NewPassword))
                statement.setString(2, NewPassword);
            else
            {
                System.out.println("Invalid Password");
                return false;
            }
            //statement.executeUpdate("INSERT INTO users" + "VALUES('John Doe','Example'");

            // Execute the statement
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User created successfully!");

                /*try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }*/
                // Close resources
                statement.close();
                connection.close();
                return true;
            } else {
                System.out.println("Failed to create user.");
                return  false;
            }


        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }


}