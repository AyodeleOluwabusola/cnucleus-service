package com.coronation.nucleus.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * @author toyewole
 */
@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id ;

    @CreationTimestamp
    @Column(name= "CREATE_DATE")
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name= "LAST_MODIFIED")
    private LocalDateTime lastModified;

    @Column(name= "ACTIVE")
    private Boolean active = true;

    @Column(name= "DELETED")
    private Boolean deleted = false;


    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
        this.lastModified = this.createDate;
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModified = LocalDateTime.now();
    }
}
