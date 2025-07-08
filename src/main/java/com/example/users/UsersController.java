package com.example.users;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UsersController {
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Iterable<User>> getAll(){
        return ResponseEntity.ok(this.userRepository.findAll());
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> findUserByEmail(@PathVariable String email) {
        return ResponseEntity.of(this.userRepository.findById(email));
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<User> save(@RequestBody @Valid User user){
        if (user.getGravatarUrl()==null)
            user.setGravatarUrl(UserGravatar.getGravatarUrlFromEmail(user.getEmail()));
        if (user.getUserRole()==null)
            user.setUserRole(Collections.singleton(UserRole.INFO));


        User result = this.userRepository.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{email}")
                .buildAndExpand(result.getEmail())
                .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String email){
        this.userRepository.deleteById(email);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        errors.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return errors;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
        Map<String,Object> errors = new HashMap<>();
        errors.put("code",HttpStatus.BAD_REQUEST.value());
        errors.put("message",ex.getMessage());
        errors.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return errors;
    }
}
