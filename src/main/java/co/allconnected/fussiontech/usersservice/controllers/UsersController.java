package co.allconnected.fussiontech.usersservice.controllers;

import co.allconnected.fussiontech.usersservice.dtos.*;
import co.allconnected.fussiontech.usersservice.services.UserService;
import co.allconnected.fussiontech.usersservice.utils.OperationException;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(
            @ModelAttribute UserCreateDTO user,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {
        try {
            UserDTO userDTO = userService.createUser(user, photo);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (OperationException e) {
            return ResponseEntity.status(e.getCode()).body(new Response(e.getCode(), e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @PostMapping("/guest")
    public  ResponseEntity<?> createGuestUser() {
        try {
            UserDTO userDTO = userService.createGuestUser();
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (OperationException e) {
            return ResponseEntity.status(e.getCode()).body(new Response(e.getCode(), e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(value = "fullname", required = false) String fullname,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "mail", required = false) String mail,
            @RequestParam(value = "rol", required = false) String rol,
            @RequestParam(value = "active", required = false) Boolean active
    ) {
        try {
            UserDTO[] listUsersDTO = userService.getUsers(fullname, username, mail, rol, active);
            if (listUsersDTO.length == 0)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(HttpStatus.NOT_FOUND.value(), "No users found"));
            return ResponseEntity.status(HttpStatus.OK).body(listUsersDTO);
        } catch (OperationException e) {
            return ResponseEntity.status(e.getCode()).body(new Response(e.getCode(), e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(id));
        } catch (OperationException e) {
            return ResponseEntity.status(e.getCode()).body(new Response(e.getCode(), e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable String id,
            @ModelAttribute UserCreateDTO user,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {
        try {
            UserDTO userDTO = userService.updateUser(id, user, photo);
            return ResponseEntity.status(HttpStatus.OK).body(userDTO);
        } catch (OperationException e) {
            return ResponseEntity.status(e.getCode()).body(new Response(e.getCode(), e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (OperationException e) {
            return ResponseEntity.status(e.getCode()).body(new Response(e.getCode(), e.getMessage()));
        }
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable String id, @RequestBody DeleteRequestDTO deleteRequest) {
        try {
            DeletedDTO deletedDTO = userService.deactivateUser(id, deleteRequest.delete_reason());
            return ResponseEntity.status(HttpStatus.OK).body(deletedDTO);
        } catch (OperationException e) {
            return ResponseEntity.status(e.getCode()).body(new Response(e.getCode(), e.getMessage()));
        }
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<?> addRole(@PathVariable String id, @RequestBody RolesDTO roles) {
        try {
            UserDTO userDTO = userService.addRoles(id, roles.roles());
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (OperationException e) {
            return ResponseEntity.status(e.getCode()).body(new Response(e.getCode(), e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/roles/{rol}")
    public ResponseEntity<?> removeRole(@PathVariable String id, @PathVariable String rol) {
        try {
            UserDTO userDTO = userService.removeRoles(id, rol);
            return ResponseEntity.status(HttpStatus.OK).body(userDTO);
        } catch (OperationException e) {
            return ResponseEntity.status(e.getCode()).body(new Response(e.getCode(), e.getMessage()));
        }
    }
}