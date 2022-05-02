package ru.rsreu.contests_system.api.organization.event.participant_info.task_solution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum ExecutionStatus {
    NO_SOLUTION(null),
    ON_CHECKING(null),
    OK("OK"),
    ALREADY_TEST("AlreadyTest"),
    NO_SPACE("NoSpace"),
    COMPILE_FAIL("CompileFail"),
    RUNTIME_ERROR("RuntimeError"),
    UNSUPPORTED_LANGUAGE("UnsupportedLang"),
    IO_FAIL("IoFail"),
    TIMEOUT("Timeout");

    private final String rustAnalogue;

    private static final Map<String, ExecutionStatus> map;

    static {
        map = new HashMap<>();
        Arrays.stream(ExecutionStatus.values()).forEach(elem -> map.put(elem.rustAnalogue, elem));
    }

    public static ExecutionStatus fromRustAnalogue(String rustAnalogue) {
        return map.get(rustAnalogue);
    }

    ExecutionStatus(String rustAnalogue) {
        this.rustAnalogue = rustAnalogue;
    }
}
