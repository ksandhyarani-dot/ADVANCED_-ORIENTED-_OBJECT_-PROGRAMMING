
package waypoint.model;

import java.time.Instant;
import java.util.Objects;

public final class Ticket {

    private final TicketId id;
    private final SpotId spotId;
    private final Instant entry;

    // Exactly two non-final fields
    private Instant exit;
    private Money fare;

    private Ticket(TicketId id, SpotId spotId, Instant entry) {
        this.id = Objects.requireNonNull(id);
        this.spotId = Objects.requireNonNull(spotId);
        this.entry = Objects.requireNonNull(entry);
    }

    public static Ticket open(TicketId id, SpotId spotId, Instant entry) {
        return new Ticket(id, spotId, entry);
    }

    public boolean isOpen() {
        return exit == null;
    }
    public Money fare() {
    return fare;
}

    public Money close(Instant at, FarePolicy policy) {
        Objects.requireNonNull(at);
        Objects.requireNonNull(policy);

        if (!isOpen()) {
            throw new TicketAlreadyClosed();
        }

        if (at.isBefore(entry)) {
            throw new IllegalArgumentException("Exit cannot be before entry");
        }

        Money calculatedFare = Objects.requireNonNull(
        policy.fareFor(entry, at, spotId)
);

        if (calculatedFare.isNegative()) {
            throw new IllegalArgumentException("Fare cannot be negative");
        }

        this.exit = at;
        this.fare = calculatedFare;

        return fare;
    }
}
class TicketAlreadyClosed extends IllegalStateException {
    TicketAlreadyClosed() {
        super("Ticket is already closed");
    }
}
