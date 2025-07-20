package com.pos.server.web.controller;


import com.pos.server.domain.Customer;
import com.pos.server.domain.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/save")
    public ResponseEntity <Customer> save (@RequestBody Customer customer){
        System.out.println("LALALALALALLALALA");

        System.out.println(customer);
        return new ResponseEntity<>(customerService.save(customer), HttpStatus.CREATED);
    }

}
