package ru.demo.user.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.demo.user.UserRepository;
import ru.demo.user.UserService;
import ru.demo.user.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMappers userMapper;

    @Override
    public void createVerify(UserCreate model) {
        if (userRepository.existsByUsername(model.getEmail()))
            throw new UserException.AlreadyExists();
    }

    @Override
    public Page<UserShort> searchBy(UserSearch search, UUID userId, Pageable page) {
        return userRepository.findBy(Specification.allOf(
                (r, cq, cb) -> StringUtils.hasText(search.getFirstName())
                        ? cb.like(cb.lower(r.get(User_.FIRST_NAME)), '%' + search.getFirstName().toLowerCase() + '%')
                        : null
        ), query -> query.page(page).map(userMapper::toShort));
    }

    @Override
    public UserDetail getById(UUID id) {
        return userRepository.findById(id).map(userMapper::toDetail).orElseThrow(UserException.NotFound::new);
    }

    @Override
    public void userUpdate(UUID userId, UserModify.UserUpdate request) {

        var result = userRepository.update((rt, cu, cb) -> {

            cu.set(rt.get(User_.FIRST_NAME), request.getFirstname());
            cu.set(rt.get(User_.MODIFIED), LocalDateTime.now());

            return cb.and(cb.equal(rt.get(User_.ID), userId));
        });

        if (result != 1) throw new UserException.NotFound();
    }

    @Override
    public void deleteUser(UUID userId, boolean isAuth) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new UserException.NotFound();
        });
    }
}
