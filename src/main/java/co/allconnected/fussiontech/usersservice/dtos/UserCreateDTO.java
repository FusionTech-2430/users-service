package co.allconnected.fussiontech.usersservice.dtos;

public record UserCreateDTO(String fullname, String username, String password, String mail, String[] roles) {
}