package com.assingment.spectrumapplication.model;

import com.assingment.spectrumapplication.model.super_classes.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {

    @Column(nullable = false)
    private String name;

    public Role(String name) {
        this.name = name;
        setActive(true);
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
