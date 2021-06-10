package domain.strategy;

/**
 * The interface of the delete strategy pattern. Since there are two ways
 * of deleting a project, we have implemented this interface with the classes
 * SingleUserDeleteStrategy and MultiUserDeleteStrategy.
 * These two classes do not directly implement the deletion algorithm, but
 * instead calls another class, namely Storage, which has the real
 * implementation.
 */
public interface DeleteStrategy {
    void delete();
}
