package api;

import dto.ProjectDTO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

public interface IRestProjectController extends IRestController {

    @PostMapping(value = "/projectCreate", produces = MediaType.APPLICATION_JSON_VALUE)
    Boolean projectCreate(@RequestBody @NotNull final ProjectDTO projectDTO);

    @Nullable
    @GetMapping(value = "/findProject/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProjectDTO findProject(@PathVariable(value = "id") @NotNull final String id);

    @NotNull
    @GetMapping(value = "/findAllProject", produces = MediaType.APPLICATION_JSON_VALUE)
    Collection<ProjectDTO> findAllProject();

    @PutMapping(value = "/projectEdit", produces = MediaType.APPLICATION_JSON_VALUE)
    Boolean projectEdit(@RequestBody @NotNull final ProjectDTO projectDTO);

    @DeleteMapping(value = "/projectDelete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Boolean projectDelete(@PathVariable(value = "id") @NotNull final String id);
}