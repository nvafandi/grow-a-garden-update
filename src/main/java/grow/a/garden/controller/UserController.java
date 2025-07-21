package grow.a.garden.controller;

import grow.a.garden.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    ResponseEntity<Object> getUsers() {
        var response = userService.getUsers();

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

}
