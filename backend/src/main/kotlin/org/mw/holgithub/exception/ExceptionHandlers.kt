package org.mw.holgithub.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlers {
    @ExceptionHandler(UserExistsException::class)
    fun userExistsExceptionHandler(exception: UserExistsException): ResponseEntity<Unit> {
        return ResponseEntity(HttpStatus.CONFLICT)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun userNotFoundExceptionHandler(exception: UserNotFoundException): ResponseEntity<Unit> {
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentialsExceptionHandler(exception: BadCredentialsException): ResponseEntity<Unit> {
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }
}