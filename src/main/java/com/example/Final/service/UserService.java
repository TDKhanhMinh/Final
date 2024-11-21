package com.example.Final.service;

import com.example.Final.entity.listingservice.Images;
import com.example.Final.entity.securityservice.Roles;
import com.example.Final.entity.securityservice.User;
import com.example.Final.repository.RolesRepository;
import com.example.Final.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RolesRepository rolesRepository;

    public User create(User user) {
        User saveUser = new User();
        saveUser.setEmail(user.getEmail());
        saveUser.setPassword(passwordEncoder.encode(user.getPassword()));
        saveUser.setFullName(user.getFullName());
        saveUser.setConfirmPassword(user.getConfirmPassword());
        saveUser.setRoles(Collections.singletonList(rolesRepository.findRolesByName("ROLE_REALTOR")));
        return userRepository.save(saveUser);
    }

    public User update(User user) {
        User oldUser = userRepository.findById(user.getUserId()).orElseThrow();
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        oldUser.setConfirmPassword(user.getConfirmPassword());
        oldUser.setFullName(user.getFullName());
        oldUser.setRoles(oldUser.getRoles());
        return userRepository.save(oldUser);
    }

    public void updateByEmail(String email, String password, String confirmPassword) {
        User oldUser = userRepository.findUserByEmail(email);
        oldUser.setEmail(email);
        oldUser.setPassword(passwordEncoder.encode(password));
        oldUser.setConfirmPassword(confirmPassword);
        oldUser.setRoles(oldUser.getRoles());
        userRepository.save(oldUser);
    }

    public void updateImage(User user, Images image) {
        User oldUser = userRepository.findUserByEmail(user.getEmail());
        oldUser.setImages(image);
        userRepository.save(oldUser);
    }

    public void updateInfo(String email, String fullName, String phone) {
        User oldUser = userRepository.findUserByEmail(email);
        oldUser.setEmail(email);
        oldUser.setPhone(phone);
        oldUser.setFullName(fullName);
        userRepository.save(oldUser);
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Couldn't find user'"));
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(user.getRoles());

        return new org.springframework.security.core.userdetails
                .User(user.getEmail(), user.getPassword(), authorities);
    }

    private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Roles> roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Roles tempRole : roles) {
            SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority(tempRole.getName());
            authorities.add(tempAuthority);
        }
        return authorities;
    }
}
