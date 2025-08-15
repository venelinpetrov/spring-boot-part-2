package com.codewithmosh.store.user.repositories;

import org.springframework.data.repository.CrudRepository;

import com.codewithmosh.store.user.entities.Profile;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}