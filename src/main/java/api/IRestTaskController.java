package api;

import dto.TaskDTO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

public interface IRestTaskController extends IRestController {

    @PostMapping(value = "/taskCreate", produces = MediaType.APPLICATION_JSON_VALUE)
    Boolean taskCreate(@RequestBody @NotNull final TaskDTO taskDTO);

    @Nullable
    @GetMapping(value = "/findTask/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    TaskDTO findTask(@PathVariable(value = "id") @NotNull final String id);

    @NotNull
    @GetMapping(value = "/findAllTask", produces = MediaType.APPLICATION_JSON_VALUE)
    Collection<TaskDTO> findAllTask();

    @PutMapping(value = "/taskEdit", produces = MediaType.APPLICATION_JSON_VALUE)
    Boolean taskEdit(@RequestBody @NotNull final TaskDTO taskDTO);

    @DeleteMapping(value = "/taskDelete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Boolean taskDelete(@PathVariable(value = "id") @NotNull final String id);
}