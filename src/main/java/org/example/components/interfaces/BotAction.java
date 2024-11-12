package org.example.components.interfaces;

import org.example.dto.GameResponseDTO;
import org.springframework.http.ResponseEntity;

public interface BotAction {
     boolean move(int x, int y);
     ResponseEntity<GameResponseDTO> registration();
     void startGame();
     void eat();
}
