package huyde180519.lap02.service;

import huyde180519.lap02.models.Account;
import lombok.Getter;
import lombok.Setter;

public class AccountService {

    public AccountService() {

    }
    public boolean registerAccount(Account account) {
        if (account == null) return false;
        if (!isValidUsername(account.getUsername())) return false;
        if (!isValidPassword(account.getPassword())) return false;
        if (!isValidEmail(account.getEmail())) return false;
        return true;
    }

    public boolean isValidEmail(String email) {
        if (email == null) return false;
        email = email.trim();
        if (email.isEmpty()) return false;

        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }



    public boolean isValidUsername(String username) {
        return username != null && username.trim().length() >= 3;
    }

    public boolean isValidPassword(String password) {
        if (password == null) return false;
        password = password.trim();  // ⭐ BẮT BUỘC
        if (password.length() <= 6) return false;

        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasSpecial = password.matches(".*[^A-Za-z0-9].*");

        return hasUpper && hasLower && hasSpecial;
    }




}
