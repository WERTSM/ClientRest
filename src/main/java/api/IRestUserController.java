package api;

import dto.Account;
import dto.UserDTO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


public interface IRestUserController extends IRestController {

    @NotNull
    @PostMapping(value = "/registry", produces = MediaType.APPLICATION_JSON_VALUE)
    String registration(@NotNull final Account account);

    @NotNull
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDTO login(@NotNull final Account account);

    @NotNull
    @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    Boolean logout();

    @Nullable
    @GetMapping(value = "/findUser/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDTO findUser(@PathVariable(value = "id") @NotNull final String id);

    @NotNull
    @PutMapping(value = "/userEdit", produces = MediaType.APPLICATION_JSON_VALUE)
    UserDTO userEdit(@NotNull final UserDTO userDTO);

    @DeleteMapping(value = "/userDelete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Boolean removeUser(@PathVariable(value = "id") @NotNull final String id);
}