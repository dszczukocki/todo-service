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
import pl.desz.todo.service.TodoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = TodoController.class)
public class TodoApiTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TodoService todoService;

    @Test
    void shouldGetAllTodos() {
        when(todoService.getAllTodos())
                .thenReturn(Flux.just(
                        new Todo(1L, "DESC"),
                        new Todo(2L, "DESC")));

        webTestClient.get()
                .uri("/todos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Todo.class)
                .hasSize(2);
    }

    @Test
    void shouldGetTodoById() {
        final Todo todo = new Todo(1L, "DESC");

        when(todoService.getById(1L))
                .thenReturn(Mono.just(todo));

        webTestClient.get()
                .uri("/todos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Todo.class)
                .isEqualTo(todo);
    }

    @Test
    void shouldSaveTodo() {
        when(todoService.saveTodo(any()))
                .thenReturn(Mono.just(new Todo(1L, "DESC")));

        webTestClient.post()
                .uri("/todos")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Todo.class);
    }

    @Test
    void shouldDeleteTodo() {
        when(todoService.deleteTodoById(1L))
                .thenReturn(Mono.empty().then());

        webTestClient.delete()
                .uri("/todos/1")
                .exchange()
                .expectStatus().isOk();
    }
}
