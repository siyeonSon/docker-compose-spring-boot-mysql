package com.siyeonson.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserResult create(UserDto userDto) {
        User user = User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .build();
        User saved = userRepository.save(user);
        return new UserResult(saved);
    }

    public UserResult readOne(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new UserResult(user);
    }

}
