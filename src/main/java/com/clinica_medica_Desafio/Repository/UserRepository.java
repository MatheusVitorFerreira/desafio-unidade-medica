package com.clinica_medica_Desafio.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.clinica_medica_Desafio.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	UserDetails findByLogin(String login);
}
