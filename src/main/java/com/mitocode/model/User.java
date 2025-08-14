package com.mitocode.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "usuarios")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private boolean enabled;
    private List<Role> roles;
}
