package com.ra.project_module4.service.imp;

import com.ra.project_module4.model.entity.User;
import com.ra.project_module4.repository.UserRepository;
import com.ra.project_module4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public Page<User> getUsers(int page, int size, String sortField, String sortDirection) {
        Sort sort = Sort.by(sortField);
        sort = sortDirection.equalsIgnoreCase("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findByUsernameContaining(String searchName, Integer page, Integer itemPage, String orderBy, String direction) {
        Pageable pageable = null;

        if(orderBy!=null && !orderBy.isEmpty()){
            // co sap xep
            Sort sort = null;
            switch (direction){
                case "ASC":
                    sort = Sort.by(orderBy).ascending();
                    break;
                case "DESC":
                    sort = Sort.by(orderBy).descending();
                    break;
            }
            pageable = PageRequest.of(page, itemPage,sort);
        }else{
            //khong sap xep
            pageable = PageRequest.of(page, itemPage);
        }

        //xu ly ve tim kiem
        if(searchName!=null && !searchName.isEmpty()){
            //co tim kiem
            return userRepository.findByUsernameContaining(searchName,pageable);
        }else{
            //khong tim kiem
            return userRepository.findAll(pageable);
        }
    }
}
