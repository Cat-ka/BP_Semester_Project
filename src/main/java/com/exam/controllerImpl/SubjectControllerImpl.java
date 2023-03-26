package com.exam.controllerImpl;

import com.exam.constensts.ProjectConstants;
import com.exam.controller.SubjectController;
import com.exam.model.Subject;
import com.exam.service.SubjectService;
import com.exam.utils.ProjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class SubjectControllerImpl implements SubjectController {

    @Autowired
    SubjectService subjectService;


    @Override
    public ResponseEntity<String> addNewSubject(Map<String, String> requestMap) {
        try {
            return subjectService.addNewSubject(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Subject>> getAllSubject(String filterValue) {
        try {
            return subjectService.getAllSubject(filterValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateSubject(Map<String, String> requestMap) {
        try {
            return subjectService.updateSubject(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> deleteSubject(Long id) {
        try {
            return subjectService.deleteSubject(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
