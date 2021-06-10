package domain.strategy.save;

import domain.Main;
import domain.dao.NoteDao;
import domain.dao.NoteDaoImpl;

/**
 * This class implements a saving algorithm, which saves a project to
 * a database. This strategy uses a DAO object to insert into the
 * the database.
 */
public class MultiUserSaveStrategy implements SaveStrategy {
    private NoteDao noteDao;

    public MultiUserSaveStrategy() {
        this.noteDao = new NoteDaoImpl();
    }

    @Override
    public void save() {
        noteDao.saveNotes(Main.projectID);
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }
}
