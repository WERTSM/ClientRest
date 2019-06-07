import api.IRestProjectController;
import api.IRestUserController;
import dto.Account;
import dto.ProjectDTO;
import enumeration.Status;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.*;
import util.ConverterUtil;
import util.FeingUtil;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class RestFeignProjectTest {

    private static IRestProjectController restProjectController;
    private static IRestUserController restUserController;

    private String testUserId;

    @BeforeClass
    public static void getInstance() {
        restUserController = FeingUtil.getInstance(IRestUserController.class);
        restProjectController = FeingUtil.getInstance(IRestProjectController.class);
    }

    @Before
    public void registrationAndLoginTestUser() {
        @NotNull final String login = "test";
        @NotNull final String password = "test";
        @NotNull final Account account = new Account(login, password);
        testUserId = restUserController.registration(account);
        restUserController.login(account);
    }

    @Test
    public void createAndFindProjectTest() {
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

        @Nullable final ProjectDTO projectDTOfromServer = restProjectController.findProject(projectDTO.getId());

        System.out.println(projectDTOfromServer);
        assert projectDTOfromServer != null;

        Assert.assertEquals(projectDTOfromServer.getId(), projectDTO.getId());
        Assert.assertEquals(projectDTOfromServer.getName(), projectDTO.getName());
        Assert.assertEquals(projectDTOfromServer.getDescription(), projectDTO.getDescription());
        Assert.assertEquals(ConverterUtil.convertDateFormat(projectDTOfromServer.getDateStart()), ConverterUtil.convertDateFormat(projectDTO.getDateStart()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(projectDTOfromServer.getDateFinish()), ConverterUtil.convertDateFormat(projectDTO.getDateFinish()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(projectDTOfromServer.getDateCreate()), ConverterUtil.convertDateFormat(projectDTO.getDateCreate()));
        Assert.assertEquals(projectDTOfromServer.getStatus(), projectDTO.getStatus());
        Assert.assertEquals(projectDTOfromServer.getUserId(), projectDTO.getUserId());
    }

    @Test
    public void findAllProjectTest() {
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

        @NotNull final ProjectDTO projectDTO2 = new ProjectDTO();
        projectDTO2.setId(UUID.randomUUID().toString());
        projectDTO2.setName("ProjectNameTest2");
        projectDTO2.setDescription("ProjectDescriptionTest2");
        projectDTO2.setDateCreate(new Date());
        projectDTO2.setDateStart(new Date());
        projectDTO2.setDateFinish(new Date());
        projectDTO2.setStatus(Status.INPROGRESS);
        projectDTO2.setUserId(testUserId);

        restProjectController.projectCreate(projectDTO2);

        final Collection<ProjectDTO> collection = restProjectController.findAllProject();

        Assert.assertEquals(collection.size(), 2);

        @NotNull ProjectDTO projectDTOfromServer = Objects.requireNonNull(restProjectController.findProject(projectDTO.getId()));

        Assert.assertEquals(projectDTOfromServer.getId(), projectDTO.getId());
        Assert.assertEquals(projectDTOfromServer.getName(), projectDTO.getName());
        Assert.assertEquals(projectDTOfromServer.getDescription(), projectDTO.getDescription());
        Assert.assertEquals(ConverterUtil.convertDateFormat(projectDTOfromServer.getDateStart()), ConverterUtil.convertDateFormat(projectDTO.getDateStart()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(projectDTOfromServer.getDateFinish()), ConverterUtil.convertDateFormat(projectDTO.getDateFinish()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(projectDTOfromServer.getDateCreate()), ConverterUtil.convertDateFormat(projectDTO.getDateCreate()));
        Assert.assertEquals(projectDTOfromServer.getStatus(), projectDTO.getStatus());
        Assert.assertEquals(projectDTOfromServer.getUserId(), projectDTO.getUserId());

        projectDTOfromServer = Objects.requireNonNull(restProjectController.findProject(projectDTO2.getId()));

        Assert.assertEquals(projectDTOfromServer.getId(), projectDTO2.getId());
        Assert.assertEquals(projectDTOfromServer.getName(), projectDTO2.getName());
        Assert.assertEquals(projectDTOfromServer.getDescription(), projectDTO2.getDescription());
        Assert.assertEquals(ConverterUtil.convertDateFormat(projectDTOfromServer.getDateStart()), ConverterUtil.convertDateFormat(projectDTO2.getDateStart()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(projectDTOfromServer.getDateFinish()), ConverterUtil.convertDateFormat(projectDTO2.getDateFinish()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(projectDTOfromServer.getDateCreate()), ConverterUtil.convertDateFormat(projectDTO2.getDateCreate()));
        Assert.assertEquals(projectDTOfromServer.getStatus(), projectDTO2.getStatus());
        Assert.assertEquals(projectDTOfromServer.getUserId(), projectDTO2.getUserId());
    }

    @Test
    public void editProjectTest() {
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

        @NotNull final ProjectDTO editProjectDTO = projectDTO;
        editProjectDTO.setName("ProjectEditNameTest");
        editProjectDTO.setDescription("ProjectEditDescriptionTest");
        editProjectDTO.setDateStart(new Date());
        editProjectDTO.setDateFinish(new Date());
        editProjectDTO.setStatus(Status.DONE);

        restProjectController.projectCreate(editProjectDTO);

        @NotNull ProjectDTO projectDTOfromServer = Objects.requireNonNull(restProjectController.findProject(projectDTO.getId()));

        Assert.assertEquals(projectDTOfromServer.getId(), editProjectDTO.getId());
        Assert.assertEquals(projectDTOfromServer.getName(), editProjectDTO.getName());
        Assert.assertEquals(projectDTOfromServer.getDescription(), editProjectDTO.getDescription());
        Assert.assertEquals(ConverterUtil.convertDateFormat(projectDTOfromServer.getDateStart()), ConverterUtil.convertDateFormat(editProjectDTO.getDateStart()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(projectDTOfromServer.getDateFinish()), ConverterUtil.convertDateFormat(editProjectDTO.getDateFinish()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(projectDTOfromServer.getDateCreate()), ConverterUtil.convertDateFormat(editProjectDTO.getDateCreate()));
        Assert.assertEquals(projectDTOfromServer.getStatus(), editProjectDTO.getStatus());
        Assert.assertEquals(projectDTOfromServer.getUserId(), editProjectDTO.getUserId());
    }

    @Test
    public void removeProjectTest() {
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
        restProjectController.projectDelete(projectDTO.getId());
        Collection<ProjectDTO> collection = restProjectController.findAllProject();

        Assert.assertEquals(collection.size(), 0);
    }

    @After
    public void deleteTestUser() {
        restUserController.removeUser(testUserId);
    }
}