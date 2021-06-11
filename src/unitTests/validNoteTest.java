import domain.Verifier;
import org.junit.jupiter.api.Assertions;

public class validNoteTest {

    @org.junit.jupiter.api.Test
    void noteText() {
        // The text is within the character limit.
        Assertions.assertTrue(Verifier.verifyNoteData("This is a short and valid message.", "2", "1"));

        // The text is above the character limit.
        Assertions.assertFalse(Verifier.verifyNoteData("This text goes over the character limit! Lorem ipsum dolor sit amet, consectetuer dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate.", "2", "1"));
    }

    @org.junit.jupiter.api.Test
    void noteOrder() {
        // The order argument is valid
        Assertions.assertTrue(Verifier.verifyNoteData("Note text.", "20", "1"));

        // The order argument is invalid
        Assertions.assertFalse(Verifier.verifyNoteData("Note text.", "twenty", "1"));
    }

    @org.junit.jupiter.api.Test
    void noteRow() {
        // The row argument is valid
        Assertions.assertTrue(Verifier.verifyNoteData("Note text.", "2", "20"));

        // The row argument is invalid
        Assertions.assertFalse(Verifier.verifyNoteData("Note text.", "2", "twenty"));
    }
}
