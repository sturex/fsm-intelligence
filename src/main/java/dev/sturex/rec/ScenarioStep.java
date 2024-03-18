package dev.sturex.rec;

public record ScenarioStep<Event extends Enum<Event>>(Event event, int eventIdx) {
}
