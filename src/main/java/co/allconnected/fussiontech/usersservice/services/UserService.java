package co.allconnected.fussiontech.usersservice.services;

import co.allconnected.fussiontech.usersservice.dtos.UserCreateDTO;
import co.allconnected.fussiontech.usersservice.model.Rol;
import co.allconnected.fussiontech.usersservice.model.User;
import co.allconnected.fussiontech.usersservice.repository.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

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

    public User createUser(UserCreateDTO userDto, MultipartFile photo) {
        User user = new User(userDto);

        // Generate user id (TODO: Move to Firebase)
        user.setIdUser(UUID.randomUUID().toString().substring(0, 28));

        // Add roles to user
        for(String rol : userDto.getRoles()){
            Rol rolEntity = rolService.getRol(rol).orElseThrow();
            user.getRols().add(rolEntity);
        }

        // Upload photo to firebase
        if(photo != null) try {
            String photoName = user.getIdUser();
            String extension = FilenameUtils.getExtension(photo.getOriginalFilename());
            user.setPhotoUrl(firebaseService.upload(photoName, extension, photo));
        } catch(IOException e){
            System.out.println(e.getMessage());
        }

        return userRepository.save(user);
    }
}