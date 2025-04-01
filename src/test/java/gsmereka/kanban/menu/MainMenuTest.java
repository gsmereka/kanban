package gsmereka.kanban.menu;

import com.jayway.jsonpath.InvalidPathException;
import gsmereka.kanban.persistence.config.ConnectionConfig;
import gsmereka.kanban.persistence.entity.BoardEntity;
import gsmereka.kanban.persistence.migration.MigrationStrategy;
import gsmereka.kanban.service.BoardQueryService;
import gsmereka.kanban.ui.BoardMenu;
import gsmereka.kanban.ui.MainMenu;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.*;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static gsmereka.kanban.persistence.config.ConnectionConfig.getConnection;
import static org.junit.jupiter.api.Assertions.*;


public class MainMenuTest {

    @Test
    public void testJustExitMenu(){
        try {
            runTestWithInput("5\n");
        } catch (Exception e) {
            fail("Uma exceção inesperada foi lançada: " + e.getMessage());
        }
    }

    @Test
    public void testInfinityLoop(){
        Exception exception = assertThrows(InvalidPathException.class, () -> {
            runTestWithInput("1\n");
        });

        assertEquals("Uma exceção inesperada foi lançada: java.util.NoSuchElementException", exception.getMessage());
    }

    @Test
    public void testAddBoard(){
        try {
            runTestWithInput("1\nBoard\n0\nColuna Inicial\nColuna Final\nColuna Cancelamento\n5");
        } catch (Exception e) {
            fail("Uma exceção inesperada foi lançada: " + e.getMessage());
        }
        try (var connection = getConnection()) {
            var queryService = new BoardQueryService(connection);
            var optional = queryService.showBoardDetails(1L);

            assertTrue(optional.isPresent(), "O board não foi encontrado no banco de dados.");
            var board = optional.get();
            assertEquals("Board", board.name(), "O nome do board não corresponde ao esperado.");

            List<String> columnNames = new ArrayList<>();
            board.columns().forEach(c -> columnNames.add(c.name()));

            assertEquals(3, columnNames.size(), "A quantidade de colunas não corresponde ao esperado.");

            var boardsInserted = queryService.findAll().size();
            assertEquals(1, boardsInserted);
            assertEquals("Coluna Inicial", columnNames.get(0), "O nome da primeira coluna não corresponde ao esperado.");
            assertEquals("Coluna Final", columnNames.get(1), "O nome da segunda coluna não corresponde ao esperado.");
            assertEquals("Coluna Cancelamento", columnNames.get(2), "O nome da terceira coluna não corresponde ao esperado.");

        } catch (SQLException e) {
            fail("Erro ao acessar o banco de dados: " + e.getMessage());
        }
    }

    private void runTestWithInput(String simulatedInput) throws SQLException {
        ConnectionConfig.configConnection("jdbc:mysql://localhost:9898/KanbanTest", "KanbanTest", "KanbanTest");

        try (var connection = getConnection()) {
            new MigrationStrategy(connection).executeMigration("/db/changelog/db.changelog-for-tests.yml");
        } catch (SQLException e) {
            fail("SQLException foi lançada: " + e.getMessage());
            return;
        }

        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        boolean exceptionOccurred = false;
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            Future<Void> future = executor.submit(() -> {
                new MainMenu().execute();
                return null;
            });

            future.get(5, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                exceptionOccurred = true;
                fail("O teste falhou devido a um possível loop infinito ou operação demorada.");
            }
            catch (NoSuchElementException e) {
                exceptionOccurred = true;
                fail("Elemento não encontrado durante a execução do teste: " + e.getMessage());
            } catch (InvalidPathException e) {
                exceptionOccurred = true;
                fail("Path inválido durante a execução do teste: " + e.getMessage());
            } catch (Exception e) {
                exceptionOccurred = true;
                fail("Uma exceção inesperada foi lançada: " + e.getMessage());
            } finally {
                executor.shutdownNow();
            }

        assertFalse(exceptionOccurred, "O teste falhou devido a uma exceção.");
    }

}