package enumeration;

public enum Role {

    ADMIN("ADMIN"), USER("USER");

    String displayName;

    Role(final String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}