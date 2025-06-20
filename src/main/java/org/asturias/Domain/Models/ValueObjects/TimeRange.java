package org.asturias.Domain.Models.ValueObjects;

import java.time.LocalTime;
import java.util.Objects;

public record TimeRange(LocalTime start, LocalTime end) {

    public TimeRange {
        Objects.requireNonNull(start, "Start time cannot be null");
        Objects.requireNonNull(end, "End time cannot be null");


        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }

    public boolean overlapsWith(TimeRange other) {
        return this.start.isBefore(other.end) && this.end.isAfter(other.start);
    }

    public boolean equalsExactly(TimeRange other) {
        return this.start.equals(other.start) && this.end.equals(other.end);
    }
}