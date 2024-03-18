package org.mw.holgithub.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionHandlers {
    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentialsExceptionHandler(exception: BadCredentialsException): ResponseEntity<Error> {
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidExceptionHandler(exception: MethodArgumentNotValidException): HashMap<String, String> {
        val errors = HashMap<String, String>()
        exception.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage ?: ""
            errors[fieldName] = errorMessage
        }
        return HashMap(errors)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun noSuchElementExceptionHandler(exception: NoSuchElementException): ResponseEntity<Error> {
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun userNotFoundExceptionHandler(exception: UserNotFoundException): ResponseEntity<Error> {
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun userAlreadyExistsExceptionHandler(exception: UserAlreadyExistsException): ResponseEntity<Error> {
        return ResponseEntity(HttpStatus.CONFLICT)
    }
}