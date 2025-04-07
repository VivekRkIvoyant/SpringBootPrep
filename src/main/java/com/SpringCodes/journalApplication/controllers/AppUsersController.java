package com.SpringCodes.journalApplication.controllers;


import com.SpringCodes.journalApplication.models.AppUser;
import com.SpringCodes.journalApplication.services.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class AppUsersController {

    private final AppUserService appUserService;


    public AppUsersController(AppUserService appUserService){
        this.appUserService = appUserService;
    }

    @GetMapping("/check")
    public String check(){
        return "User Controller working fine";
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id){
        AppUser user = appUserService.findUserById(id).orElse(null);
        if (user==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user,HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<AppUser> postUser(@RequestBody AppUser user){
        if(user==null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        appUserService.createAppUser(user);
        return new ResponseEntity<>(user,HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AppUser>> getAllUsers(){
        List<AppUser> users = appUserService.listOfAppUsers();
        if(users.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users,HttpStatus.FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppUser> deleteUsers(@PathVariable Long id){
        AppUser user = appUserService.findUserById(id).orElse(null);
        if(user==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        appUserService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    @PutMapping("/update")
    public ResponseEntity<AppUser> updateAppUser(@PathVariable Long id,@RequestBody AppUser appUser){
        Optional<AppUser> oldUser = appUserService.findUserById(id);
        if(oldUser.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AppUser old = oldUser.get();
        old.setUserName(appUser.getUserName());
        old.setPassword(appUser.getPassword());
        return new ResponseEntity<>(old,HttpStatus.CREATED);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<AppUser> findUserByName(@PathVariable String username){
        Optional<AppUser> user = Optional.ofNullable(appUserService.findUserByName(username));
        if(user.isPresent()){
            AppUser appUser = user.get();
            return new ResponseEntity<>(appUser,HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

