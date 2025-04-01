package com.siyeonson.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResult> getUser(
            @PathVariable Long id
    ) {
        var response = userService.readOne(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users")
    public ResponseEntity<UserResult> create(
            @RequestBody UserDto userDto
    ) {
        var response = userService.create(userDto);
        return ResponseEntity.ok(response);
    }

}
