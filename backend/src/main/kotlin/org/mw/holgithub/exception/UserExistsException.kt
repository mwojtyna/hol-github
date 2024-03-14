package org.mw.holgithub.exception

class UserExistsException(username: String) :
    Exception("User with username '$username' already exists")