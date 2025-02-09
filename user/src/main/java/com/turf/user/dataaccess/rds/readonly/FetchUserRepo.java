package com.turf.user.dataaccess.rds.readonly;

import com.turf.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface FetchUserRepo {

    Optional<User> findById(Integer id);

    Optional<User> findByEmail(String email);

    List<User> fetchAllUserExcept(String email);
}
