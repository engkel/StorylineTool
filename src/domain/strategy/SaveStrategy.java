package domain.strategy;

/**
 * The interface of the save strategy pattern. Since there are two ways
 * of saving a project, we have implemented this interface with the classes
 * SingleUserSaveStrategy and MultiUserSaveStrategy.
 * These two classes do not directly implement the saving algorithm, but
 * instead calls another class, namely Storage, which has the real
 * implementation.
 */
public interface SaveStrategy {
    void save();
}
