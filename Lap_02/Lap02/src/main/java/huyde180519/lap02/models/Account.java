package huyde180519.lap02.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class Account {
    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String email;

    public  Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
