package gsmereka.kanban.dto;


import gsmereka.kanban.persistence.entity.BoardColumnKindEnum;

public record BoardColumnInfoDTO(Long id, int order, BoardColumnKindEnum kind) {
}