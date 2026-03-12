package tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import utils.DriverFactory;

public class BaseTest {

    protected static WebDriver driver;

    // Base URL of the MiniSocialNetwork ASP.NET application
    protected static final String baseUrl = "https://localhost:7009";

    // Default account used by most authenticated flows
    protected static final String DEFAULT_USERNAME = "phanhuy12";
    protected static final String DEFAULT_PASSWORD = "TempPass1";

    // Reusable data tokens for CSV-based tests
    protected static final String TOKEN_EMPTY = "EMPTY";
    protected static final String TOKEN_KEEP = "KEEP";
    protected static final String TOKEN_TOO_LONG = "TOO_LONG";

    // Reusable expectation keys
    protected static final String EXPECT_SUCCESS = "success";
    protected static final String EXPECT_ERROR = "error";
    protected static final String EXPECT_CANCEL = "cancel";
    protected static final String EXPECT_FOUND = "found";
    protected static final String EXPECT_EMPTY = "empty";

    // ===== CaseMeta record – stores CSV metadata for readable assertion messages =====

    /**
     * Lightweight container for the first 9 standard columns of every CSV row.
     * Used by {@link #caseMessage(CaseMeta, String)} to produce descriptive
     * assertion messages such as:
     *   [TC_LOG_001] Login with valid credentials – Login success scenario must redirect away from /Account/Login
     */
    protected record CaseMeta(
        String testCaseId,
        String testCaseDescription,
        String preConditions,
        String testCaseProcedure,
        String testData,
        String expectedResults,
        String priority,
        String severity,
        String status
    ) {}

    /**
     * Factory helper – constructs a {@link CaseMeta} from the 9 standard CSV columns.
     */
    protected static CaseMeta metadata(String testCaseId,
                                       String testCaseDescription,
                                       String preConditions,
                                       String testCaseProcedure,
                                       String testData,
                                       String expectedResults,
                                       String priority,
                                       String severity,
                                       String status) {
        return new CaseMeta(
            clean(testCaseId),
            clean(testCaseDescription),
            clean(preConditions),
            clean(testCaseProcedure),
            clean(testData),
            clean(expectedResults),
            clean(priority),
            clean(severity),
            clean(status)
        );
    }

    /**
     * Builds a human-readable assertion message prefixed with the test-case id
     * and description.  Example output:
     *   [TC_LOG_001] Login with valid credentials – Login success scenario must redirect away from /Account/Login
     */
    protected static String caseMessage(CaseMeta meta, String detail) {
        return "[" + meta.testCaseId() + "] " + meta.testCaseDescription() + " – " + detail;
    }

    // ===== Utility helpers =====

    protected static String clean(String value) {
        return value == null ? "" : value.trim();
    }

    protected static String resolveEmptyToken(String value) {
        String normalized = clean(value);
        return TOKEN_EMPTY.equalsIgnoreCase(normalized) ? "" : normalized;
    }

    protected static String resolveKeepOrEmptyToken(String value, String currentValue) {
        String normalized = clean(value);
        if (TOKEN_KEEP.equalsIgnoreCase(normalized)) {
            return currentValue == null ? "" : currentValue;
        }
        if (TOKEN_EMPTY.equalsIgnoreCase(normalized)) {
            return "";
        }
        return normalized;
    }

    protected static boolean expect(String actual, String expected) {
        return expected.equalsIgnoreCase(clean(actual));
    }

    @BeforeAll
    public static void setupClass() {
        driver = DriverFactory.getDriver();
    }

    @AfterAll
    public static void tearDown() {
        DriverFactory.quitDriver();
    }
}