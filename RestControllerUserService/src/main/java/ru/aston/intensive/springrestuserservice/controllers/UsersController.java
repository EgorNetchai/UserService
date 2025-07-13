package ru.aston.intensive.springrestuserservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.intensive.springrestuserservice.dto.UserDto;
import ru.aston.intensive.springrestuserservice.models.UserEntity;
import ru.aston.intensive.springrestuserservice.services.UserMapper;
import ru.aston.intensive.springrestuserservice.services.UsersServiceCrud;
import ru.aston.intensive.springrestuserservice.util.UserNotCreatedException;
import ru.aston.intensive.springrestuserservice.util.UserNotFoundException;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST-контроллер для управления пользователями.
 * Обрабатывает HTTP-запросы для выполнения CRUD-операций над пользователями.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Взаимодействие с пользователями")
public class UsersController {

    private final UsersServiceCrud usersServiceCRUD;
    private final UserMapper userMapper;

    /**
     * Конструктор контроллера пользователей.
     *
     * @param usersServiceCRUD Сервис для работы с пользователями
     * @param userMapper Маппер для преобразования объектов между UserEntity и UserDto
     */
    @Autowired
    public UsersController(UsersServiceCrud usersServiceCRUD, UserMapper userMapper) {
        this.usersServiceCRUD = usersServiceCRUD;
        this.userMapper = userMapper;
    }


    /**
     * Получает список всех пользователей.
     *
     * @return Список пользователей в формате UserDto
     *
     * @throws UserNotFoundException Если пользователи не найдены
     */
    @GetMapping
    @Operation(
            summary = "Получение всех пользователей",
            description = "Позволяет получить список данных всех пользователей"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Все пользователи получены", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Страница не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public CollectionModel<UserDto> getUsers() {
        List<UserDto> users = usersServiceCRUD.findAll().stream()
                .map(userEntity -> {
                    UserDto userDto = userMapper.toUserDto(userEntity);
                    userDto.add(linkTo(methodOn(UsersController.class).getUser(userEntity.getId())).withSelfRel());
                    userDto.add(linkTo(methodOn(UsersController.class).getUsers()).withRel("users"));
                    userDto.add(Link.of("users/update/" + userEntity.getId(), "update").withType("PUT"));
                    return userDto;
                })
                .toList();
        return CollectionModel.of(users,
                linkTo(methodOn(UsersController.class).getUsers()).withSelfRel(),
                Link.of("users/create", "create").withType("POST"));
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     *
     * @return пользователь в формате UserDto
     *
     * @throws UserNotFoundException если пользователь не найден
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Получение пользователя",
            description = "Позволяет получить данные пользователя по его Id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь получен успешно", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Страница не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public EntityModel<UserDto> getUser(@Parameter(description = "Id пользователя") @PathVariable("id") Long id) {
       UserDto userDto = userMapper.toUserDto(usersServiceCRUD.findOne(id));
       userDto.add(linkTo(methodOn(UsersController.class).getUser(id)).withSelfRel());
       userDto.add(linkTo(methodOn(UsersController.class).getUsers()).withRel("users"));
       userDto.add(Link.of("/users/update/" + id, "update").withType("PUT"));
       userDto.add(linkTo(methodOn(UsersController.class).deleteUser(id)).withRel("delete"));
       return EntityModel.of(userDto);
    }

    /**
     * Создает нового пользователя.
     *
     * @param userDto       Данные пользователя в формате UserDto
     * @param bindingResult Результат валидации
     *
     * @return HTTP-статус OK при успешном создании
     *
     * @throws UserNotCreatedException  Если данные пользователя некорректны
     * @throws IllegalArgumentException Если email уже занят
     */
    @PostMapping("/create")
    @Operation(
            summary = "Создание пользователя",
            description = "Позволяет создавать и сохранять пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь создан успешно", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Ошибка создания пользователя"),
            @ApiResponse(responseCode = "404", description = "Страница не найдена"),
            @ApiResponse(responseCode = "405", description = "Метод не разрешен"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ResponseEntity<UserDto> createUser(
            @RequestBody @Valid @Parameter(description = "Объект пользователя") UserDto userDto,
            @Parameter(description = "Ожидаемый результат")BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new UserNotCreatedException(errorMsg.toString());
        }

        UserEntity savedUser = usersServiceCRUD.save(userMapper.toUserEntity(userDto));
        UserDto saveUserDto = userMapper.toUserDto(savedUser);
        saveUserDto.add(linkTo(methodOn(UsersController.class).getUser(savedUser.getId())).withSelfRel());
        saveUserDto.add(linkTo(methodOn(UsersController.class).getUsers()).withRel("users"));

        return ResponseEntity.status(HttpStatus.CREATED).body(saveUserDto);
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param id            идентификатор пользователя
     * @param userDto       обновленные данные пользователя в формате UserDto
     * @param bindingResult результат валидации
     *
     * @return HTTP-статус OK при успешном обновлении
     *
     * @throws UserNotCreatedException  если данные пользователя некорректны
     * @throws UserNotFoundException    если пользователь не найден
     * @throws IllegalArgumentException если email уже занят
     */
    @PutMapping("/update/{id}")
    @Operation(
            summary = "Обновление пользователя",
            description = "Обновляет существующего пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
            }),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Страница не найдена"),
            @ApiResponse(responseCode = "405", description = "Метод не разрешен"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "Id пользователя") @PathVariable("id")  Long id,
            @Parameter(description = "Объект пользователя") @RequestBody @Valid  UserDto userDto,
            @Parameter(description = "Ожидаемый результат") BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new UserNotCreatedException(errorMsg.toString());
        }

        UserEntity updatedUserEntity = usersServiceCRUD.update(id, userMapper.toUserEntity(userDto));
        UserDto updatedUserDto = userMapper.toUserDto(updatedUserEntity);
        updatedUserDto.add(linkTo(methodOn(UsersController.class).getUser(id)).withSelfRel());
        updatedUserDto.add(linkTo(methodOn(UsersController.class).getUsers()).withRel("users"));
        updatedUserDto.add(linkTo(methodOn(UsersController.class).deleteUser(id)).withRel("delete"));

        return ResponseEntity.ok(updatedUserDto);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     *
     * @return HTTP-статус OK при успешном удалении
     *
     * @throws UserNotFoundException если пользователь не найден
     */
    @DeleteMapping("/delete/{id}")
    @Operation( summary = "Удаление пользователя",
                description = "Удаление существующего пользователя по его ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "no content"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Страница не найдена"),
            @ApiResponse(responseCode = "405", description = "Метод не разрешен"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Id пользователя") @PathVariable("id") Long id
    ) {
        usersServiceCRUD.delete(id);

        return ResponseEntity.noContent().build();
    }
}
