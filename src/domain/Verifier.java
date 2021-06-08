package domain;

public class Verifier {
    /**
     * This method will verify whether the given note data is valid or not.
     * An example is that the order or the row can't contain any letters.
     * @param text The text of the note
     * @param order The order of the note
     * @param row The row of the note
     * @return True = The data is valid (The note can be added), False = The data is invalid
     */
    public static boolean verifyNoteData(String text, String order, String row) {
        // domain.Note text length limit (To make the program compatible with the database)
        if (text.length() > 512) {
            return false;
        }

        // Make sure that order and row can be converted to valid integers.
        try {
            Integer.parseInt(order);
            Integer.parseInt(row);
        } catch (Exception e) {
            return false;
        }

        // All checks have been passed
        return true;
    }
}
