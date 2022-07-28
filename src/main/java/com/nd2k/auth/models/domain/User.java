package com.nd2k.auth.models.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
public class User {
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        Long id;
        String email;
        String password;
        @ManyToMany(fetch = FetchType.EAGER)
        Collection<Role> roles;

        public User(String email, String password) {
                this.email = email;
                this.password = password;
        }
}
