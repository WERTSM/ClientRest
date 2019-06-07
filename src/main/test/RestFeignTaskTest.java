import api.IRestProjectController;
import api.IRestTaskController;
import api.IRestUserController;
import dto.Account;
import dto.ProjectDTO;
import dto.TaskDTO;
import enumeration.Status;
import org.jetbrains.annotations.NotNull;
import org.junit.*;
import util.ConverterUtil;
import util.FeingUtil;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class RestFeignTaskTest {

    private static IRestTaskController restTaskController;
    private static IRestProjectController restProjectController;
    private static IRestUserController restUserController;

    private String testUserId;
    private ProjectDTO testprojectDTO;

    @BeforeClass
    public static void getInstance() {
        restTaskController = FeingUtil.getInstance(IRestTaskController.class);
        restProjectController = FeingUtil.getInstance(IRestProjectController.class);
        restUserController = FeingUtil.getInstance(IRestUserController.class);
    }

    @Before
    public void registrationAndLoginTestUserAndCreateProject() {
        @NotNull final String login = "test";
        @NotNull final String password = "test";
        @NotNull final Account account = new Account(login, password);
        testUserId = restUserController.registration(account);
        restUserController.login(account);

        @NotNull final ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(UUID.randomUUID().toString());
        projectDTO.setName("ProjectNameTest");
        projectDTO.setDescription("ProjectDescriptionTest");
        projectDTO.setDateCreate(new Date());
        projectDTO.setDateStart(new Date());
        projectDTO.setDateFinish(new Date());
        projectDTO.setStatus(Status.INPROGRESS);
        projectDTO.setUserId(testUserId);

        restProjectController.projectCreate(projectDTO);
        testprojectDTO = projectDTO;
    }

    @Test
    public void createAndFindTaskTest() {
        @NotNull final TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(UUID.randomUUID().toString());
        taskDTO.setName("TasktNameTest");
        taskDTO.setDescription("TaskDescriptionTest");
        taskDTO.setDateCreate(new Date());
        taskDTO.setDateStart(new Date());
        taskDTO.setDateFinish(new Date());
        taskDTO.setStatus(Status.INPROGRESS);
        taskDTO.setProjectId(testprojectDTO.getId());
        taskDTO.setUserId(testUserId);

        restTaskController.taskCreate(taskDTO);

        @NotNull final TaskDTO taskDTOfromServer = Objects.requireNonNull(restTaskController.findTask(taskDTO.getId()));

        Assert.assertEquals(taskDTOfromServer.getId(), taskDTO.getId());
        Assert.assertEquals(taskDTOfromServer.getName(), taskDTO.getName());
        Assert.assertEquals(taskDTOfromServer.getDescription(), taskDTO.getDescription());
        Assert.assertEquals(ConverterUtil.convertDateFormat(taskDTOfromServer.getDateStart()), ConverterUtil.convertDateFormat(taskDTO.getDateStart()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(taskDTOfromServer.getDateFinish()), ConverterUtil.convertDateFormat(taskDTO.getDateFinish()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(taskDTOfromServer.getDateCreate()), ConverterUtil.convertDateFormat(taskDTO.getDateCreate()));
        Assert.assertEquals(taskDTOfromServer.getStatus(), taskDTO.getStatus());
        Assert.assertEquals(taskDTOfromServer.getProjectId(), taskDTO.getProjectId());
        Assert.assertEquals(taskDTOfromServer.getUserId(), taskDTO.getUserId());
    }

    @Test
    public void findAllTaskTest() {
        @NotNull final TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(UUID.randomUUID().toString());
        taskDTO.setName("TaskNameTest");
        taskDTO.setDescription("TaskDescriptionTest");
        taskDTO.setDateCreate(new Date());
        taskDTO.setDateStart(new Date());
        taskDTO.setDateFinish(new Date());
        taskDTO.setStatus(Status.INPROGRESS);
        taskDTO.setProjectId(testprojectDTO.getId());
        taskDTO.setUserId(testUserId);

        restTaskController.taskCreate(taskDTO);

        @NotNull final TaskDTO taskDTO2 = new TaskDTO();
        taskDTO2.setId(UUID.randomUUID().toString());
        taskDTO2.setName("TaskNameTest2");
        taskDTO2.setDescription("TaskDescriptionTest2");
        taskDTO2.setDateCreate(new Date());
        taskDTO2.setDateStart(new Date());
        taskDTO2.setDateFinish(new Date());
        taskDTO2.setStatus(Status.INPROGRESS);
        taskDTO2.setProjectId(testprojectDTO.getId());
        taskDTO2.setUserId(testUserId);

        restTaskController.taskCreate(taskDTO2);

        final Collection<TaskDTO> collection = restTaskController.findAllTask();

        Assert.assertEquals(collection.size(), 2);

        @NotNull TaskDTO taskDTOfromServer = Objects.requireNonNull(restTaskController.findTask(taskDTO.getId()));

        Assert.assertEquals(taskDTOfromServer.getId(), taskDTO.getId());
        Assert.assertEquals(taskDTOfromServer.getName(), taskDTO.getName());
        Assert.assertEquals(taskDTOfromServer.getDescription(), taskDTO.getDescription());
        Assert.assertEquals(ConverterUtil.convertDateFormat(taskDTOfromServer.getDateStart()), ConverterUtil.convertDateFormat(taskDTO.getDateStart()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(taskDTOfromServer.getDateFinish()), ConverterUtil.convertDateFormat(taskDTO.getDateFinish()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(taskDTOfromServer.getDateCreate()), ConverterUtil.convertDateFormat(taskDTO.getDateCreate()));
        Assert.assertEquals(taskDTOfromServer.getStatus(), taskDTO.getStatus());
        Assert.assertEquals(taskDTOfromServer.getStatus(), taskDTO.getStatus());
        Assert.assertEquals(taskDTOfromServer.getUserId(), taskDTO.getUserId());

        taskDTOfromServer = Objects.requireNonNull(restTaskController.findTask(taskDTO2.getId()));

        Assert.assertEquals(taskDTOfromServer.getId(), taskDTO2.getId());
        Assert.assertEquals(taskDTOfromServer.getName(), taskDTO2.getName());
        Assert.assertEquals(taskDTOfromServer.getDescription(), taskDTO2.getDescription());
        Assert.assertEquals(ConverterUtil.convertDateFormat(taskDTOfromServer.getDateStart()), ConverterUtil.convertDateFormat(taskDTO2.getDateStart()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(taskDTOfromServer.getDateFinish()), ConverterUtil.convertDateFormat(taskDTO2.getDateFinish()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(taskDTOfromServer.getDateCreate()), ConverterUtil.convertDateFormat(taskDTO2.getDateCreate()));
        Assert.assertEquals(taskDTOfromServer.getStatus(), taskDTO2.getStatus());
        Assert.assertEquals(taskDTOfromServer.getStatus(), taskDTO2.getStatus());
        Assert.assertEquals(taskDTOfromServer.getUserId(), taskDTO2.getUserId());
    }

    @Test
    public void editTaskTest() {
        @NotNull final TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(UUID.randomUUID().toString());
        taskDTO.setName("TaskNameTest");
        taskDTO.setDescription("TaskDescriptionTest");
        taskDTO.setDateCreate(new Date());
        taskDTO.setDateStart(new Date());
        taskDTO.setDateFinish(new Date());
        taskDTO.setStatus(Status.INPROGRESS);
        taskDTO.setProjectId(testprojectDTO.getId());
        taskDTO.setUserId(testUserId);

        restTaskController.taskCreate(taskDTO);

        @NotNull final TaskDTO editTaskDTO = taskDTO;
        editTaskDTO.setName("TaskEditNameTest");
        editTaskDTO.setDescription("TaskEditDescriptionTest");
        editTaskDTO.setDateStart(new Date());
        editTaskDTO.setDateFinish(new Date());
        editTaskDTO.setStatus(Status.DONE);

        restTaskController.taskCreate(editTaskDTO);

        @NotNull TaskDTO taskDTOfromServer = Objects.requireNonNull(restTaskController.findTask(taskDTO.getId()));

        Assert.assertEquals(taskDTOfromServer.getId(), taskDTO.getId());
        Assert.assertEquals(taskDTOfromServer.getName(), taskDTO.getName());
        Assert.assertEquals(taskDTOfromServer.getDescription(), taskDTO.getDescription());
        Assert.assertEquals(ConverterUtil.convertDateFormat(taskDTOfromServer.getDateStart()), ConverterUtil.convertDateFormat(taskDTO.getDateStart()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(taskDTOfromServer.getDateFinish()), ConverterUtil.convertDateFormat(taskDTO.getDateFinish()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(taskDTOfromServer.getDateCreate()), ConverterUtil.convertDateFormat(taskDTO.getDateCreate()));
        Assert.assertEquals(taskDTOfromServer.getStatus(), taskDTO.getStatus());
        Assert.assertEquals(taskDTOfromServer.getStatus(), taskDTO.getStatus());
        Assert.assertEquals(taskDTOfromServer.getUserId(), taskDTO.getUserId());
    }

    @Test
    public void removeTaskTest() {

        @NotNull final TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(UUID.randomUUID().toString());
        taskDTO.setName("TaskNameTest");
        taskDTO.setDescription("TaskDescriptionTest");
        taskDTO.setDateCreate(new Date());
        taskDTO.setDateStart(new Date());
        taskDTO.setDateFinish(new Date());
        taskDTO.setStatus(Status.INPROGRESS);
        taskDTO.setProjectId(testprojectDTO.getId());
        taskDTO.setUserId(testUserId);

        restTaskController.taskCreate(taskDTO);

        restTaskController.taskDelete(taskDTO.getId());

        Collection<TaskDTO> collection = restTaskController.findAllTask();

        Assert.assertEquals(collection.size(), 0);
    }

    @After
    public void deleteTestUser() {
        restUserController.removeUser(testUserId);
    }
}