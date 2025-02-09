package com.turf.user.dataaccess.rds.readwrite.impl;

import com.turf.user.dataaccess.rds.readwrite.PersistUserRepo;
import com.turf.user.dataaccess.rds.repository.UserRepository;
import com.turf.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PersistUserRepoImpl implements PersistUserRepo {

    private final UserRepository userRepository;

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
