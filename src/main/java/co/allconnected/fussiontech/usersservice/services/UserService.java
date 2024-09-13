package co.allconnected.fussiontech.usersservice.services;

import co.allconnected.fussiontech.usersservice.dtos.UserCreateDTO;
import co.allconnected.fussiontech.usersservice.model.Rol;
import co.allconnected.fussiontech.usersservice.model.User;
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
    private final RolService rolService;
    private final FirebaseService firebaseService;

    @Autowired
    public UserService(UserRepository userRepository, RolService rolService, FirebaseService firebaseService) {
        this.userRepository = userRepository;
        this.firebaseService = firebaseService;
        this.rolService = rolService;
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
            user.setPhotoUrl(firebaseService.upload(photoName, extension, photo));
        }

        return userRepository.save(user);
    }
}