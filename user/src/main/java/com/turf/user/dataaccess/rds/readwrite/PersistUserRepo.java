package com.turf.user.dataaccess.rds.readwrite;

import com.turf.user.entity.User;

public interface PersistUserRepo {

    void saveUser(User user);
}
