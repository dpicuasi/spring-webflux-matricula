package com.mitocode.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserDTO {
    private String id;
    private String username;
    private List<String> roles;
    private boolean enabled;
}
