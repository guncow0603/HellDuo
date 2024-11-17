package com.hellduo.global.security;

import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsImpl getUserDetails(String email) {
        User user = userRepository.findUserByEmailWithThrow(email);

        return new UserDetailsImpl(user);
    }


    public UserDetailsImpl loadUserById(Long id) {
        User user = userRepository.findUserByIdWithThrow(id);

        return new UserDetailsImpl(user);
    }


}