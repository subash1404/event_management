package com.example.eventmanagement.response;

import com.example.eventmanagement.enums.Role;
import com.example.eventmanagement.models.College;
import com.example.eventmanagement.models.Feedback;
import com.example.eventmanagement.models.Registration;
import com.example.eventmanagement.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserResponse {
    private String name;
    private String email;
    private String collegeName;
    private String collegeLocation;
    private Role role;

    public UserResponse(User user) {
        this.setEmail(user.getEmail());
        this.setName(user.getName());
        this.setRole(user.getRole());
        System.out.println(user.getRole().toString());
        System.out.println(user.toString());
        this.setCollegeName(user.getRole() == Role.Student ? user.getStudent().getCollege().getName() : user.getAdmin().getCollege().getName());
        this.setCollegeLocation(user.getRole() == Role.Student ? user.getStudent().getCollege().getLocation() : user.getAdmin().getCollege().getLocation());
    }
}
