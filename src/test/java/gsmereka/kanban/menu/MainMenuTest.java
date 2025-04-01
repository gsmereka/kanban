package gsmereka.kanban.menu;

import gsmereka.kanban.persistence.config.ConnectionConfig;
import gsmereka.kanban.persistence.migration.MigrationStrategy;
import gsmereka.kanban.ui.MainMenu;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Scanner;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static gsmereka.kanban.persistence.config.ConnectionConfig.getConnection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainMenuTest {

    @Test
    public void testCreateBoard() throws SQLException {
        ConnectionConfig.configConnection("jdbc:mysql://localhost:9898/KanbanTest", "KanbanTest", "KanbanTest");
        try(var connection = getConnection()){
            new MigrationStrategy(connection).executeMigration();
        }
        String simulatedInput = "1\nBoard\n0\nColuna Inicial\nColuna Final\nColuna Cancelamento\n5";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        try {
            new MainMenu().execute();
        } catch (SQLException e) {
            fail("SQLException foi lançada: " + e.getMessage());
        }
    }

//    @Test
//    public void testCreateBoard() throws SQLException {
//        Scanner mockScanner = mock(Scanner.class);
//
//        when(mockScanner.nextInt())
//                .thenReturn(1)  // Opção 1 - Criar board
//                .thenReturn(1);  // 1 coluna adicional
//        when(mockScanner.next())
//                .thenReturn("My Board")  // Nome do board
//                .thenReturn("To Do")  // Nome da coluna inicial
//                .thenReturn("Pending")  // Nome da coluna pendente
//                .thenReturn("Done")  // Nome da coluna final
//                .thenReturn("Cancelled");  // Nome da coluna de cancelamento
//
//        MainMenu menu = new MainMenu() {
//            protected Scanner getScanner() {
//                return mockScanner;
//            }
//        };
//        menu.execute();
//
//        // Verificar se o método de inserção foi chamado
//
//        // Verificar se o Board Foi Criado
//    }
}