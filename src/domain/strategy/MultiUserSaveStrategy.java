package domain.strategy;

import domain.Main;
import domain.Storage;
import domain.dao.NoteDao;
import domain.dao.NoteDaoImpl;

/**
 * This class implements a saving algorithm, which saves a project to
 * a database. This strategy uses a DAO object to insert into the
 * the database.
 */
public class MultiUserSaveStrategy implements SaveStrategy {
    private NoteDao noteDao = new NoteDaoImpl();

    @Override
    public void save() {
        noteDao.saveNotes(Main.projectID);
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }
}
