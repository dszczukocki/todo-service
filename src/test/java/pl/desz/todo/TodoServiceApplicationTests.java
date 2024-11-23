package pl.desz.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.desz.todo.model.Todo;
import pl.desz.todo.persistence.TodoRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Import(MongoDBContainerConf.class)
@SpringBootTest
class TodoServiceApplicationTests {

    private static final Long TODO_ID = 1L;
    private static final String TODO_DESCRIPTION = "DESCRIPTION";

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void shouldSaveTodo() {
        final Todo todo = new Todo(TODO_ID, TODO_DESCRIPTION);
        final Mono<Todo> todoMono = todoRepository.save(todo);

        StepVerifier.create(todoMono)
                .expectNext(todo)
                .verifyComplete();
    }

    @Test
    void shouldFindTodoById() {
        final Todo todo = new Todo(TODO_ID, TODO_DESCRIPTION);
        todoRepository.save(todo).block();

        StepVerifier.create(todoRepository.findById(TODO_ID))
                .expectNextMatches(foundTodo -> foundTodo.getId().equals(TODO_ID) && foundTodo.getDescription().equals(TODO_DESCRIPTION))
                .verifyComplete();
    }

    @Test
    void shouldUpdateTodo() {
        final Todo todo = new Todo(TODO_ID, TODO_DESCRIPTION);
        todoRepository.save(todo).block();

        final Todo updatedTodo = new Todo(TODO_ID, "Updated");
        final Mono<Todo> monoUpdatedTodo = todoRepository.save(updatedTodo);

        StepVerifier.create(monoUpdatedTodo)
                .expectNextMatches(t -> t.getId().equals(TODO_ID) && t.getDescription().equals("Updated"))
                .verifyComplete();
    }

    @Test
    void shouldDeleteTodo() {
        final Todo todo = new Todo(TODO_ID, TODO_DESCRIPTION);
        todoRepository.save(todo).block();

        StepVerifier.create(todoRepository.deleteById(TODO_ID))
                .verifyComplete();

        StepVerifier.create(todoRepository.findById(TODO_ID))
                .expectNextCount(0)
                .verifyComplete();
    }
}