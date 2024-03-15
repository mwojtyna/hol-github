package org.mw.holgithub.exception

class UserAlreadyExistsException(username: String) :
    Exception("User with username '$username' already exists")
