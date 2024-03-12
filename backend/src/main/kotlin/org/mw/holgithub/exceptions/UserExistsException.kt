package org.mw.holgithub.exceptions

class UserExistsException(username: String) :
    Exception("User with username '$username' already exists")