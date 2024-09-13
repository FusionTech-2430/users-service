package co.allconnected.fussiontech.usersservice.controllers;

import co.allconnected.fussiontech.usersservice.dtos.UserCreateDTO;
import co.allconnected.fussiontech.usersservice.dtos.UserDTO;
import co.allconnected.fussiontech.usersservice.services.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@ModelAttribute UserCreateDTO user,
                                              @RequestParam(value = "photo", required = false) MultipartFile photo)
            throws IOException, FirebaseAuthException {
        return ResponseEntity.ok(new UserDTO(userService.createUser(user, photo)));
    }
}