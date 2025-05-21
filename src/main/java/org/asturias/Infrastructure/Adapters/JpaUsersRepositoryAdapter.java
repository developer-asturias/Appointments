package org.asturias.Infrastructure.Adapters;


import org.asturias.Domain.Models.Users;
import org.asturias.Domain.Ports.Out.UsersRepositoryPort;
import org.asturias.Infrastructure.Entities.UsersEntity;
import org.asturias.Infrastructure.Mappers.Entities.UsersMapper;
import org.asturias.Infrastructure.Repositories.JpaUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaUsersRepositoryAdapter implements UsersRepositoryPort {

    @Autowired
    private final JpaUsersRepository usersRepository;


    @Autowired
    private UsersMapper usersMapper;

    
    public JpaUsersRepositoryAdapter(JpaUsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @Override
    public Optional<Users> findById(Long id) {
        return usersRepository.findById(id).map(usersEntity -> usersMapper.USERS(usersEntity));
    }

    @Override
    public Users save(Users users) {
        UsersEntity usersEntity = usersMapper.USERS_ENTITY(users);
        return usersMapper.USERS(usersRepository.save(usersEntity));
    }
}
