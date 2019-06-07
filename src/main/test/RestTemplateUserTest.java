import dto.Account;
import dto.UserDTO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public class RestTemplateUserTest {
    private String testUserDTOId;

    @Before
    public void registrationAndLoginTestUserAndCreateProject() {
        @NotNull final String urlRegistration = "http://localhost:9999/rest/registry/";
        @NotNull final String login = "test";
        @NotNull final String password = "test";
        @NotNull final Account account = new Account(login, password);
        @NotNull final RestTemplate template = new RestTemplate();

        @NotNull HttpEntity entity = new HttpEntity<>(account);
        testUserDTOId = template.exchange(urlRegistration, HttpMethod.POST, entity, String.class).getBody();
    }

    @Test
    public void registrationAndFindUserTest() {
        @NotNull final RestTemplate template = new RestTemplate();

        @NotNull final String urlFind = "http://localhost:9999/rest/findUser/" + testUserDTOId;

        @NotNull HttpHeaders headers = new HttpHeaders();
        @NotNull HttpEntity entity = new HttpEntity<String>(headers);

        @NotNull final UserDTO userDTOfromServer = Objects.requireNonNull(template.exchange(urlFind, HttpMethod.GET, entity, UserDTO.class).getBody());

        Assert.assertEquals(userDTOfromServer.getId(), testUserDTOId);
    }

    @Test
    public void editUserTest() {
        @NotNull final RestTemplate template = new RestTemplate();

        @NotNull final String urlFind = "http://localhost:9999/rest/findUser/" + testUserDTOId;

        @NotNull HttpHeaders headers = new HttpHeaders();
        @NotNull HttpEntity entity = new HttpEntity<String>(headers);

        @NotNull final UserDTO userDTO = Objects.requireNonNull(template.exchange(urlFind, HttpMethod.GET, entity, UserDTO.class).getBody());

        userDTO.setLogin("testEdit");

        @NotNull final String urlEdit = "http://localhost:9999/rest/userEdit/";

        entity = new HttpEntity<>(userDTO);
        template.exchange(urlEdit, HttpMethod.PUT, entity, UserDTO.class);

        entity = new HttpEntity<String>(headers);
        @NotNull final UserDTO userDTOfromServer = Objects.requireNonNull(template.exchange(urlFind, HttpMethod.GET, entity, UserDTO.class).getBody());

        Assert.assertEquals(userDTOfromServer.getId(), userDTO.getId());
        Assert.assertEquals(userDTOfromServer.getLogin(), userDTO.getLogin());
        Assert.assertEquals(userDTOfromServer.getHashPassword(), userDTO.getHashPassword());
        Assert.assertEquals(userDTOfromServer.getRole(), userDTO.getRole());
    }

    @Test
    public void removeUserTest() {
        @NotNull final String urlRegistration = "http://localhost:9999/rest/registry/";
        @NotNull final String login = "test2";
        @NotNull final String password = "test2";
        @NotNull final Account account = new Account(login, password);
        @NotNull final RestTemplate template = new RestTemplate();

        @NotNull HttpEntity entity = new HttpEntity<>(account);
        @NotNull final String UserDTOId2 = Objects.requireNonNull(template.exchange(urlRegistration, HttpMethod.POST, entity, String.class).getBody());

        @NotNull final String urlDelete = "http://localhost:9999/rest/userDelete/" + UserDTOId2;

        @NotNull HttpHeaders headers = new HttpHeaders();
        entity = new HttpEntity<String>(headers);
        template.exchange(urlDelete, HttpMethod.DELETE, entity, Boolean.class).getBody();

        @NotNull final String urlFind = "http://localhost:9999/rest/findUser/" + UserDTOId2;

        entity = new HttpEntity<String>(headers);
        @Nullable final UserDTO userDTOfromServer = template.exchange(urlFind, HttpMethod.GET, entity, UserDTO.class).getBody();

        Assert.assertNull(userDTOfromServer);
    }

    @After
    public void deleteTestUser() {
        @NotNull final String url = "http://localhost:9999/rest/userDelete/" + testUserDTOId;
        @NotNull final RestTemplate template = new RestTemplate();
        template.exchange(url, HttpMethod.DELETE, null, Boolean.class);
    }
}