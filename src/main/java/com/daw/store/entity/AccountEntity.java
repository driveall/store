package com.daw.store.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity {
    private String id;
    private String login;
    private String password;
    private String passwordConfirmed;
}
