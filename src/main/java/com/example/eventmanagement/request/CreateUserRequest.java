package com.example.eventmanagement.request;

import com.example.eventmanagement.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
    private Long college_id;
}
