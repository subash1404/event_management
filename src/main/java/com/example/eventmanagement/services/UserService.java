package com.example.eventmanagement.services;

import com.example.eventmanagement.enums.Role;
import com.example.eventmanagement.models.Admin;
import com.example.eventmanagement.models.College;
import com.example.eventmanagement.models.Student;
import com.example.eventmanagement.models.User;
import com.example.eventmanagement.repositories.AdminRepository;
import com.example.eventmanagement.repositories.CollegeRepository;
import com.example.eventmanagement.repositories.StudentRepository;
import com.example.eventmanagement.repositories.UserRepository;
import com.example.eventmanagement.request.UpdateUserRequest;
import com.example.eventmanagement.response.UserResponse;
import com.example.eventmanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @PreAuthorize("hasAnyRole('Admin','Student')")
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('Admin','Student')")
    public User findUserById(Long id){
        return userRepository.findById(id).get();
    }


    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @PreAuthorize("hasAnyRole('Admin','Student')")
    public UserResponse updateUser(UpdateUserRequest updateUserRequest){
        User existingUser = userRepository.findByEmail(updateUserRequest.getEmail()).orElseThrow(() -> new RuntimeException("User Not Found"));
        existingUser.setName(updateUserRequest.getName());
        User updatedUser = userRepository.save(existingUser);
        College college = collegeRepository.findById(updateUserRequest.getCollege_id()).orElseThrow(() -> new RuntimeException("College Not Found"));

        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(updatedUser.getEmail());
        userResponse.setRole(updatedUser.getRole());
        userResponse.setCollegeName(college.getName());
        userResponse.setCollegeName(college.getLocation());
        userResponse.setName(updatedUser.getName());

        if(existingUser.getRole() == Role.Student){
            Student student = studentRepository.findById(updatedUser.getId()).get();
            student.setCollege(college);
            studentRepository.save(student);
        }else{
            Admin admin = adminRepository.findById(updatedUser.getId()).get();
            admin.setCollege(college);
            adminRepository.save(admin);
        }
        return userResponse;
    }

    @PreAuthorize("hasAnyRole('Admin','Student')")
    public String deleteUser(Long id){
        studentRepository.deleteById(id);
        userRepository.deleteById(id);
        return "User with ID: "+id+" deleted successfully";
    }
}
