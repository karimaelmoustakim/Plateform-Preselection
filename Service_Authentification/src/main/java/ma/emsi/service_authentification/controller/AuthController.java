package ma.emsi.service_authentification.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import ma.emsi.service_authentification.Service.UserService;
import ma.emsi.service_authentification.model.User;
import ma.emsi.service_authentification.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password, HttpServletResponse response) {
        try {
            User authenticatedUser = userService.authenticate(email, password);

            // Générer un cookie HTTP avec l'ID ou un identifiant de session
            Cookie cookie = new Cookie("SESSIONID", authenticatedUser.getId().toString());
            cookie.setHttpOnly(true); // Empêcher l'accès JavaScript
            cookie.setSecure(false); // Mettre true si vous utilisez HTTPS en production
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60); // 1 jour en secondes

            // Ajouter le cookie à la réponse
            response.addCookie(cookie);

            return ResponseEntity.ok(authenticatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

//
//    // CREATE
//    @PostMapping("/add")
//    public ResponseEntity<User> createUser(@RequestBody User user) {
//        User newUser = userService.createUser(user);
//        return ResponseEntity.ok(newUser);
//    }

    // READ (Tous les utilisateurs)
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // READ (Un utilisateur par ID)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable Long id) {
        boolean exists = userService.doesUserExist(id);
        return ResponseEntity.ok(exists);
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<Long> getUserIdByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email); // Méthode à implémenter dans UserService
        if (user != null) {
            return ResponseEntity.ok(user.getId()); // Retourne l'ID de l'utilisateur
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
