package gsmereka.kanban.ui;

import gsmereka.kanban.persistence.entity.BoardColumnEntity;
import gsmereka.kanban.persistence.entity.BoardColumnKindEnum;
import gsmereka.kanban.persistence.entity.BoardEntity;
import gsmereka.kanban.service.BoardQueryService;
import gsmereka.kanban.service.BoardService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static gsmereka.kanban.persistence.config.ConnectionConfig.getConnection;
import static gsmereka.kanban.persistence.entity.BoardColumnKindEnum.*;

public class MainMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public void execute() throws SQLException {
        System.out.println("Bem-vindo ao gerenciador de boards, escolha a opção desejada");
        int option = -1;
        while (true) {
            System.out.println("1 - Criar um novo board");
            System.out.println("2 - Ver todos os boards existentes");
            System.out.println("3 - Selecionar um board existente");
            System.out.println("4 - Excluir um board");
            System.out.println("5 - Sair");
            option = readInt("Escolha uma opção: ");
            switch (option) {
                case 1 -> createBoard();
                case 2 -> showAllBoards();
                case 3 -> selectBoard();
                case 4 -> deleteBoard();
                case 5 -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida, informe uma opção do menu");
            }
        }
    }

    private void createBoard() throws SQLException {
        var entity = new BoardEntity();
        entity.setName(readString("Informe o nome do seu board: "));

        int additionalColumns = readInt("Seu board terá colunas além das 3 padrões? Se sim informe quantas, senão digite '0': ");
        List<BoardColumnEntity> columns = new ArrayList<>();

        var initialColumn = createColumn(readString("Informe o nome da coluna inicial do board: "), INITIAL, 0);
        columns.add(initialColumn);

        for (int i = 0; i < additionalColumns; i++) {
            var pendingColumn = createColumn(readString("Informe o nome da coluna de tarefa pendente do board: "), PENDING, i + 1);
            columns.add(pendingColumn);
        }

        var finalColumn = createColumn(readString("Informe o nome da coluna final: "), FINAL, additionalColumns + 1);
        columns.add(finalColumn);

        var cancelColumn = createColumn(readString("Informe o nome da coluna de cancelamento do board: "), CANCEL, additionalColumns + 2);
        columns.add(cancelColumn);

        entity.setBoardColumns(columns);
        try (var connection = getConnection()) {
            new BoardService(connection).insert(entity);
            System.out.println("Board criado com sucesso!");
        }
    }

    private void selectBoard() throws SQLException {
        long id = readLong("Informe o ID do board que deseja selecionar: ");
        try (var connection = getConnection()) {
            var queryService = new BoardQueryService(connection);
            var optional = queryService.findById(id);
            optional.ifPresentOrElse(
                    b -> new BoardMenu(b).execute(),
                    () -> System.out.printf("Não foi encontrado um board com ID %s\n", id)
            );
        }
    }

    private void showAllBoards() throws SQLException {
        System.out.println("\n---\nAqui estão todos os Boards registrados:\n");
        try (var connection = getConnection()) {
            var queryService = new BoardQueryService(connection);
            List<BoardEntity> boards = queryService.findAll();
            if (boards.isEmpty()) {
                System.out.println("Nenhum board encontrado.");
            } else {
                boards.forEach(board ->
                        System.out.println("ID: " + board.getId() + ", Nome: " + board.getName()));
            }
            System.out.println("\n---");
        }
    }

    private void deleteBoard() throws SQLException {
        long id = readLong("Informe o ID do board que será excluído: ");
        try (var connection = getConnection()) {
            var service = new BoardService(connection);
            if (service.delete(id)) {
                System.out.printf("O board %s foi excluído com sucesso!\n", id);
            } else {
                System.out.printf("Não foi encontrado um board com ID %s\n", id);
            }
        }
    }

    private BoardColumnEntity createColumn(final String name, final BoardColumnKindEnum kind, final int order) {
        var boardColumn = new BoardColumnEntity();
        boardColumn.setName(name);
        boardColumn.setKind(kind);
        boardColumn.setOrder(order);
        return boardColumn;
    }

    // Métodos auxiliares para leitura segura de entradas
    private int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Entrada inválida! Digite um número inteiro.");
            }
        }
    }

    private long readLong(String message) {
        while (true) {
            try {
                System.out.print(message);
                return scanner.nextLong();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Entrada inválida! Digite um número válido.");
            }
        }
    }

    private String readString(String message) {
        System.out.print(message);
        return scanner.next();
    }
}
