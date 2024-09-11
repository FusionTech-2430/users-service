package co.allconnected.fussiontech.usersservice.services;

import co.allconnected.fussiontech.usersservice.dtos.UserCreateDTO;
import co.allconnected.fussiontech.usersservice.model.User;
import co.allconnected.fussiontech.usersservice.repository.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FirebaseService firebaseService;

    @Autowired
    public UserService(UserRepository userRepository, FirebaseService firebaseService) {
        this.userRepository = userRepository;
        this.firebaseService = firebaseService;
    }

    public User createUser(UserCreateDTO userDto) {
        User user = new User(userDto);
        user.setIdUser(UUID.randomUUID().toString().substring(0, 28));


        String photoName = userDto.getMail().replace(".","_");
        String extension = FilenameUtils.getExtension(userDto.getPhoto_url().getOriginalFilename());

        try {
            firebaseService.upload(photoName, extension, userDto.getPhoto_url());
        } catch(IOException e){
            System.out.println(e.getMessage());
        }

        return userRepository.save(user);
    }
}