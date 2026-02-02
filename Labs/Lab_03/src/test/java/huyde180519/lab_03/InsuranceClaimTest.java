package huyde180519.lab_03;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class InsuranceClaimTest {

    @Test
    public void testProcessClaimSuccess() {
        InsuranceClaim claim = new InsuranceClaim("C001", 1000);
        boolean result = claim.processClaim("Approved");
        assertTrue(result);
        assertEquals("Approved", claim.getClaimStatus());
    }

    @Test
    public void testCalculatePayoutApproved() {
        InsuranceClaim claim = new InsuranceClaim("C002", 2000);
        claim.processClaim("Approved");
        assertEquals(1700, claim.calculatePayout());
    }

    @Test
    public void testCalculatePayoutNotApproved() {
        InsuranceClaim claim = new InsuranceClaim("C003", 1500);
        assertEquals(0, claim.calculatePayout());
    }

    @Test
    public void testUpdateClaimAmountInvalid() {
        InsuranceClaim claim = new InsuranceClaim("C004", 1000);
        assertThrows(IllegalArgumentException.class, () -> {
            claim.updateClaimAmount(-500);
        });
    }
}
