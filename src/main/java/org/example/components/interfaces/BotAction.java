package org.example.components.interfaces;

import org.example.dto.GameResponseDTO;
import org.springframework.http.ResponseEntity;

public interface BotAction {
     ResponseEntity<GameResponseDTO> move(int x, int y);
     ResponseEntity<GameResponseDTO> registration();
     ResponseEntity<GameResponseDTO> startGame();
     void eat();
}
