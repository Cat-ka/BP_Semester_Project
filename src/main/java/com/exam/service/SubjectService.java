package com.exam.service;

import com.exam.model.Subject;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface SubjectService {
    ResponseEntity<String> addNewSubject(Map<String, String> requestMap);

    ResponseEntity<List<Subject>> getAllSubject(String filterValue);

    ResponseEntity<String> updateSubject(Map<String, String> requestMap);

    ResponseEntity<?> deleteSubject(Long id);
}
