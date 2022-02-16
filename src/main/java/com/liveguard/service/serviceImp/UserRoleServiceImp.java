package com.liveguard.service.serviceImp;

import com.liveguard.domain.Role;
import com.liveguard.repository.UserRoleRepository;
import com.liveguard.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserRoleServiceImp implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImp(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Optional<Role> findById(Long id) {
        return userRoleRepository.findById(id);
    }
}
