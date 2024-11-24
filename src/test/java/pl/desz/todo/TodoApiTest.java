package pl.desz.todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.desz.todo.api.TodoController;
import pl.desz.todo.model.Todo;
import pl.desz.todo.model.User;
import pl.desz.todo.service.TodoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = TodoController.class)
public class TodoApiTest {

    private static final String TODO_ID = "1";
    private static final Todo TODO = new Todo(TODO_ID, "desc", new User("user1"), 1);

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TodoService todoService;

    @Test
    void shouldGetAllTodos() {
        when(todoService.getAllTodos())
                .thenReturn(Flux.just(TODO, TODO));

        webTestClient.get()
                .uri("/todos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Todo.class)
                .hasSize(2);
    }

    @Test
    void shouldGetTodoById() {

        when(todoService.getById(TODO_ID))
                .thenReturn(Mono.just(TODO));

        webTestClient.get()
                .uri("/todos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Todo.class)
                .isEqualTo(TODO);
    }

    @Test
    void shouldSaveTodo() {
        when(todoService.saveTodo(any()))
                .thenReturn(Mono.just(TODO));

        webTestClient.post()
                .uri("/todos")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Todo.class);
    }

    @Test
    void shouldDeleteTodo() {
        when(todoService.deleteTodoById(TODO_ID))
                .thenReturn(Mono.empty().then());

        webTestClient.delete()
                .uri("/todos/1")
                .exchange()
                .expectStatus().isOk();
    }
}
