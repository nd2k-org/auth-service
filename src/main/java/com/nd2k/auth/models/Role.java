package com.nd2k.auth.models;

import lombok.Builder;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@Entity
public record Role(
        @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id,
        String roleName) {
}
