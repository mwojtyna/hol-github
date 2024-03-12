package org.mw.holgithub.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlers {
    @ExceptionHandler(UserExistsException::class)
    fun userExistsExceptionHandler(exception: UserExistsException): ResponseEntity<Unit> {
        return ResponseEntity(HttpStatus.CONFLICT)
    }
}