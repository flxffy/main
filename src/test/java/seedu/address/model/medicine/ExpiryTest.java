package seedu.address.model.medicine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class ExpiryTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Expiry(null));
    }

    @Test
    public void constructor_invalidExpiry_throwsIllegalArgumentException() {
        String invalidDate = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Expiry(invalidDate));
    }

    @Test
    public void isValidDate() {
        // null expiry
        Assert.assertThrows(NullPointerException.class, () -> Expiry.isValidDate(null));

        // blank expiry
        assertFalse(Expiry.isValidDate("")); // empty string
        assertFalse(Expiry.isValidDate(" ")); // spaces only

        // missing parts
        assertFalse(Expiry.isValidDate("/01/2020")); // missing day part
        assertFalse(Expiry.isValidDate("0101/2020")); // missing '-' symbol

        // invalid parts
        assertFalse(Expiry.isValidDate("01-01-2020")); // '-' instead of '/'
        assertFalse(Expiry.isValidDate("01.01.2020")); // '.' instead of '/'
        assertFalse(Expiry.isValidDate("0 1/01/2020")); // spaces in day part
        assertFalse(Expiry.isValidDate(" 01/01/2020")); // leading space
        assertFalse(Expiry.isValidDate("01/01/2020 ")); // trailing space
        assertFalse(Expiry.isValidDate("01/01/20a0")); // invalid year
        assertFalse(Expiry.isValidDate("01//01/2020")); // double '-' symbol
        assertFalse(Expiry.isValidDate("01/01/01/2020")); // extra part
        assertFalse(Expiry.isValidDate("2020/01/01")); // wrong order
        assertFalse(Expiry.isValidDate("32/01/2020")); // day more than 31
        assertFalse(Expiry.isValidDate("01/13/2020")); // month more than 12

        // valid expiry
        assertTrue(Expiry.isValidDate("11/11/2020"));
        assertTrue(Expiry.isValidDate("1/1/2020")); // without leading zero

    }
}
