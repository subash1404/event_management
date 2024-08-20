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
import com.example.eventmanagement.request.CreateUserRequest;
import com.example.eventmanagement.request.LoginRequest;
import com.example.eventmanagement.response.LoginResponse;
import com.example.eventmanagement.response.UserResponse;
import com.example.eventmanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.websocket.server.ServerEndpoint;

@Service
@RequestMapping("/auth")
@RestController
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @Transactional
    @PostMapping("/register")
    public UserResponse createUser(@RequestBody CreateUserRequest createUserRequest){
        System.out.println("Inside Post");
        System.out.println(createUserRequest.getCollege_id());
        College college = collegeRepository.findById(createUserRequest.getCollege_id()).orElseThrow(() -> new RuntimeException("Colllge Not Found.Unable to save student"));
//        college = entityManager.merge(college);
        User user = new User();
        user.setName(createUserRequest.getName());
        user.setEmail(createUserRequest.getEmail());
        String encodedPassword = passwordEncoder.encode(createUserRequest.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(createUserRequest.getRole());
        User savedUser = userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setName(savedUser.getName());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setRole(savedUser.getRole());
        userResponse.setCollegeLocation(college.getLocation());
        userResponse.setCollegeName(college.getName());

        if(createUserRequest.getRole() == Role.Student){
            Student student = new Student();
            student.setUser(savedUser);
            student.setCollege(college);
            user.setStudent(student);
            studentRepository.save(student);
        }else{
            Admin admin = new Admin();
            admin.setUser(savedUser);
            admin.setCollege(college);
            user.setAdmin(admin);
            adminRepository.save(admin);
        }
        return userResponse;
    }

    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody LoginRequest loginRequest){
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        System.out.println(userDetails.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDetails, loginRequest.getPassword())
            );
        }catch (AuthenticationException e){
            throw new RuntimeException(e);
        }
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User Not Found"));
        String jwtToken = jwtUtil.generateToken(userDetails);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setSuccess(passwordEncoder.matches(loginRequest.getPassword(),user.getPassword()));
        loginResponse.setToken(jwtToken);
        return loginResponse;
    }
}
