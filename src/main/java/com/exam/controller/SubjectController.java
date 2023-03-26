package com.exam.controller;


import com.exam.model.Subject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/subject")
public interface SubjectController {

    @PostMapping(path = "/add")
    ResponseEntity<String> addNewSubject(@RequestBody(required = true)Map<String, String> requestMap);

    @GetMapping(path = "/get")
    ResponseEntity<List<Subject>> getAllSubject (@RequestParam(required = false) String filterValue);

    @PostMapping(path = "/update")
    ResponseEntity<String> updateSubject(@RequestBody(required = true)
                                          Map<String, String> requestMap);

    @DeleteMapping(path = "/delete/{id}")
    ResponseEntity<?> deleteSubject(@PathVariable("id") Long id);

}
