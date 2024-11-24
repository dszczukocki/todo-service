package pl.desz.todo.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pl.desz.todo.model.Todo;

@Repository
public interface TodoRepository extends ReactiveCrudRepository<Todo, String> {
}
