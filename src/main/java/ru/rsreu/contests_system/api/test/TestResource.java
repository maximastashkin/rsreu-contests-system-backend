package ru.rsreu.contests_system.api.test;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rsreu.contests_system.api.organization.Organization;
import ru.rsreu.contests_system.api.organization.event.Event;
import ru.rsreu.contests_system.api.organization.event.EventType;
import ru.rsreu.contests_system.api.organization.service.OrganizationService;
import ru.rsreu.contests_system.api.task.Task;
import ru.rsreu.contests_system.api.task.TaskTest;
import ru.rsreu.contests_system.api.task.service.TaskService;
import ru.rsreu.contests_system.api.user.Authority;
import ru.rsreu.contests_system.api.user.User;
import ru.rsreu.contests_system.api.user.service.UserService;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/test")
@AllArgsConstructor
public class TestResource {
    private final OrganizationService organizationService;
    private final UserService userService;
    private final TaskService taskService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/generate")
    public void generateTestData() {
        if (userService.getAll(Integer.MAX_VALUE, 0).isEmpty()) {
            User admin = User.builder()
                    .email("admin@mail.ru")
                    .password(passwordEncoder.encode("0000"))
                    .firstName("admin")
                    .lastName("admin")
                    .educationPlace("rsreu")
                    .authorities(EnumSet.of(Authority.ADMIN, Authority.UNBLOCKED, Authority.ACTIVE))
                    .build();
            userService.save(admin);
            User participant = User.builder()
                    .email("participant@mail.ru")
                    .password(passwordEncoder.encode("0000"))
                    .firstName("participant")
                    .lastName("participant")
                    .educationPlace("rsreu")
                    .authorities(EnumSet.of(Authority.PARTICIPANT, Authority.UNBLOCKED, Authority.ACTIVE))
                    .build();
            userService.save(participant);
            User leader = User.builder()
                    .email("leader@mail.ru")
                    .password(passwordEncoder.encode("0000"))
                    .firstName("leader")
                    .lastName("leader")
                    .educationPlace("rsreu")
                    .authorities(EnumSet.of(Authority.ORGANIZATION_LEADER, Authority.UNBLOCKED, Authority.ACTIVE))
                    .build();
            userService.save(leader);
            User organizer = User.builder()
                    .email("organizer@mail.ru")
                    .password(passwordEncoder.encode("0000"))
                    .firstName("organizer")
                    .lastName("organizer")
                    .educationPlace("rsreu")
                    .authorities(EnumSet.of(Authority.ORGANIZER, Authority.UNBLOCKED, Authority.ACTIVE))
                    .build();
            userService.save(organizer);
        }
        if (taskService.getAll().isEmpty()) {
            Task task = Task.builder()
                    .text("На вход даются 2 числа a и b. Необходимо вывести сумму этих чисел")
                    .tests(new HashSet<>() {
                        {
                            addAll(List.of(
                                    TaskTest.builder()
                                            .input("1 2")
                                            .output("3")
                                            .isPublic(true)
                                            .build(),
                                    TaskTest.builder()
                                            .input("5 6")
                                            .output("11")
                                            .isPublic(true)
                                            .build(),
                                    TaskTest.builder()
                                            .input("-5 5")
                                            .output("0")
                                            .build(),
                                    TaskTest.builder()
                                            .input("-7 -2")
                                            .output("-9")
                                            .build(),
                                    TaskTest.builder()
                                            .input("-15 3")
                                            .output("-12")
                                            .build()
                            ));
                        }
                    })
                    .memoryLimit(10000)
                    .timeLimit(10000)
                    .build();
            taskService.save(task);
        }
        if (organizationService.getAll(Integer.MAX_VALUE, 0).isEmpty()) {
            Event nonActualEvent = Event.builder()
                    .name("Non-actual test event")
                    .eventType(EventType.OLYMPIAD)
                    .timeLimit(4 * 60 * 60)
                    .startDateTime(LocalDateTime.of(2022, 4, 1, 10,30))
                    .endDateTime(LocalDateTime.of(2022, 4, 7, 22, 30))
                    .eventLeader(userService.getUserByEmail("organizer@mail.ru"))
                    .tasks(new HashSet<>() {
                        {
                            add(taskService.getAll().get(0));
                        }
                    })
                    .build();
            Event actualNonStartedEvent = Event.builder()
                    .name("Actual non started test event")
                    .eventType(EventType.OLYMPIAD)
                    .timeLimit(4 * 60 * 60)
                    .startDateTime(LocalDateTime.of(2022, 7, 1, 10,30))
                    .endDateTime(LocalDateTime.of(2022, 7, 7, 22, 30))
                    .eventLeader(userService.getUserByEmail("organizer@mail.ru"))
                    .tasks(new HashSet<>() {
                        {
                            add(taskService.getAll().get(0));
                        }
                    })
                    .build();
            Event startedEvent = Event.builder()
                    .name("Started test event")
                    .eventType(EventType.OLYMPIAD)
                    .timeLimit(4 * 60 * 60)
                    .startDateTime(LocalDateTime.of(2022, 4, 1, 10,30))
                    .endDateTime(LocalDateTime.of(2022, 7, 7, 22, 30))
                    .eventLeader(userService.getUserByEmail("organizer@mail.ru"))
                    .tasks(new HashSet<>() {
                        {
                            add(taskService.getAll().get(0));
                        }
                    })
                    .build();
            Organization organization = Organization.builder()
                    .name("Test organization")
                    .organizationEmail("organization@mail.ru")
                    .organizationPhone("79107777777")
                    .organizationLeader(userService.getUserByEmail("leader@mail.ru"))
                    .organizers(new HashSet<>() {
                        {
                            add(userService.getUserByEmail("organizer@mail.ru"));
                        }
                    })
                    .events(new HashSet<>() {
                        {
                            addAll(List.of(nonActualEvent, startedEvent, actualNonStartedEvent));
                        }
                    })
                    .build();
            organizationService.save(organization);
        }
    }
}
