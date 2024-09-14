package co.allconnected.fussiontech.usersservice.services;

import co.allconnected.fussiontech.usersservice.dtos.DeletedDTO;
import co.allconnected.fussiontech.usersservice.dtos.UserCreateDTO;
import co.allconnected.fussiontech.usersservice.model.Deleted;
import co.allconnected.fussiontech.usersservice.model.Rol;
import co.allconnected.fussiontech.usersservice.model.User;
import co.allconnected.fussiontech.usersservice.repository.DeletedRepository;
import co.allconnected.fussiontech.usersservice.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuthException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    public User createUser(UserCreateDTO userDto, MultipartFile photo) throws FirebaseAuthException, IOException {
        User user = new User(userDto);

        // Create user in firebase
        user.setIdUser(firebaseService.createUser(userDto.getMail(), userDto.getPassword()));

        // Add roles to user
        for(String rol : userDto.getRoles()){
            Rol rolEntity = rolService.getRol(rol).orElseThrow();
            user.getRoles().add(rolEntity);
        }

        // Upload photo to firebase
        if(photo != null) {
            String photoName = user.getIdUser();
            String extension = FilenameUtils.getExtension(photo.getOriginalFilename());
            user.setPhotoUrl(firebaseService.uploadImg(photoName, extension, photo));
        }

        return userRepository.save(user);
    }

    public void deleteUser(String id) throws RuntimeException {
        userRepository.findById(id).ifPresent(user -> {
            if(user.getPhotoUrl() != null)
                firebaseService.deleteImg(user.getIdUser());
            try {
                firebaseService.deleteUser(user.getIdUser());
            } catch (FirebaseAuthException e) {
                throw new RuntimeException("Firebase authentication error: " + e.getMessage());
            }
            userRepository.delete(user);
        });

        userRepository.deleteById(id);
    }

    public DeletedDTO deactivateUser(String id, String reason) throws RuntimeException {
        return userRepository.findById(id).map(user -> {
            Deleted deleted = new Deleted(user.getIdUser(), reason);
            user.setActive(false);
            user.setDeleted(deletedRepository.save(deleted));
            userRepository.save(user);
            try {
                firebaseService.disableUser(user.getIdUser());
            } catch (FirebaseAuthException e) {
                throw new RuntimeException(e);
            }
            return new DeletedDTO(deleted.getIdUser(), deleted.getReason(), deleted.getDeleteDate());
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }
}