package com.sungur.bookstore.service;


import com.sungur.bookstore.model.Role;
import com.sungur.bookstore.model.User;
import com.sungur.bookstore.repository.RoleRepository;
import com.sungur.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository,
                       @Qualifier("roleRepository")   RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    public User findUserByUserName(String userName) {

        return userRepository.findByUserName(userName);
    }

    public User saveUser(User user) throws Exception {
        if (!fullEmptyCheck(user)) {
            throw new Exception("Kullanıcı degerleri boş girilmez!.");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    private boolean fullEmptyCheck(User user) {
        if (user.getUserName().isEmpty() && user.getEmail().isEmpty() &&
                user.getPassword().isEmpty()) {
            return false;
        }
        return true;
    }
}
