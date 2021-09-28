package dev.luchonetvv.userapi.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dev.luchonetvv.userapi.domain.entities.RoleEntity;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(String name);
}
