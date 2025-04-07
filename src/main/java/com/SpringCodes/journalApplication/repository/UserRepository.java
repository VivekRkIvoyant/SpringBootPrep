package com.SpringCodes.journalApplication.repository;

import com.SpringCodes.journalApplication.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser,Long> {

    public AppUser findByUserName(String name);
}
