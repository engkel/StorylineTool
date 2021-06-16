package domain.strategy.delete;

import domain.Main;
import domain.dao.NoteDao;
import domain.dao.NoteDaoImpl;

public class MultiUserDeleteStrategy implements DeleteStrategy {
    private NoteDao noteDao;

    public MultiUserDeleteStrategy() {
        this.noteDao = new NoteDaoImpl();
    }

    @Override
    public void delete() {
        // This will remove all the data for the given project ID,
        // in the timeline table in the database.
        noteDao.deleteNotes(Main.projectID);

        // Delete all the notes in the notes LinkedList
        Main.notes.clear();
    }
}
