package com.projects.payment_service.dtos;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class User {
    private long id;
    private String name;
    private String email;
    private String password;
    private List<Role> roles;
}
