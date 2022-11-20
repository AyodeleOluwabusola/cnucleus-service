package com.coronation.nucleus.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author toyewole
 */

@Getter
@Setter
@Entity
@Table(name = "CT_USER")
public class CTUser extends BaseEntity {

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "PASSSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "CONFIRMATION_CODE")
    private String confirmationCode;

    @Column(name = "PENDING_REQUEST")
    private Long pendingRequestPk;

    @Column(name = "CONFIRMATION_TIMESTAMP")
    private LocalDateTime emailConfirmationTimestamp;


}
