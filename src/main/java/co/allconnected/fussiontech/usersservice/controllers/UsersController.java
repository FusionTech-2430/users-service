package co.allconnected.fussiontech.usersservice.controllers;

import co.allconnected.fussiontech.usersservice.dtos.*;
import co.allconnected.fussiontech.usersservice.services.UserService;
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
        try{
            UserDTO userDTO = new UserDTO(userService.createUser(user, photo));
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        }
        catch (FirebaseAuthException e) {
            Response response = new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Firebase authentication error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            Response response = new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable String id) {
        try{
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch (RuntimeException e) {
            Response response = new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(
            @PathVariable String id, @RequestBody DeleteRequestDTO deleteRequest) {
        try{
            DeletedDTO deletedDTO = userService.deactivateUser(id, deleteRequest.delete_reason());
            return ResponseEntity.status(HttpStatus.OK).body(deletedDTO);
        }
        catch (RuntimeException e) {
            Response response = new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }
}

