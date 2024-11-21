package pl.desz.todo;

import org.springframework.boot.SpringApplication;

public class TestTodoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(TodoServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
