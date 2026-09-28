package ee.helmes.backend.exception;

import java.util.Set;

public class UnknownSectorException extends RuntimeException {

    public UnknownSectorException(Set<Integer> unknownSectorIds) {
        super("Unknown sector ids: " + unknownSectorIds);
    }
}
