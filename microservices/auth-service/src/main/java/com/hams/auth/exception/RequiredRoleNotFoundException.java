package com.hams.auth.exception;

public class RequiredRoleNotFoundException
        extends RuntimeException {

    public RequiredRoleNotFoundException(String roleName) {
        super("Required role not found: " + roleName);
    }
}