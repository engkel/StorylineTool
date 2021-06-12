import domain.Verifier;
import org.junit.jupiter.api.Assertions;

public class validNoteTest {

    @org.junit.jupiter.api.Test
    void noteText() {
        // The text is within the character limit.
        boolean verified = Verifier.verifyNoteData("This is a short and valid message.", "2", "1");
        Assertions.assertTrue(verified);

        // The text is above the character limit.
        verified = Verifier.verifyNoteData("This text goes over the character limit! Lorem ipsum dolor sit amet, consectetuer dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate.", "2", "1");
        Assertions.assertFalse(verified);
    }

    @org.junit.jupiter.api.Test
    void noteOrder() {
        // The order argument is valid
        boolean verified =Verifier.verifyNoteData("Note text.", "20", "1");
        Assertions.assertTrue(verified);

        // The order argument is invalid
        verified = Verifier.verifyNoteData("Note text.", "twenty", "1");
        Assertions.assertFalse(verified);
    }

    @org.junit.jupiter.api.Test
    void noteRow() {
        // The row argument is valid
        boolean verified = Verifier.verifyNoteData("Note text.", "2", "20");
        Assertions.assertTrue(verified);

        // The row argument is invalid
        verified =Verifier.verifyNoteData("Note text.", "2", "twenty");
        Assertions.assertFalse(verified);
    }
}
