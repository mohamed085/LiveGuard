package com.liveguard.service;

import com.liveguard.domain.Role;

import java.util.Optional;

public interface UserRoleService {

    Optional<Role> findById (Long id);
}
