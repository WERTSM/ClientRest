import api.IRestUserController;
import dto.Account;
import dto.UserDTO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.*;
import util.FeingUtil;

import java.util.Objects;

public class RestFeignUserTest {

    private static IRestUserController restUserController;

    private String testUserDTOId;

    @BeforeClass
    public static void getInstance() {
        restUserController = FeingUtil.getInstance(IRestUserController.class);
    }

    @Before
    public void registrationAndLoginTestUserAndCreateProject() {
        @NotNull final String login = "test";
        @NotNull final String password = "test";
        @NotNull final Account account = new Account(login, password);
        testUserDTOId = restUserController.registration(account);
    }

    @Test
    public void registrationAndFindUserTest() {
        @NotNull final UserDTO userDTOfromServer = Objects.requireNonNull(restUserController.findUser(testUserDTOId));

        Assert.assertEquals(userDTOfromServer.getId(), testUserDTOId);
    }

    @Test
    public void editUserTest() {
        @NotNull final UserDTO userDTO = Objects.requireNonNull(restUserController.findUser(testUserDTOId));

        userDTO.setLogin("testEdit");

        restUserController.userEdit(userDTO);

        @NotNull final UserDTO userDTOfromServer = Objects.requireNonNull(restUserController.findUser(userDTO.getId()));

        Assert.assertEquals(userDTOfromServer.getId(), userDTO.getId());
        Assert.assertEquals(userDTOfromServer.getLogin(), userDTO.getLogin());
        Assert.assertEquals(userDTOfromServer.getHashPassword(), userDTO.getHashPassword());
        Assert.assertEquals(userDTOfromServer.getRole(), userDTO.getRole());
    }

    @Test
    public void removeUserTest() {
        @NotNull final String login = "test2";
        @NotNull final String password = "test2";
        @NotNull final Account account = new Account(login, password);

        @NotNull final String userDTOId2 = Objects.requireNonNull(restUserController.registration(account));

        restUserController.removeUser(userDTOId2);

        @Nullable final UserDTO userDTOfromServer = restUserController.findUser(userDTOId2);

        Assert.assertNull(userDTOfromServer);
    }

    @After
    public void deleteTestUser() {
        restUserController.removeUser(testUserDTOId);
    }
}