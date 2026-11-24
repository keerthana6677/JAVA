import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.Properties;

public final class DatabaseConnection {
    public static Connection getConnection() throws SQLException, IOException {
        Properties properties=new Properties();
        Path config=Paths.get("Task06","config","database.properties");
        try(InputStream input=Files.newInputStream(config)){
            properties.load(input);
        }
        String url=properties.getProperty("db.url");
        String user=properties.getProperty("db.user");
        String password=properties.getProperty("db.password");
        if(url==null || user==null || password==null) throw new IOException("Database configuration is incomplete.");
        return DriverManager.getConnection(url,user,password);
    }
}
