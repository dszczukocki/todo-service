package pl.desz.todo.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.desz.todo.model.Todo;
import pl.desz.todo.service.TodoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public Flux<Todo> getTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public Mono<Todo> getTodoById(@PathVariable Long id) {
        return todoService.getById(id);
    }

    @PostMapping
    public Mono<Todo> addTodo(Mono<Todo> todoMono) {
        return todoService.saveTodo(todoMono);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTodo(@PathVariable Long id) {
        return todoService.deleteTodoById(id);
    }
}
