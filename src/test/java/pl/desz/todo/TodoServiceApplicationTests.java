package pl.desz.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.desz.todo.model.Todo;
import pl.desz.todo.model.User;
import pl.desz.todo.persistence.TodoRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Import(MongoDBContainerConf.class)
@SpringBootTest
class TodoServiceApplicationTests {

    private static final String TODO_ID = "1";
    private static final String TODO_DESCRIPTION = "DESCRIPTION";
    private static final Todo TODO = new Todo(TODO_ID, TODO_DESCRIPTION, new User("user1"), 1);

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void shouldSaveTodo() {
        final Mono<Todo> todoMono = todoRepository.save(TODO);

        StepVerifier.create(todoMono)
                .expectNext(TODO)
                .verifyComplete();
    }

    @Test
    void shouldFindTodoById() {
        todoRepository.save(TODO).block();

        StepVerifier.create(todoRepository.findById(TODO_ID))
                .expectNextMatches(foundTodo -> foundTodo.getId().equals(TODO_ID) && foundTodo.getDescription().equals(TODO_DESCRIPTION))
                .verifyComplete();
    }

    @Test
    void shouldUpdateTodo() {
        todoRepository.save(TODO).block();

        final Todo updatedTodo = new Todo(TODO_ID, "Updated", new User("user1"), 1);
        final Mono<Todo> monoUpdatedTodo = todoRepository.save(updatedTodo);

        StepVerifier.create(monoUpdatedTodo)
                .expectNextMatches(t -> t.getId().equals(TODO_ID) && t.getDescription().equals("Updated"))
                .verifyComplete();
    }

    @Test
    void shouldDeleteTodo() {
        todoRepository.save(TODO).block();

        StepVerifier.create(todoRepository.deleteById(TODO_ID))
                .verifyComplete();

        StepVerifier.create(todoRepository.findById(TODO_ID))
                .expectNextCount(0)
                .verifyComplete();
    }
}