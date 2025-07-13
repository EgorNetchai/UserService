package ru.aston.intensive.springrestuserservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.aston.intensive.springrestuserservice.controllers.UsersController;
import ru.aston.intensive.springrestuserservice.dto.UserDto;
import ru.aston.intensive.springrestuserservice.models.UserEntity;
import ru.aston.intensive.springrestuserservice.services.UserMapper;
import ru.aston.intensive.springrestuserservice.services.UsersServiceCrudImpl;
import ru.aston.intensive.springrestuserservice.util.UserNotFoundException;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тестовый класс для проверки REST-контроллера {@link UsersController}.
 * Использует {@link MockMvc} для эмуляции HTTP-запросов и проверяет корректность ответов.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
})
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsersServiceCrudImpl usersServiceImpl;

    @MockitoBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Получение списка всех пользователей")
    void testGetAllUsers() throws Exception {
        UserEntity userEntity = new UserEntity("John Doe", "john@example.com", 30);
        userEntity.setId(1L);
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");
        userDto.setAge(30);
        List<UserEntity> userEntities = List.of(userEntity);

        when(usersServiceImpl.findAll()).thenReturn(userEntities);
        when(userMapper.toUserDto(userEntity)).thenReturn(userDto);

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userDtoList", hasSize(1)))
                .andExpect(jsonPath("$._embedded.userDtoList[0].name", is("John Doe")))
                .andExpect(jsonPath("$._embedded.userDtoList[0].email", is("john@example.com")))
                .andExpect(jsonPath("$._embedded.userDtoList[0].age", is(30)))
                .andExpect(jsonPath("$._embedded.userDtoList[0]._links.self.href",
                        is("http://localhost/users/1")))
                .andExpect(jsonPath("$._embedded.userDtoList[0]._links.users.href",
                        is("http://localhost/users")))
                .andExpect(jsonPath("$._embedded.userDtoList[0]._links.update.href",
                        is("users/update/1")))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/users")))
                .andExpect(jsonPath("$._links.create.href", is("users/create")));

        verify(usersServiceImpl, times(1)).findAll();
        verify(userMapper, times(1)).toUserDto(userEntity);
    }

    @Test
    @DisplayName("Получение пользователя по идентификатору")
    void testGetUserById() throws Exception {
        UserEntity userEntity = new UserEntity("John Doe", "john@example.com", 30);
        userEntity.setId(1L);
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");
        userDto.setAge(30);

        when(usersServiceImpl.findOne(1L)).thenReturn(userEntity);
        when(userMapper.toUserDto(userEntity)).thenReturn(userDto);

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")))
                .andExpect(jsonPath("$.age", is(30)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$._links.users.href", is("http://localhost/users")))
                .andExpect(jsonPath("$._links.update.href", is("/users/update/1")))
                .andExpect(jsonPath("$._links.delete.href", is("http://localhost/users/delete/1")));

        verify(usersServiceImpl, times(1)).findOne(1L);
        verify(userMapper, times(1)).toUserDto(userEntity);
    }

    @Test
    @DisplayName("Обработка ошибки при получении несуществующего пользователя")
    void testGetUserByIdWhenNotFound() throws Exception {
        when(usersServiceImpl.findOne(1L)).thenThrow(new UserNotFoundException("Пользователь с таким id не найден!"));

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Пользователь с таким id не найден!")));

        verify(usersServiceImpl, times(1)).findOne(1L);
        verify(userMapper, never()).toUserDto(any());
    }

    @Test
    @DisplayName("Создание нового пользователя")
    void testCreateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");
        userDto.setAge(30);
        UserEntity userEntity = new UserEntity("John Doe", "john@example.com", 30);
        userEntity.setId(1L);

        when(userMapper.toUserEntity(any(UserDto.class))).thenReturn(userEntity);
        when(usersServiceImpl.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toUserDto(userEntity)).thenReturn(userDto);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")))
                .andExpect(jsonPath("$.age", is(30)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$._links.users.href", is("http://localhost/users")));

        verify(userMapper, times(1)).toUserEntity(any(UserDto.class));
        verify(usersServiceImpl, times(1)).save(userEntity);
        verify(userMapper, times(1)).toUserDto(userEntity);
    }

    @Test
    @DisplayName("Обработка ошибок валидации при создании пользователя")
    void testCreateUserWithInvalidData() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John123");
        userDto.setEmail("invalid-email");
        userDto.setAge(-1);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Имя не должно содержать цифры")))
                .andExpect(jsonPath("$.message", containsString("Возраст должен быть больше 0 лет")))
                .andExpect(jsonPath("$.message", containsString("Email не должен содержать запрещенные символы")));

        verify(userMapper, never()).toUserEntity(any());
        verify(usersServiceImpl, never()).save(any(UserEntity.class));
        verify(userMapper, never()).toUserDto(any());
    }

    @Test
    @DisplayName("Обновление данных пользователя")
    void testUpdateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("Jane Doe");
        userDto.setEmail("jane@example.com");
        userDto.setAge(25);
        UserEntity updatedUserEntity = new UserEntity("Jane Doe", "jane@example.com", 25);
        updatedUserEntity.setId(1L);

        when(userMapper.toUserEntity(any(UserDto.class))).thenReturn(updatedUserEntity);
        when(usersServiceImpl.update(eq(1L), any(UserEntity.class))).thenReturn(updatedUserEntity);
        when(userMapper.toUserDto(updatedUserEntity)).thenReturn(userDto);

        mockMvc.perform(put("/users/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Jane Doe")))
                .andExpect(jsonPath("$.email", is("jane@example.com")))
                .andExpect(jsonPath("$.age", is(25)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$._links.users.href", is("http://localhost/users")))
                .andExpect(jsonPath("$._links.delete.href", is("http://localhost/users/delete/1")));

        verify(userMapper, times(1)).toUserEntity(any(UserDto.class));
        verify(usersServiceImpl, times(1)).update(eq(1L), any(UserEntity.class));
        verify(userMapper, times(1)).toUserDto(updatedUserEntity);
    }

    @Test
    @DisplayName("Удаление пользователя по идентификатору")
    void testDeleteUser() throws Exception {
        doNothing().when(usersServiceImpl).delete(1L);

        mockMvc.perform(delete("/users/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(usersServiceImpl, times(1)).delete(1L);
        verify(userMapper, never()).toUserDto(any());
    }

    @Test
    @DisplayName("Обработка ошибки при удалении несуществующего пользователя")
    void testDeleteWhenUserNotFound() throws Exception {
        doThrow(new UserNotFoundException("Пользователь с таким id не найден!")).when(usersServiceImpl).delete(1L);

        mockMvc.perform(delete("/users/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Пользователь с таким id не найден!")));

        verify(usersServiceImpl, times(1)).delete(1L);
        verify(userMapper, never()).toUserDto(any());
    }

    @Test
    @DisplayName("Обработка ошибки при создании пользователя с занятым email")
    void testCreateUserWithDuplicateEmail() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("john@example.com");
        userDto.setAge(30);
        UserEntity userEntity = new UserEntity("John Doe", "john@example.com", 30);

        when(userMapper.toUserEntity(any(UserDto.class))).thenReturn(userEntity);
        doThrow(new IllegalArgumentException("Email уже занят")).when(usersServiceImpl).save(any(UserEntity.class));

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Email уже занят")));

        verify(userMapper, times(1)).toUserEntity(any(UserDto.class));
        verify(usersServiceImpl, times(1)).save(any(UserEntity.class));
        verify(userMapper, never()).toUserDto(any());
    }

    @Test
    @DisplayName("Обработка ошибки при обновлении несуществующего пользователя")
    void testUpdateWhenUserNotFound() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("Jane Doe");
        userDto.setEmail("jane@example.com");
        userDto.setAge(25);
        UserEntity userEntity = new UserEntity("Jane Doe", "jane@example.com", 25);

        when(userMapper.toUserEntity(any(UserDto.class))).thenReturn(userEntity);
        doThrow(new UserNotFoundException("Пользователь с таким id не найден!"))
                .when(usersServiceImpl).update(eq(1L), any(UserEntity.class));

        mockMvc.perform(put("/users/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Пользователь с таким id не найден!")));

        verify(userMapper, times(1)).toUserEntity(any(UserDto.class));
        verify(usersServiceImpl, times(1)).update(eq(1L), any(UserEntity.class));
        verify(userMapper, never()).toUserDto(any());
    }

    @Test
    @DisplayName("Получение пустого списка пользователей")
    void testGetAllUsersWhenEmptyList() throws Exception {
        when(usersServiceImpl.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").doesNotExist())
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/users")))
                .andExpect(jsonPath("$._links.create.href", is("users/create")));

        verify(usersServiceImpl, times(1)).findAll();
        verify(userMapper, never()).toUserDto(any());
    }

    @Test
    @DisplayName("Создание пользователя с граничными значениями")
    void testCreateUserBoundaryValues() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("Ab");
        userDto.setEmail("test@example.com");
        userDto.setAge(150);
        UserEntity userEntity = new UserEntity("Ab", "test@example.com", 150);
        userEntity.setId(1L);

        when(userMapper.toUserEntity(any(UserDto.class))).thenReturn(userEntity);
        when(usersServiceImpl.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toUserDto(userEntity)).thenReturn(userDto);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Ab")))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.age", is(150)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/users/1")))
                .andExpect(jsonPath("$._links.users.href", is("http://localhost/users")));

        verify(userMapper, times(1)).toUserEntity(any(UserDto.class));
        verify(usersServiceImpl, times(1)).save(userEntity);
        verify(userMapper, times(1)).toUserDto(userEntity);
    }
}
