import dto.Account;
import dto.ProjectDTO;
import dto.TaskDTO;
import dto.UserDTO;
import enumeration.Status;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import util.ConverterUtil;

import java.util.*;

public class RestTemplateTaskTest {

    private List listCookie;
    private String testUserId;
    private ProjectDTO testprojectDTO;

    @Before
    public void registrationAndLoginTestUserAndCreateProject() {

        @NotNull final String urlRegistration = "http://localhost:9999/rest/registry/";
        @NotNull final String login = "test";
        @NotNull final String password = "test";
        @NotNull final Account account = new Account(login, password);
        @NotNull final RestTemplate template = new RestTemplate();

        @NotNull HttpEntity entity = new HttpEntity<>(account);
        template.exchange(urlRegistration, HttpMethod.POST, entity, String.class);

        @NotNull final String urlLogin = "http://localhost:9999/rest/login/";
        @NotNull final ResponseEntity<UserDTO> response = template.exchange(urlLogin, HttpMethod.POST, entity, UserDTO.class);
        listCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        testUserId = Objects.requireNonNull(response.getBody()).getId();

        @NotNull final ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(UUID.randomUUID().toString());
        projectDTO.setName("ProjectNameTest");
        projectDTO.setDescription("ProjectDescriptionTest");
        projectDTO.setDateCreate(new Date());
        projectDTO.setDateStart(new Date());
        projectDTO.setDateFinish(new Date());
        projectDTO.setStatus(Status.INPROGRESS);
        projectDTO.setUserId(testUserId);

        @NotNull final String url = "http://localhost:9999/rest/projectCreate/";

        @NotNull HttpHeaders headers = new HttpHeaders();
        headers.addAll(HttpHeaders.COOKIE, listCookie);
        headers.setContentType(MediaType.APPLICATION_JSON);
        entity = new HttpEntity<>(projectDTO, headers);
        template.exchange(url, HttpMethod.POST, entity, Boolean.class);
        testprojectDTO = projectDTO;
    }

    @Test
    public void createAndFindTaskTest() {
        @NotNull final RestTemplate template = new RestTemplate();
        @NotNull HttpHeaders headers = new HttpHeaders();

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

        @NotNull final String urlCreate = "http://localhost:9999/rest/taskCreate/";

        headers.addAll(HttpHeaders.COOKIE, listCookie);
        headers.setContentType(MediaType.APPLICATION_JSON);
        @NotNull HttpEntity entity = new HttpEntity<>(taskDTO, headers);
        template.exchange(urlCreate, HttpMethod.POST, entity, Boolean.class);

        @NotNull final String urlFind = "http://localhost:9999/rest/findTask/" + taskDTO.getId();

        entity = new HttpEntity<String>(headers);
        @NotNull final TaskDTO taskDTOfromServer = Objects.requireNonNull(template.exchange(urlFind, HttpMethod.GET, entity, TaskDTO.class).getBody());

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
        @NotNull final RestTemplate template = new RestTemplate();
        @NotNull HttpHeaders headers = new HttpHeaders();
        @NotNull HttpEntity entity;

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

        @NotNull final String urlCreate = "http://localhost:9999/rest/taskCreate/";

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.addAll(HttpHeaders.COOKIE, listCookie);
        entity = new HttpEntity<>(taskDTO, headers);
        template.exchange(urlCreate, HttpMethod.POST, entity, Boolean.class);

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

        entity = new HttpEntity<>(taskDTO2, headers);
        template.exchange(urlCreate, HttpMethod.POST, entity, Boolean.class);

        @NotNull final String urlFindAll = "http://localhost:9999/rest/findAllTask";

        entity = new HttpEntity<String>(headers);
        @NotNull final ResponseEntity<Collection<TaskDTO>> response = template.exchange(urlFindAll, HttpMethod.GET, entity, new ParameterizedTypeReference<Collection<TaskDTO>>() {
        });
        final Collection<TaskDTO> collection = response.getBody();

        Assert.assertEquals(collection.size(), 2);

        @NotNull String urlFind = "http://localhost:9999/rest/findTask/" + taskDTO.getId();

        entity = new HttpEntity<String>(headers);
        @NotNull TaskDTO taskDTOfromServer = Objects.requireNonNull(template.exchange(urlFind, HttpMethod.GET, entity, TaskDTO.class).getBody());

        Assert.assertEquals(taskDTOfromServer.getId(), taskDTO.getId());
        Assert.assertEquals(taskDTOfromServer.getName(), taskDTO.getName());
        Assert.assertEquals(taskDTOfromServer.getDescription(), taskDTO.getDescription());
        Assert.assertEquals(ConverterUtil.convertDateFormat(taskDTOfromServer.getDateStart()), ConverterUtil.convertDateFormat(taskDTO.getDateStart()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(taskDTOfromServer.getDateFinish()), ConverterUtil.convertDateFormat(taskDTO.getDateFinish()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(taskDTOfromServer.getDateCreate()), ConverterUtil.convertDateFormat(taskDTO.getDateCreate()));
        Assert.assertEquals(taskDTOfromServer.getStatus(), taskDTO.getStatus());
        Assert.assertEquals(taskDTOfromServer.getStatus(), taskDTO.getStatus());
        Assert.assertEquals(taskDTOfromServer.getUserId(), taskDTO.getUserId());

        urlFind = "http://localhost:9999/rest/findTask/" + taskDTO2.getId();
        taskDTOfromServer = Objects.requireNonNull(template.exchange(urlFind, HttpMethod.GET, entity, TaskDTO.class).getBody());

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
        @NotNull final RestTemplate template = new RestTemplate();
        @NotNull HttpHeaders headers = new HttpHeaders();
        @NotNull HttpEntity entity;

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

        @NotNull final String urlCreate = "http://localhost:9999/rest/taskCreate/";

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.addAll(HttpHeaders.COOKIE, listCookie);
        entity = new HttpEntity<>(taskDTO, headers);
        template.exchange(urlCreate, HttpMethod.POST, entity, Boolean.class);

        @NotNull final TaskDTO editTaskDTO = taskDTO;
        editTaskDTO.setName("TaskEditNameTest");
        editTaskDTO.setDescription("TaskEditDescriptionTest");
        editTaskDTO.setDateStart(new Date());
        editTaskDTO.setDateFinish(new Date());
        editTaskDTO.setStatus(Status.DONE);

        @NotNull final String urlEdit = "http://localhost:9999/rest/taskEdit/";

        entity = new HttpEntity<>(editTaskDTO, headers);
        template.exchange(urlEdit, HttpMethod.PUT, entity, Boolean.class);

        @NotNull String urlFind = "http://localhost:9999/rest/findTask/" + taskDTO.getId();

        entity = new HttpEntity<String>(headers);
        @NotNull TaskDTO taskDTOfromServer = Objects.requireNonNull(template.exchange(urlFind, HttpMethod.GET, entity, TaskDTO.class).getBody());

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
        @NotNull final RestTemplate template = new RestTemplate();
        @NotNull HttpHeaders headers = new HttpHeaders();
        @NotNull HttpEntity entity;

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

        @NotNull final String urlCreate = "http://localhost:9999/rest/taskCreate/";

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.addAll(HttpHeaders.COOKIE, listCookie);
        entity = new HttpEntity<>(taskDTO, headers);
        template.exchange(urlCreate, HttpMethod.POST, entity, Boolean.class);

        @NotNull String urlDelete = "http://localhost:9999/rest/taskDelete/" + taskDTO.getId();

        entity = new HttpEntity<String>(headers);
        template.exchange(urlDelete, HttpMethod.DELETE, entity, Boolean.class).getBody();

        @NotNull final String urlFindAll = "http://localhost:9999/rest/findAllTask";

        entity = new HttpEntity<String>(headers);
        @NotNull final ResponseEntity<Collection<TaskDTO>> response = template.exchange(urlFindAll, HttpMethod.GET, entity, new ParameterizedTypeReference<Collection<TaskDTO>>() {
        });
        Collection<TaskDTO> collection = response.getBody();

        Assert.assertEquals(collection.size(), 0);
    }

    @After
    public void deleteTestUser() {
        @NotNull final String url = "http://localhost:9999/rest/userDelete/" + testUserId;
        @NotNull final RestTemplate template = new RestTemplate();
        template.exchange(url, HttpMethod.DELETE, null, Boolean.class);
    }
}