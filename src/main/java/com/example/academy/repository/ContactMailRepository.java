package com.example.academy.repository;

import com.example.academy.model.entity.ContactMailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMailRepository extends JpaRepository<ContactMailEntity, Integer> {
}
