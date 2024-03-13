package org.mw.holgithub.exception

class UserNotFoundException(username: String) :
    Exception("User with username '$username' not found")