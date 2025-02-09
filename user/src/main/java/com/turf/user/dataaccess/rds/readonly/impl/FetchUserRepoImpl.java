package com.turf.user.dataaccess.rds.readonly.impl;

import com.turf.user.dataaccess.rds.readonly.FetchUserRepo;
import com.turf.user.dataaccess.rds.repository.UserRepository;
import com.turf.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FetchUserRepoImpl implements FetchUserRepo {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> fetchAllUserExcept(String email) {
        return userRepository.findByEmailNotAndDeletedFalse(email);
    }
}
