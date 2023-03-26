package com.exam.serviceImpl;

import com.exam.config.JwtAuthFilter;
import com.exam.constensts.ProjectConstants;
import com.exam.model.Subject;
import com.exam.repo.SubjectRepository;
import com.exam.service.SubjectService;
import com.exam.utils.ProjectUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Override
    public ResponseEntity<String> addNewSubject(Map<String, String> requestMap) {
        try {
            if (jwtAuthFilter.isAdmin()) {
                if (validateCategoryMap(requestMap, false)) {
                    subjectRepository.save(getSubjectFromMap(requestMap, false));
                    return ProjectUtils.getResponseEntity("Category Added Successfully", HttpStatus.OK);
                }
            } else {
                return ProjectUtils.getResponseEntity(ProjectConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Subject>> getAllSubject(String filterValue) {
        try {
            if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
                log.info("Inside if getAllSubject");
                return new ResponseEntity<List<Subject>>(subjectRepository.getAllSubject(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(subjectRepository.findAll(), HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Subject>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateSubject(Map<String, String> requestMap) {
        try {
            if (jwtAuthFilter.isAdminAuth()) {
                if (validateCategoryMap(requestMap, true)) {
                    //ak je validácia tru, tak ho mám overeného
                    Optional optional = subjectRepository.findById(Integer.parseInt(requestMap.get("id")));
                    //toto mi ale vráti objekt typu optional, čiže ho tak musíme aj uložiť do optional
                    if (!optional.isEmpty()) {
                        subjectRepository.save(getSubjectFromMap(requestMap, true));
                        return ProjectUtils.getResponseEntity("Category Updated Successfuly.", HttpStatus.OK);
                    } else {
                        //ak chcem meniť id ktoré neexistuje
                        return ProjectUtils.getResponseEntity("Category id does not exist.", HttpStatus.OK);
                    }
                }
                return ProjectUtils.getResponseEntity(ProjectConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return ProjectUtils.getResponseEntity(ProjectConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> deleteSubject(Long id) {
        try {
            if (jwtAuthFilter.isAdminAuth()) {
                Optional optional = subjectRepository.findById(Integer.valueOf("id"));
                if (!optional.isEmpty()) {
                    subjectRepository.deleteById(Math.toIntExact(id));
                    return ProjectUtils.getResponseEntity("Category Updated Successfuly.", HttpStatus.OK);
                } else {
                    //ak chcem mazať id ktoré neexistuje
                    return ProjectUtils.getResponseEntity("Category id does not exist.", HttpStatus.OK);
                }
            } else {
                return ProjectUtils.getResponseEntity(ProjectConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ProjectUtils.getResponseEntity(ProjectConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Subject getSubjectFromMap(Map<String, String> requestMap, Boolean isAdd) {
        Subject subject = new Subject();
        if (isAdd) {
            subject.setId(Integer.parseInt(requestMap.get("id")));
        }
        subject.setName(requestMap.get("name"));
        return subject;
    }
}
