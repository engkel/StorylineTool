package domain.strategy;

import domain.Storage;

public class SingleUserDeleteStrategy implements DeleteStrategy {
    @Override
    public void delete() {
        Storage.deleteFileContent();
    }
}