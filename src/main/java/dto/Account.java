package dto;

public class Account {
    private String login;
    private String password;

    public String getLogin() {
        return login;
    }

    public Account(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}