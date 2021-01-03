package ru.dolinini.notebook.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dolinini.notebook.model.User;

public interface UserRepo extends JpaRepository<User,Long> {
    User findByFirstname (String firstname);
    boolean existsByFirstname(String firstname);
}
