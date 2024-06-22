package study.data_jpa.repository;

import lombok.NoArgsConstructor;

public class UsernameOnlyDto {


    private final String username;


    public UsernameOnlyDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
