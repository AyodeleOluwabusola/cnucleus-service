package com.coronation.nucleus.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author toyewole
 */
@Getter
@Setter
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;

}
