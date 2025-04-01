package gsmereka.kanban.persistence.config;

import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ConnectionConfig {

    private static String url;
    private static String user;
    private static String password;

    public static void configConnection(String url, String user, String password) {
        ConnectionConfig.url = url;
        ConnectionConfig.user = user;
        ConnectionConfig.password = password;
    }

    public static Connection getConnection() throws SQLException {
        if (url == null || user == null || password == null) {
            throw new IllegalStateException("A conexão não foi configurada corretamente.");
        }

        var connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);
        return connection;
    }
}