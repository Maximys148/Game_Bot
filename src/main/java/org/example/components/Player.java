package org.example.components;

import lombok.Data;

@Data
public class Player {
    private String id;
    private String nickName;
    private Integer indexMove;
    private int positionX;
    private int positionY;
    private Integer countFood = 0;

    public Player(String nickName) {
        this.nickName = nickName;
    }

    public Integer getCountFood() {
        return countFood;
    }

    public void setCountFood(Integer countFood) {
        this.countFood = countFood;
    }

    public Integer getIndexMove() {
        return indexMove;
    }

    public void setIndexMove(Integer indexMove) {
        this.indexMove = indexMove;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}