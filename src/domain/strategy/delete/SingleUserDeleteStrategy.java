package domain.strategy.delete;

import domain.Storage;

public class SingleUserDeleteStrategy implements DeleteStrategy {
    @Override
    public void delete() {
        Storage.deleteFileContent();
    }
}