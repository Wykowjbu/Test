package huyde180519.lap02.testservice;

import huyde180519.lap02.models.Account;
import huyde180519.lap02.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountServiceTest {
    private final AccountService accountService = new AccountService();

    @ParameterizedTest(name = "Test {index} => registerAccount({0},{1},{2}) = {3}")
    @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
    void testRegisterAccount(String username, String password, String email, boolean expected) {
        Account account = new Account(username, password, email);
        boolean result = accountService.registerAccount(account);
        assertEquals(expected, result,
                () -> "registerAccount(" + username + ", " + password + ", " + email + ") should be " + expected);
    }

    @Test
    void testIsValidEmail() {
        assertEquals(true, accountService.isValidEmail("huy123@gmai.com"));
    }

    @Test
    void testIsValidUsername() {
        assertEquals(true, accountService.isValidUsername("bob123"));
    }
    @Test
    void testIsValidPassword() {
        assertEquals(true, accountService.isValidPassword("Password123@"));
    }
}
