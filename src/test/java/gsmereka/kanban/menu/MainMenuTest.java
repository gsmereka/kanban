package gsmereka.kanban.menu;

import gsmereka.kanban.persistence.config.ConnectionConfig;
import gsmereka.kanban.persistence.migration.MigrationStrategy;
import gsmereka.kanban.ui.MainMenu;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static gsmereka.kanban.persistence.config.ConnectionConfig.getConnection;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class MainMenuTest {

    @Test
    public void testCreateBoard() throws SQLException {
        runTestWithInput("5\n");
    }

    @Test
    public void testAddBoard() throws SQLException {
        runTestWithInput("1\nBoard\n0\nColuna Inicial\nColuna Final\nColuna Cancelamento\n5");  // Exemplo de outro input
    }

    private void runTestWithInput(String simulatedInput) throws SQLException {
        ConnectionConfig.configConnection("jdbc:mysql://localhost:9898/KanbanTest", "KanbanTest", "KanbanTest");
        try (var connection = getConnection()) {
            new MigrationStrategy(connection).executeMigration("/db/changelog/db.changelog-for-tests.yml");
        }
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        boolean exceptionOccurred = false;
        try {
            new MainMenu().execute();
        } catch (SQLException e) {
            exceptionOccurred = true;
            fail("SQLException foi lançada: " + e.getMessage());
        } catch (Exception e) {
            exceptionOccurred = true;
            fail("Uma exceção inesperada foi lançada: " + e.getMessage());
        }
        assertFalse(exceptionOccurred, "O teste falhou devido a uma exceção.");
    }
}