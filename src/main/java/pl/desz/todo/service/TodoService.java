package pl.desz.todo.service;

import org.springframework.stereotype.Service;
import pl.desz.todo.model.Todo;
import pl.desz.todo.persistence.TodoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Flux<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Mono<Todo> getById(Long id) {
        return todoRepository.findById(id);
    }

    public Mono<Todo> saveTodo(Mono<Todo> todoMono) {
        return todoMono.flatMap(todoRepository::save);
    }

    public Mono<Void> deleteTodoById(Long id) {
        return todoRepository.deleteById(id);
    }
}
