package org.asturias.Infrastructure.Repositories;

import org.asturias.Infrastructure.Entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUsersRepository   extends JpaRepository<UsersEntity, Long> {
}
