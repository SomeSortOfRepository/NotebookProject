package ru.dolinini.notebook.repos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.dolinini.notebook.model.NotebookEntry;

import java.util.List;

public interface NotebookRepo extends CrudRepository<NotebookEntry,Long> {
    @Query("select * from NotebookEntry n inner join User u  on u.id=n.user_id where n.user_id=?1")
    List<NotebookEntry> findByUserId(Long id);
}
