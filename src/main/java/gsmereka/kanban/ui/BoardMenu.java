package gsmereka.kanban.ui;

import gsmereka.kanban.dto.BoardColumnInfoDTO;
import gsmereka.kanban.persistence.entity.BoardColumnEntity;
import gsmereka.kanban.persistence.entity.BoardEntity;
import gsmereka.kanban.persistence.entity.CardEntity;
import gsmereka.kanban.service.BoardColumnQueryService;
import gsmereka.kanban.service.BoardQueryService;
import gsmereka.kanban.service.CardQueryService;
import gsmereka.kanban.service.CardService;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static gsmereka.kanban.persistence.config.ConnectionConfig.getConnection;

@AllArgsConstructor
public class BoardMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");
    private final BoardEntity entity;

    public void execute() {
        try {
            System.out.printf("Bem-vindo ao board %s, selecione a operação desejada\n", entity.getId());
            int option = -1;
            while (option != 9) {
                System.out.println("1 - Criar um card");
                System.out.println("2 - Mover um card");
                System.out.println("3 - Bloquear um card");
                System.out.println("4 - Desbloquear um card");
                System.out.println("5 - Cancelar um card");
                System.out.println("6 - Ver board");
                System.out.println("7 - Ver coluna com cards");
                System.out.println("8 - Ver card");
                System.out.println("9 - Voltar para o menu anterior");
                System.out.println("10 - Sair");

                option = readInt("Escolha uma opção: ");

                switch (option) {
                    case 1 -> createCard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando para o menu anterior...");
                    case 10 -> {
                        System.out.println("Saindo do sistema...");
                        System.exit(0);
                    }
                    default -> System.out.println("Opção inválida! Informe um número entre 1 e 10.");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erro no sistema: " + ex.getMessage());
            System.exit(0);
        }
    }

    private void createCard() throws SQLException {
        var card = new CardEntity();
        card.setTitle(readString("Informe o título do card: "));
        card.setDescription(readString("Informe a descrição do card: "));
        card.setBoardColumn(entity.getInitialColumn());

        try (var connection = getConnection()) {
            new CardService(connection).create(card);
        }
    }

    private void moveCardToNextColumn() throws SQLException {
        long cardId = readLong("Informe o ID do card que deseja mover para a próxima coluna: ");
        List<BoardColumnInfoDTO> boardColumnsInfo = getBoardColumnsInfo();

        try (var connection = getConnection()) {
            new CardService(connection).moveToNextColumn(cardId, boardColumnsInfo);
        } catch (RuntimeException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    private void blockCard() throws SQLException {
        long cardId = readLong("Informe o ID do card que será bloqueado: ");
        String reason = readString("Informe o motivo do bloqueio: ");
        List<BoardColumnInfoDTO> boardColumnsInfo = getBoardColumnsInfo();

        try (var connection = getConnection()) {
            new CardService(connection).block(cardId, reason, boardColumnsInfo);
        } catch (RuntimeException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    private void unblockCard() throws SQLException {
        long cardId = readLong("Informe o ID do card que será desbloqueado: ");
        String reason = readString("Informe o motivo do desbloqueio: ");

        try (var connection = getConnection()) {
            new CardService(connection).unblock(cardId, reason);
        } catch (RuntimeException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    private void cancelCard() throws SQLException {
        long cardId = readLong("Informe o ID do card que deseja cancelar: ");
        var cancelColumn = entity.getCancelColumn();
        List<BoardColumnInfoDTO> boardColumnsInfo = getBoardColumnsInfo();

        try (var connection = getConnection()) {
            new CardService(connection).cancel(cardId, cancelColumn.getId(), boardColumnsInfo);
        } catch (RuntimeException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    private void showBoard() throws SQLException {
        try (var connection = getConnection()) {
            var optional = new BoardQueryService(connection).showBoardDetails(entity.getId());
            optional.ifPresent(b -> {
                System.out.printf("Board [%s,%s]\n", b.id(), b.name());
                b.columns().forEach(c ->
                        System.out.printf("Coluna [%s] tipo: [%s] tem %s cards\n", c.name(), c.kind(), c.cardsAmount())
                );
            });
        }
    }

    private void showColumn() throws SQLException {
        var columnsIds = entity.getBoardColumns().stream().map(BoardColumnEntity::getId).toList();
        long selectedColumnId = -1;
        while (!columnsIds.contains(selectedColumnId)) {
            System.out.printf("Escolha uma coluna do board %s pelo ID:\n", entity.getName());
            entity.getBoardColumns().forEach(c -> System.out.printf("%s - %s [%s]\n", c.getId(), c.getName(), c.getKind()));
            selectedColumnId = readLong("Informe o ID da coluna: ");
        }

        try (var connection = getConnection()) {
            var column = new BoardColumnQueryService(connection).findById(selectedColumnId);
            column.ifPresent(co -> {
                System.out.printf("Coluna %s tipo %s\n", co.getName(), co.getKind());
                co.getCards().forEach(ca -> System.out.printf("Card %s - %s\nDescrição: %s",
                        ca.getId(), ca.getTitle(), ca.getDescription()));
            });
        }
    }

    private void showCard() throws SQLException {
        long selectedCardId = readLong("Informe o ID do card que deseja visualizar: ");

        try (var connection = getConnection()) {
            new CardQueryService(connection).findById(selectedCardId)
                    .ifPresentOrElse(
                            c -> {
                                System.out.printf("Card %s - %s.\n", c.id(), c.title());
                                System.out.printf("Descrição: %s\n", c.description());
                                System.out.println(c.blocked() ?
                                        "Está bloqueado. Motivo: " + c.blockReason() :
                                        "Não está bloqueado");
                                System.out.printf("Já foi bloqueado %s vezes\n", c.blocksAmount());
                                System.out.printf("Está no momento na coluna %s - %s\n", c.columnId(), c.columnName());
                            },
                            () -> System.out.printf("Não existe um card com o ID %s\n", selectedCardId));
        }
    }

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
        return readInt(message);
    }

    private String readString(String message) {
        System.out.print(message);
        return scanner.next();
    }

    private List<BoardColumnInfoDTO> getBoardColumnsInfo() {
        return entity.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();
    }
}
