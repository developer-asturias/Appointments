package org.asturias.Domain.Ports.Out;

import org.asturias.Domain.Models.Appointments;
import org.asturias.Domain.Models.Users;

import java.util.Optional;

public interface UsersRepositoryPort {

    Optional<Users> findById (Long id);
    Users save (Users users);
    Optional <Users> update (Users users);
}
