package co.allconnected.fussiontech.usersservice.controllers;

import co.allconnected.fussiontech.usersservice.model.Rol;
import co.allconnected.fussiontech.usersservice.services.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RolesController {

    private final RolService rolService;

    @Autowired
    public RolesController(RolService rolService) {
        this.rolService = rolService;
    }

    @PostMapping
    public ResponseEntity<Rol> createRol(@RequestBody Rol rol) {
        return ResponseEntity.ok(rolService.createRol(rol));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> getRol(@PathVariable String id) {
        return rolService.getRol(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Rol>> getAllRoles() {
        return ResponseEntity.ok(rolService.getAllRoles());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> updateRol(@PathVariable String id, @RequestBody Rol rol) {
        return ResponseEntity.ok(rolService.updateRol(rol));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRol(@PathVariable String id) {
        rolService.deleteRol(id);
        return ResponseEntity.noContent().build();
    }
}