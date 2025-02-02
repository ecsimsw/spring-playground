package com.ecsimsw.abcde.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {

    @PreAuthorize("hasAnyRole('ADMIN', 'A')")
    @GetMapping("/api/abcde/a")
    public ResponseEntity<Void> a() {
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'B')")
    @GetMapping("/api/abcde/b")
    public ResponseEntity<Void> b() {
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'B')")
    @GetMapping("/api/abcde/c")
    public ResponseEntity<Void> c() {
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'B')")
    @GetMapping("/api/abcde/d")
    public ResponseEntity<Void> d() {
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'B')")
    @GetMapping("/api/abcde/e")
    public ResponseEntity<Void> e() {
        return ResponseEntity.ok().build();
    }
}
