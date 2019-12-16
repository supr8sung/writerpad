package com.xebia.fs101.writerpad.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
@PreAuthorize("hasAnyRole('WRITER') or hasRole('ADMIN')")
public @interface WriterOrAdminOnly {
}
