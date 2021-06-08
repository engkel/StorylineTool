package domain;

public class Note {
    public String text;
    public int order;
    public int row;

    // Constructor for the domain.Note class,
    // Make sure to check for duplicate notes with the checkDuplicate
    // method, before adding any notes to the domain.Main.notes linkedlist.
    public Note(String text, int order, int row) {
        this.text = text;
        this.order = order;
        this.row = row;
    }

    /**
     * Checks if there is a note in the notes linkedlist
     * with the same order and row.
     * @param order the order number to check
     * @param row the row to check
     * @return true = duplicate (Another note already exists), false = not a duplicate.
     */
    static boolean checkDuplicate(int order, int row) {
        for (int i = 0; i < Main.notes.size(); i++) {
            if (Main.notes.get(i).order != order) {
                continue;
            }
            if (Main.notes.get(i).row == row) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method will add a note to the linked list.
     * The order and row are from the user, so we can
     * not trust that those are the final values.
     * @param text The text of the note to add
     * @param order The order given by the user, this will often not be the final order
     * @param row The row given by the user
     */
    static void addNoteToLinkedList(String text, int order, int row) {
        if (Main.notes.size() == 0) {
            Main.notes.add(new Note(text, 2, row));
            return;
        }

        int addedIndex = -1;

        // Adding the note
        for (int i = 0; i < Main.notes.size(); i++) {
            // Order is larger, always add
            if (Main.notes.get(i).order > order) {
                Main.notes.add(i, new Note(text, order, row));
                addedIndex = i;
                break;
            }

            // Order is the same, check the row.
            if (Main.notes.get(i).order == order) {
                // Only add if the current item in the linked list has a larger row
                if (Main.notes.get(i).row > row) {
                    Main.notes.add(i + 1, new Note(text, order, row));
                    addedIndex = i;
                    break;
                }
            }
        }

        // Check if the note was added, if not,
        // then it has to be added at the end of the linked list.
        if (addedIndex == -1) {
            // If the order number of the previous note is the same,
            // then add the current note to the end.
            if (Main.notes.get(Main.notes.size()-1).order == order) {
                Main.notes.add(new Note(text, order, row));
                return;
            }
            // Add the note to the end of the linked list,
            // with an order 2 larger than the previous.
            // We do this to make sure that the order of this row,
            // is not some very large number.
            Main.notes.add(new Note(text, Main.notes.get(Main.notes.size()-1).order + 2, row));
            return;
        }

        // Check if we need to fix all the order numbers.
        // If the previous note has the same order, then we
        // do not need to update the order number for any
        // other notes.
        if (addedIndex != 0 && order % 2 != 1) {
            if (Main.notes.get(addedIndex - 1).order == order) {
                return;
            }
        }

        // Just in case (I haven't actually checked if it's possible)
        if (order % 2 == 0) {
            System.out.println("THIS SHOULD NEVER HAPPEN, INVESTIGATE NOW! :)");
        }

        // Change the uneven number to be an even number 3 -> 4
        Main.notes.get(addedIndex).order++;


        // Fix the order values so they all are even numbers
        // We will start from the added item, since all elements
        // before that point already have a correct value.
        for (int i = addedIndex + 1; i < Main.notes.size(); i++) {
            Main.notes.get(i).order += 2;
        }
    }
}
