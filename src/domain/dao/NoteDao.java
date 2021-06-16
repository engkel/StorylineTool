package domain.dao;

/**
 * The DAO patterns interface for the note. This interface makes it
 * possible to save and load notes from a database in many different
 * ways.
 */
public interface NoteDao {
    void saveNotes(int projectID);
    void loadNotes(int projectID);
    void deleteNotes(int projectID);
}
