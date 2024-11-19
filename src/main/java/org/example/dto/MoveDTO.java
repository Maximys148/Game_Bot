package org.example.dto;

public class MoveDTO {
    private String nickname;
    private int x;
    private int y;

    public MoveDTO(String nickname, int x, int y) {
        this.nickname = nickname;
        this.x = x;
        this.y = y;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
