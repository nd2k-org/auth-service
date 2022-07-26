package com.nd2k.auth.models;

import lombok.Builder;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Builder
@Entity(name = "users")
public record User(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id,
        String email,
        String password,
        @ManyToMany(fetch = FetchType.EAGER) Collection<Role> roles) {
}
