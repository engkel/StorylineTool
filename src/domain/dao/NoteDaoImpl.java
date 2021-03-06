package domain.dao;

import domain.Storage;

/**
 * Concrete class that implements the NoteDao interface. The real
 * implementation and algorithm is in the Storage class.
 */
public class NoteDaoImpl implements NoteDao{

    @Override
    public void saveNotes(int projectID) {
        Storage.saveToDatabase(projectID);
    }

    @Override
    public void loadNotes(int projectID) {
        Storage.loadFromDatabase(projectID);
    }

    @Override
    public void deleteNotes(int projectID) {
        Storage.deleteProjectDBContent(projectID);
    }
}
