package gsmereka.kanban;

import gsmereka.kanban.persistence.config.ConnectionConfig;
import gsmereka.kanban.persistence.migration.MigrationStrategy;
import gsmereka.kanban.ui.MainMenu;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

import static gsmereka.kanban.persistence.config.ConnectionConfig.getConnection;

@SpringBootApplication
public class Main {

	public static void main(String[] args) throws SQLException {

		ConnectionConfig.configConnection("jdbc:mysql://localhost:3306/board", "board", "board");
		try(var connection = getConnection()){
			new MigrationStrategy(connection).executeMigration();
		}
		new MainMenu().execute();
	}

}