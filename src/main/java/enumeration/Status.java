package enumeration;

import lombok.Getter;
import lombok.Setter;

public enum Status {

    PLANNED("PLANNED"), INPROGRESS("INPROGRESS"), DONE("DONE");

    @Setter
    @Getter
    private String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }
}