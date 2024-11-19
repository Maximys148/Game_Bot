package org.example.dto;

public class RegistrationDTO {
    private String nickName;

    public RegistrationDTO(String nickName) {
        this.nickName = nickName;
    }

    public RegistrationDTO() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
