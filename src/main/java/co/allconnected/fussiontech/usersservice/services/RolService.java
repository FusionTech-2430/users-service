package co.allconnected.fussiontech.usersservice.services;

import co.allconnected.fussiontech.usersservice.model.Rol;
import co.allconnected.fussiontech.usersservice.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    private final RolRepository rolRepository;

    @Autowired
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public Rol createRol(Rol rol) {
        return rolRepository.save(rol);
    }

    public Optional<Rol> getRol(String id) {
        return rolRepository.findById(id);
    }

    public List<Rol> getAllRoles() {
        return rolRepository.findAll();
    }

    public Rol updateRol(Rol rol) {
        return rolRepository.save(rol);
    }

    public void deleteRol(String id) {
        rolRepository.deleteById(id);
    }
}