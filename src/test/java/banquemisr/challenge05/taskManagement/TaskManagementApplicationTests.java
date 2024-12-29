package banquemisr.challenge05.taskManagement;

import banquemisr.challenge05.taskManagement.model.entity.Role;
import banquemisr.challenge05.taskManagement.model.entity.Task;
import banquemisr.challenge05.taskManagement.model.entity.User;
import banquemisr.challenge05.taskManagement.model.enums.Priority;
import banquemisr.challenge05.taskManagement.model.enums.Status;
import banquemisr.challenge05.taskManagement.model.request.CreateUserRequest;
import banquemisr.challenge05.taskManagement.model.request.LoginUserRequest;
import banquemisr.challenge05.taskManagement.model.request.TaskFilter;
import banquemisr.challenge05.taskManagement.model.request.TaskRequest;
import banquemisr.challenge05.taskManagement.repository.RoleRepository;
import banquemisr.challenge05.taskManagement.repository.TaskRepository;
import banquemisr.challenge05.taskManagement.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
class TaskManagementApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TaskRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @AfterEach
    @BeforeEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    public void whenLogin_Valid_Credentials_thenReturnToken() throws IOException, Exception {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("admin@test");
        loginUserRequest.setPassword("123");
        mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(loginUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token", notNullValue()));

    }

    @Test
    public void whenLogin_Not_Valid_Credentials_thenReturnToken() throws IOException, Exception {
        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setEmail("admin@test");
        loginUserRequest.setPassword("test");
        mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(loginUserRequest)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "admin@test", roles = "ADMIN_ROLE")
    public void whenCreate_Valid_Credentials_thenReturn200() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("test@test");
        createUserRequest.setPassword("123");
        createUserRequest.setFullName("test user");
        createUserRequest.setAuthorities(List.of("ADMIN_ROLE"));

        mvc.perform(post("/api/v1/auth/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createUserRequest)))
                .andExpect(status().isOk());

        List<User> found = userRepository.findAll();
        assertThat(found).extracting(User::getEmail).contains("test@test");

    }

    @Test
    @WithMockUser(username = "user@test", roles = "USER_ROLE")
    public void whenCreate_Valid_Credentials_UnAuthorize_thenReturnUnauthorize() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("test@test");
        createUserRequest.setPassword("123");
        createUserRequest.setFullName("test user");
        createUserRequest.setAuthorities(List.of("ADMIN_ROLE"));

        mvc.perform(post("/api/v1/auth/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createUserRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin@test", roles = "ADMIN_ROLE")
    public void whenCreate_Valid_Credentials_EmailExist_thenReturnBadRequest() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("user@test");
        createUserRequest.setPassword("123");
        createUserRequest.setFullName("test user");
        createUserRequest.setAuthorities(List.of("ADMIN_ROLE"));

        mvc.perform(post("/api/v1/auth/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createUserRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin@test", roles = "ADMIN_ROLE")
    public void whenGet_AllUsers_thenReturn200() throws Exception {
        User user = createUser("test-get@test");

        mvc.perform(get("/api/v1/auth/users-list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

	@Test
	@WithMockUser(username = "user@test", roles = "USER_ROLE")
	public void whenGet_AllUsers_Unauthorize_thenReturn401() throws Exception {
		mvc.perform(get("/api/v1/auth/users-list")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}

    @Test
    @WithMockUser(username = "admin@test", roles = "ADMIN_ROLE")
    public void whenValidInput_thenCreateTask() throws IOException, Exception {
        TaskRequest task = getTaskObject("Task Test 1");
        mvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(task)))
                .andExpect(status().isCreated());

        List<Task> found = repository.findAll();
        assertThat(found).extracting(Task::getTitle).containsOnly("Task Test 1");
    }

    @Test
    @WithMockUser(username = "user@test", roles = "USER_ROLE")
    public void whenValidInput_UnAuthorize_thenReturn401() throws IOException, Exception {
        TaskRequest task = getTaskObject("Task Test 1");
        mvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(task)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin@test", roles = "ADMIN_ROLE")
    public void whenValidInput_InvalidInput_thenReturn400() throws IOException, Exception {
        TaskRequest task = getTaskObject(null);
        mvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(task)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin@test", roles = "ADMIN_ROLE")
    public void whenValidInput_thenUpdateTask() throws IOException, Exception {
        UUID testTaskId = createTestTask("task 1");
        TaskRequest task = getTaskObject("Task Test 1");
        task.setId(testTaskId);
        mvc.perform(put("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(task)))
                .andExpect(status().isCreated());

        List<Task> found = repository.findAll();
        assertThat(found).extracting(Task::getTitle).containsOnly("Task Test 1");
    }

    @Test
    @WithMockUser(username = "user@test", roles = "USER_ROLE")
    public void whenValidInput_UnAuthorize_thenNoUpdateReturn401() throws IOException, Exception {
        UUID testTaskId = createTestTask("task 1");
        TaskRequest task = getTaskObject("Task Test 1");
        task.setId(testTaskId);
        mvc.perform(put("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(task)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin@test", roles = "ADMIN_ROLE")
    public void whenValidInput_InvalidInput_Update_failed_thenReturn400() throws IOException, Exception {
        UUID testTaskId = createTestTask("task 1");
        TaskRequest task = getTaskObject(null);
        task.setId(testTaskId);
        mvc.perform(put("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(task)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(username = "admin@test", roles = "ADMIN_ROLE")
    public void givenTask_whenGetTask_thenStatus200()
            throws Exception {

        UUID testTaskId = createTestTask("task 1");

        mvc.perform(get("/api/v1/task/" + testTaskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("title", is("task 1"))).andExpect(jsonPath("id", is(String.valueOf(testTaskId))));
    }

    @Test
    @WithMockUser(username = "admin@test", roles = "ADMIN_ROLE")
    public void givenTask_whenGetTaskWithFilter_thenStatus200()
            throws Exception {

        UUID testTaskId = createTestTask("task for test");
        TaskFilter taskFilter = new TaskFilter();
        taskFilter.setTitle("task for test");
        taskFilter.setLimit(10);
        taskFilter.setOffset(0);

        mvc.perform(post("/api/v1/task/list")
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(taskFilter)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("tasks[0].title", is("task for test"))).andExpect(jsonPath("tasks[0].id", is(String.valueOf(testTaskId))));
    }

    @Test
    @WithMockUser(username = "user@test", roles = "USER_ROLE")
    public void givenTask_whenGetTaskWithFilter_UnauthorizeUser_thenStatus200()
            throws Exception {

        UUID testTaskId = createTestTask("task for test");
        TaskFilter taskFilter = new TaskFilter();
        taskFilter.setTitle("task for test");
        taskFilter.setLimit(10);
        taskFilter.setOffset(0);

        mvc.perform(post("/api/v1/task/list")
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(taskFilter)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("totalItems", is(0)));
    }

    @Test
    @WithMockUser(username = "admin@test", roles = "ADMIN_ROLE")
    public void givenTask_whenGetTaskPagination_thenStatus200()
            throws Exception {

        for (int i = 0; i < 20; i++) {
            createTestTask("test task" + i);
        }
        TaskFilter taskFilter = new TaskFilter();
        taskFilter.setLimit(10);
        taskFilter.setOffset(0);


        mvc.perform(post("/api/v1/task/list")
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(taskFilter)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("tasks", hasSize(10)))
                .andExpect(jsonPath("totalItems", greaterThanOrEqualTo(20)));
    }

    @Test
    @WithMockUser(username = "user@test", roles = "USER_ROLE")
    public void givenTask_whenGetTaskWithUnauthorizeUser_thenStatus400()
            throws Exception {

        UUID testTaskId = createTestTask("task 1");

        mvc.perform(get("/api/v1/task/" + testTaskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin@test", roles = "ADMIN_ROLE")
    public void deleteTask_whenDeleteTask_thenStatus204()
            throws Exception {

        UUID testTaskId = createTestTask("Task Test 1");

        List<Task> found = repository.findAll();
        assertThat(found).extracting(Task::getTitle).containsOnly("Task Test 1");

        mvc.perform(delete("/api/v1/task/" + testTaskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(jsonPath("$", is("deleted successfully")));

        List<Task> afterDelete = repository.findAll();
        assertThat(afterDelete).isEmpty();
    }

    @Test
    @WithMockUser(username = "user@test", roles = "USER_ROLE")
    public void deleteTask_whenDeleteTask_Unauthorize_thenStatus401()
            throws Exception {

        UUID testTaskId = createTestTask("Task Test 1");

        List<Task> found = repository.findAll();
        assertThat(found).extracting(Task::getTitle).containsOnly("Task Test 1");

        mvc.perform(delete("/api/v1/task/" + testTaskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin@test", roles = "ADMIN_ROLE")
    public void deleteTask_whenDeleteTask_NotFound_thenStatus400()
            throws Exception {

        mvc.perform(delete("/api/v1/task/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private UUID createTestTask(String taskTitle) {

        User user = userRepository.findByEmail("admin@test").get();
        Task task = new Task(null, taskTitle, "description", Status.DONE, Priority.HIGH, LocalDate.now().plusDays(2), user, new ArrayList<>());

        return repository.saveAndFlush(task).getId();
    }

    private TaskRequest getTaskObject(String taskTitle) {
        User user = userRepository.findByEmail("admin@test").get();
        return new TaskRequest(null, taskTitle, "description", Status.DONE, Priority.HIGH, LocalDate.now().plusDays(2), user.getId());
    }

    private User createUser(String email) {
        Set<Role> roles = roleRepository.findByAuthorityIn(List.of("ADMIN_ROLE"));
        User user = new User(null, "full name", email, "123", new Date(), new Date(), roles, new HashSet<>());

        return userRepository.saveAndFlush(user);
    }

    private byte[] toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(object);
    }

}
