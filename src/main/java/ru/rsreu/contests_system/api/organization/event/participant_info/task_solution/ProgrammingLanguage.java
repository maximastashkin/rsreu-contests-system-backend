package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution;

import lombok.Getter;

@Getter
public enum ProgrammingLanguage {
    C("C"),
    CPP("C++"),
    CSHARP("C#"),
    JAVA("Java"),
    JS("JavaScript"),
    KOTLIN("Kotlin"),
    PASCAL("Pascal"),
    PYTHON("Python"),
    RUST("Rust");

    private final String stringRepresentation;

    ProgrammingLanguage(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
}
