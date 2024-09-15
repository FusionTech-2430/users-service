package co.allconnected.fussiontech.usersservice.services;

import co.allconnected.fussiontech.usersservice.dtos.DeletedDTO;
import co.allconnected.fussiontech.usersservice.dtos.InactiveUserDTO;
import co.allconnected.fussiontech.usersservice.dtos.UserCreateDTO;
import co.allconnected.fussiontech.usersservice.dtos.UserDTO;
import co.allconnected.fussiontech.usersservice.model.Deleted;
import co.allconnected.fussiontech.usersservice.model.Rol;
import co.allconnected.fussiontech.usersservice.model.User;
import co.allconnected.fussiontech.usersservice.repository.DeletedRepository;
import co.allconnected.fussiontech.usersservice.repository.UserRepository;
import co.allconnected.fussiontech.usersservice.utils.OperationException;
import com.google.firebase.auth.FirebaseAuthException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DeletedRepository deletedRepository;
    private final RolService rolService;
    private final FirebaseService firebaseService;

    @Autowired
    public UserService(UserRepository userRepository, DeletedRepository deletedRepository, RolService rolService, FirebaseService firebaseService) {
        this.userRepository = userRepository;
        this.firebaseService = firebaseService;
        this.rolService = rolService;
        this.deletedRepository = deletedRepository;
    }

    public UserDTO createUser(UserCreateDTO userDto, MultipartFile photo) throws FirebaseAuthException, IOException {
        User user = new User(userDto);

        // Create user in firebase
        user.setIdUser(firebaseService.createUser(userDto.mail(), userDto.password()));

        // Add roles to user
        for (String rol : userDto.roles()) {
            Rol rolEntity = rolService.getRol(rol).orElseThrow();
            user.getRoles().add(rolEntity);
        }

        // Upload photo to firebase
        if (photo != null) {
            String photoName = user.getIdUser();
            String extension = FilenameUtils.getExtension(photo.getOriginalFilename());
            user.setPhotoUrl(firebaseService.uploadImg(photoName, extension, photo));
        }

        return new UserDTO(userRepository.save(user));
    }

    public UserDTO createGuestUser(){
        User user = new User();
        user.setIdUser(UUID.randomUUID().toString().replace("-", "").substring(0, 28));

        user.setUsername("Guest<" + user.getIdUser() + ">");
        user.setFullname("Guest");
        user.setActive(true);

        Rol rolEntity = rolService.getRol("guest").orElseThrow();
        user.getRoles().add(rolEntity);

        return new UserDTO(userRepository.save(user));
    }

    public void deleteUser(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPhotoUrl() != null)
                firebaseService.deleteImg(user.getIdUser());
            // check if user is guest
            if (user.getRoles().stream().noneMatch(rol -> rol.getIdRol().equals("guest"))) try {
                firebaseService.deleteUser(user.getIdUser());
            } catch (FirebaseAuthException e) {
                throw new OperationException(500, "Firebase authentication error: " + e.getMessage());
            }
            userRepository.delete(user);
        } else {
            throw new OperationException(404, "User not found");
        }
    }

    public DeletedDTO deactivateUser(String id, String reason) {
        return userRepository.findById(id).map(user -> {
            Deleted deleted = new Deleted(user.getIdUser(), reason);
            user.setActive(false);
            user.setDeleted(deletedRepository.save(deleted));
            userRepository.save(user);
            try {
                firebaseService.disableUser(user.getIdUser());
            } catch (FirebaseAuthException e) {
                throw new OperationException(500, e.getMessage());
            }
            return new DeletedDTO(deleted.getIdUser(), deleted.getReason(), deleted.getDeleteDate());
        }).orElseThrow(() -> new OperationException(404, "User not found"));
    }

    public UserDTO[] getUsers(String fullname, String username, String mail, String rol, Boolean active) {
        return userRepository.findUsersByFilters(fullname, username, mail, rol, active).stream()
                .map(user -> user.getActive() ? new UserDTO(user) : new InactiveUserDTO(user))
                .toArray(UserDTO[]::new);
    }

    public UserDTO getUser(String id) {
        return userRepository.findById(id)
                .map(user -> user.getActive() ? new UserDTO(user) : new InactiveUserDTO(user))
                .orElseThrow(() -> new OperationException(404, "User not found"));
    }
}