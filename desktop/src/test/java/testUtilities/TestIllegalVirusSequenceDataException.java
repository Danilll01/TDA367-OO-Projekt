package testUtilities;

import com.mygdx.chalmersdefense.model.viruses.IllegalVirusSequenceDataException;
import org.junit.Test;

/**
 * @author Joel Båtsman Hilmersson
 * Test class for getting coverage for the exception that never should be thrown if round data is correct
 */
public class TestIllegalVirusSequenceDataException {

    @Test(expected = IllegalVirusSequenceDataException.class)
    public void testIllegalRoundDataException() {
        throw new IllegalVirusSequenceDataException("This is just to test the class and should normally never be thrown if data is correct format");
    }
}
