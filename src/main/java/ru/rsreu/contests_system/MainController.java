package ru.rsreu.contests_system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rsreu.contests_system.organization.Organization;
import ru.rsreu.contests_system.organization.OrganizationRepository;
import ru.rsreu.contests_system.organization.event.Event;
import ru.rsreu.contests_system.organization.event.EventType;
import ru.rsreu.contests_system.organization.event.participant_info.ParticipantInfo;
import ru.rsreu.contests_system.organization.event.participant_info.appeal.Appeal;
import ru.rsreu.contests_system.organization.event.participant_info.appeal.AppealStatus;
import ru.rsreu.contests_system.organization.event.participant_info.task_solution.SolutionStatus;
import ru.rsreu.contests_system.organization.event.participant_info.task_solution.TaskSolution;
import ru.rsreu.contests_system.organization.event.participant_info.task_solution.TestInfo;
import ru.rsreu.contests_system.task.Task;
import ru.rsreu.contests_system.task.TaskRepository;
import ru.rsreu.contests_system.task.TaskTest;
import ru.rsreu.contests_system.user.Role;
import ru.rsreu.contests_system.user.User;
import ru.rsreu.contests_system.user.UserRepository;

import javax.servlet.http.Part;
import java.time.LocalDateTime;

@RestController
public class MainController {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public MainController(UserRepository userRepository, OrganizationRepository organizationRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/test")
    public User test() {
        TaskTest taskTest = new TaskTest("0 1 2 3 4", "5\n1");
        Task task = Task.builder().text("Test task").memoryLimit(1000).timeLimit(1000).build();
        task.addTaskTest(taskTest);
        taskRepository.insert(task);

        User user = new User("maxik", "maxik", Role.MASTER_ORGANIZER, "test", "rsreu");
        Organization organization = Organization.builder().name("testOrg").organizationLeader(user).build();
        user = new User("pavel", "pavel", Role.ORGANIZER, "test", "rsreu");
        organization.addOrganizer(user);
        Event event = Event.builder().eventType(EventType.OLYMPIAD).startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .eventLeader(user).build();
        event.addEventOrganizer(user);
        event.addTask(task);
        user = new User("alesha", "alesha", Role.PARTICIPANT, "test", "rsreu");
        ParticipantInfo participantInfo = ParticipantInfo.builder().participant(user).appeal(Appeal.builder().appealStatus(AppealStatus.PENDING).text("ololo").build()).build();
        TestInfo testInfo = TestInfo.builder().memory(500).time(500).score(228).build();
        TaskSolution taskSolution = TaskSolution.builder().solution("solution").solutionStatus(SolutionStatus.COMPILE_ERROR).task(task).build();
        taskSolution.addTestInfo(testInfo);
        participantInfo.addTaskSolution(taskSolution);
        event.addParticipantInfo(participantInfo);
        organization.addEvent(event);
        organizationRepository.insert(organization);
        return user;
    }

    @GetMapping("/org")
    public Organization orgTest() {
        return organizationRepository.findOrganizationByOrganizationLeader(userRepository.findAll().get(0));
    }

}
