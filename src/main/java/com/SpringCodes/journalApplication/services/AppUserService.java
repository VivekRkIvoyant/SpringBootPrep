package com.SpringCodes.journalApplication.services;

import com.SpringCodes.journalApplication.models.AppUser;
import com.SpringCodes.journalApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {


    private final UserRepository userRepository;

    public AppUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createAppUser(AppUser user) {
        userRepository.save(user);
    }


    public List<AppUser> listOfAppUsers() {
        List<AppUser> list = userRepository.findAll();
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }


    public Optional<AppUser> findUserById(Long id) {
        AppUser user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }


    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }


    public ResponseEntity<AppUser> updateAppUser(Long id, AppUser user) {
        Optional<AppUser> appUser = userRepository.findById(id);
        if (appUser.isPresent()) {
            AppUser appUser1 = appUser.get();
            appUser1.setUserName(user.getUserName());
            appUser1.setPassword(user.getPassword());
            userRepository.save(appUser1);
            return new ResponseEntity<>(appUser1, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public AppUser findUserByName(String name){
        return userRepository.findByUserName(name);
    }
}


