package co.allconnected.fussiontech.usersservice.services;

import co.allconnected.fussiontech.usersservice.dtos.UserCreateDTO;
import co.allconnected.fussiontech.usersservice.model.User;
import co.allconnected.fussiontech.usersservice.model.Deleted;
import co.allconnected.fussiontech.usersservice.repository.UserRepository;
import co.allconnected.fussiontech.usersservice.repository.DeletedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    // private final DeletedRepository deletedRepository;

    @Autowired
    public UserService(UserRepository userRepository, DeletedRepository deletedRepository) {
        this.userRepository = userRepository;
        // this.deletedRepository = deletedRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(UserCreateDTO userDto) {
        User user = new User();
        user.setIdUser(UUID.randomUUID().toString().substring(0, 28));
        user.setUsername(userDto.getUsername());
        user.setMail(userDto.getMail());
        user.setFullname(userDto.getFullname());
        user.setPhotoUrl(userDto.getPhotoUrl());
        user.setLocationLat(userDto.getLocationLat());
        user.setLocationLng(userDto.getLocationLng());
        user.setActive(userDto.getActive());
        return userRepository.save(user);
    }
/*
    public User updateUser(String id, User user) {
        User existingUser = getUserById(id);
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            // Add other fields to be updated
            return userRepository.save(existingUser);
        }
        return null;
    }


    public void deleteUser(String id) {
        User user = getUserById(id);
        if (user != null) {
            Deleted deleted = new Deleted();
            deleted.setId(user.getId());
            deleted.setName(user.getName());
            deleted.setEmail(user.getEmail());
            // Add other fields to be transferred
            deletedRepository.save(deleted);
            userRepository.deleteById(id);
        }
    }
    */
}