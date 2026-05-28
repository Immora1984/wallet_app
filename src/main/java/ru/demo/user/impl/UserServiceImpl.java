package ru.demo.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.demo.user.UserRepository;
import ru.demo.user.UserService;
import ru.demo.user.impl.jpa.User;
import ru.demo.user.impl.jpa.User_;
import ru.demo.user.model.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UserMappers userMapper;

    @Override
    public UserDetail getById(UUID id) {
        return userRepository.findById(id).map(userMapper::toDetail).orElseThrow(UserException.NotFound::new);
    }

    @Override
    public void userUpdate(UUID userId, UserModify.UserUpdate request) {

        var result = userRepository.update((rt, cu, cb) -> {

            cu.set(rt.get(User_.MODIFIED), LocalDateTime.now());

            return cb.and(cb.equal(rt.get(User_.ID), userId));
        });

        if (result != 1) throw new UserException.NotFound();
    }

    @Override
    public void registerUser(UserCreate userCreate) {
        if (userRepository.existsByUsername(userCreate.getUsername())) throw new UserException.AlreadyExists();
        userRepository.save(userMapper.fromCreate(userCreate));
    }

    @Override
    public User importOAuth2(OAuth2User oAuth2User) {
        var user = userRepository.findByEmail(oAuth2User.getName()).orElseGet(User::new);
        userMapper.update(user, oAuth2User, true);
        return userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteUser(UUID userId, boolean isAuth) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new UserException.NotFound();
        });
    }
}
