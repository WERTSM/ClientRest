import dto.Account;
import dto.ProjectDTO;
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

public class RestTemplateProjectTest {

    private List listCookie;
    private String testUserId;

    @Before
    public void registrationAndLoginTestUser() {
        @NotNull final String urlRegistration = "http://localhost:9999/rest/registry/";
        @NotNull final String login = "test";
        @NotNull final String password = "test";
        @NotNull final Account account = new Account(login, password);
        @NotNull final RestTemplate template = new RestTemplate();

        @NotNull final HttpEntity entity = new HttpEntity<>(account);
        template.exchange(urlRegistration, HttpMethod.POST, entity, String.class);

        @NotNull final String urlLogin = "http://localhost:9999/rest/login/";
        @NotNull final ResponseEntity<UserDTO> response = template.exchange(urlLogin, HttpMethod.POST, entity, UserDTO.class);
        listCookie = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        testUserId = Objects.requireNonNull(response.getBody()).getId();
    }

    @Test
    public void createAndFindProjectTest() {
        @NotNull final RestTemplate template = new RestTemplate();
        @NotNull HttpHeaders headers = new HttpHeaders();

        @NotNull final ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(UUID.randomUUID().toString());
        projectDTO.setName("ProjectNameTest");
        projectDTO.setDescription("ProjectDescriptionTest");
        projectDTO.setDateCreate(new Date());
        projectDTO.setDateStart(new Date());
        projectDTO.setDateFinish(new Date());
        projectDTO.setStatus(Status.INPROGRESS);
        projectDTO.setUserId(testUserId);

        @NotNull final String urlCreate = "http://localhost:9999/rest/projectCreate/";

        headers.addAll(HttpHeaders.COOKIE, listCookie);
        headers.setContentType(MediaType.APPLICATION_JSON);
        @NotNull HttpEntity entity = new HttpEntity<>(projectDTO, headers);
        template.exchange(urlCreate, HttpMethod.POST, entity, Boolean.class);

        @NotNull final String urlFind = "http://localhost:9999/rest/findProject/" + projectDTO.getId();

        entity = new HttpEntity<String>(headers);
        @NotNull final ProjectDTO projectDTOfromServer = Objects.requireNonNull(template.exchange(urlFind, HttpMethod.GET, entity, ProjectDTO.class).getBody());

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
        @NotNull final RestTemplate template = new RestTemplate();
        @NotNull HttpHeaders headers = new HttpHeaders();
        @NotNull HttpEntity entity;

        @NotNull final ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(UUID.randomUUID().toString());
        projectDTO.setName("ProjectNameTest");
        projectDTO.setDescription("ProjectDescriptionTest");
        projectDTO.setDateCreate(new Date());
        projectDTO.setDateStart(new Date());
        projectDTO.setDateFinish(new Date());
        projectDTO.setStatus(Status.INPROGRESS);
        projectDTO.setUserId(testUserId);

        @NotNull final String urlCreate = "http://localhost:9999/rest/projectCreate/";

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.addAll(HttpHeaders.COOKIE, listCookie);
        entity = new HttpEntity<>(projectDTO, headers);
        template.exchange(urlCreate, HttpMethod.POST, entity, Boolean.class);

        @NotNull final ProjectDTO projectDTO2 = new ProjectDTO();
        projectDTO2.setId(UUID.randomUUID().toString());
        projectDTO2.setName("ProjectNameTest2");
        projectDTO2.setDescription("ProjectDescriptionTest2");
        projectDTO2.setDateCreate(new Date());
        projectDTO2.setDateStart(new Date());
        projectDTO2.setDateFinish(new Date());
        projectDTO2.setStatus(Status.INPROGRESS);
        projectDTO2.setUserId(testUserId);

        entity = new HttpEntity<>(projectDTO2, headers);
        template.exchange(urlCreate, HttpMethod.POST, entity, Boolean.class);

        @NotNull final String urlFindAll = "http://localhost:9999/rest/findAllProject";

        entity = new HttpEntity<String>(headers);
        @NotNull final ResponseEntity<Collection<ProjectDTO>> response = template.exchange(urlFindAll, HttpMethod.GET, entity, new ParameterizedTypeReference<Collection<ProjectDTO>>() {
        });
        final Collection<ProjectDTO> collection = response.getBody();

        Assert.assertEquals(collection.size(), 2);

        @NotNull String urlFind = "http://localhost:9999/rest/findProject/" + projectDTO.getId();

        entity = new HttpEntity<String>(headers);
        @NotNull ProjectDTO projectDTOfromServer = Objects.requireNonNull(template.exchange(urlFind, HttpMethod.GET, entity, ProjectDTO.class).getBody());

        Assert.assertEquals(projectDTOfromServer.getId(), projectDTO.getId());
        Assert.assertEquals(projectDTOfromServer.getName(), projectDTO.getName());
        Assert.assertEquals(projectDTOfromServer.getDescription(), projectDTO.getDescription());
        Assert.assertEquals(ConverterUtil.convertDateFormat(projectDTOfromServer.getDateStart()), ConverterUtil.convertDateFormat(projectDTO.getDateStart()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(projectDTOfromServer.getDateFinish()), ConverterUtil.convertDateFormat(projectDTO.getDateFinish()));
        Assert.assertEquals(ConverterUtil.convertDateFormat(projectDTOfromServer.getDateCreate()), ConverterUtil.convertDateFormat(projectDTO.getDateCreate()));
        Assert.assertEquals(projectDTOfromServer.getStatus(), projectDTO.getStatus());
        Assert.assertEquals(projectDTOfromServer.getUserId(), projectDTO.getUserId());

        urlFind = "http://localhost:9999/rest/findProject/" + projectDTO2.getId();
        projectDTOfromServer = Objects.requireNonNull(template.exchange(urlFind, HttpMethod.GET, entity, ProjectDTO.class).getBody());

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
        @NotNull final RestTemplate template = new RestTemplate();
        @NotNull HttpHeaders headers = new HttpHeaders();
        @NotNull HttpEntity entity;

        @NotNull final ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(UUID.randomUUID().toString());
        projectDTO.setName("ProjectNameTest");
        projectDTO.setDescription("ProjectDescriptionTest");
        projectDTO.setDateCreate(new Date());
        projectDTO.setDateStart(new Date());
        projectDTO.setDateFinish(new Date());
        projectDTO.setStatus(Status.INPROGRESS);
        projectDTO.setUserId(testUserId);

        @NotNull final String urlCreate = "http://localhost:9999/rest/projectCreate/";

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.addAll(HttpHeaders.COOKIE, listCookie);
        entity = new HttpEntity<>(projectDTO, headers);
        template.exchange(urlCreate, HttpMethod.POST, entity, Boolean.class);

        @NotNull final ProjectDTO editProjectDTO = projectDTO;
        editProjectDTO.setName("ProjectEditNameTest");
        editProjectDTO.setDescription("ProjectEditDescriptionTest");
        editProjectDTO.setDateStart(new Date());
        editProjectDTO.setDateFinish(new Date());
        editProjectDTO.setStatus(Status.DONE);

        @NotNull final String urlEdit = "http://localhost:9999/rest/projectEdit/";

        entity = new HttpEntity<>(editProjectDTO, headers);
        template.exchange(urlEdit, HttpMethod.PUT, entity, Boolean.class);

        @NotNull String urlFind = "http://localhost:9999/rest/findProject/" + projectDTO.getId();

        entity = new HttpEntity<String>(headers);
        @NotNull ProjectDTO projectDTOfromServer = Objects.requireNonNull(template.exchange(urlFind, HttpMethod.GET, entity, ProjectDTO.class).getBody());

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
        @NotNull final RestTemplate template = new RestTemplate();
        @NotNull HttpHeaders headers = new HttpHeaders();
        @NotNull HttpEntity entity;

        @NotNull final ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(UUID.randomUUID().toString());
        projectDTO.setName("ProjectNameTest");
        projectDTO.setDescription("ProjectDescriptionTest");
        projectDTO.setDateCreate(new Date());
        projectDTO.setDateStart(new Date());
        projectDTO.setDateFinish(new Date());
        projectDTO.setStatus(Status.INPROGRESS);
        projectDTO.setUserId(testUserId);

        @NotNull final String urlCreate = "http://localhost:9999/rest/projectCreate/";

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.addAll(HttpHeaders.COOKIE, listCookie);
        entity = new HttpEntity<>(projectDTO, headers);
        template.exchange(urlCreate, HttpMethod.POST, entity, Boolean.class);

        @NotNull String urlDelete = "http://localhost:9999/rest/projectDelete/" + projectDTO.getId();

        entity = new HttpEntity<String>(headers);
        template.exchange(urlDelete, HttpMethod.DELETE, entity, Boolean.class).getBody();

        @NotNull final String urlFindAll = "http://localhost:9999/rest/findAllProject";

        entity = new HttpEntity<String>(headers);
        @NotNull final ResponseEntity<Collection<ProjectDTO>> response = template.exchange(urlFindAll, HttpMethod.GET, entity, new ParameterizedTypeReference<Collection<ProjectDTO>>() {
        });
        Collection<ProjectDTO> collection = response.getBody();

        Assert.assertEquals(collection.size(), 0);
    }

    @After
    public void deleteTestUser() {
        @NotNull final String url = "http://localhost:9999/rest/userDelete/" + testUserId;
        @NotNull final RestTemplate template = new RestTemplate();
        template.exchange(url, HttpMethod.DELETE, null, Boolean.class);
    }
}