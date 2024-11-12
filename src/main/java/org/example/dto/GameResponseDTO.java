package org.example.dto;

import org.example.components.Game;

import java.util.List;

public class GameResponseDTO {
    private String status; // Используем Enum для статуса ответа
    private String message;
    private Object data; // Можно заменить Object на конкретный тип данных, если необходимо
    private Game game;
    private List<String> errors; // Список ошибок, если они есть

    // Конструкторы
    public GameResponseDTO(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public GameResponseDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public GameResponseDTO(String status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public GameResponseDTO(String status, String message, Game game) {
        this.status = status;
        this.message = message;
        this.data = null;
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    // Геттеры и сеттеры
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
