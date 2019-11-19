package com.codegym.controller;

import com.codegym.model.Department;
import com.codegym.model.Employee;
import com.codegym.repository.DepartmentRepository;
import com.codegym.service.DepartmentService;
import com.codegym.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DepartmentService departmentService;

    @ModelAttribute("departments")
    public Iterable<Department> departments(){
        return departmentService.findAll();
    }

    @GetMapping("/employee")
    public ModelAndView listEmplyee(@RequestParam("s") Optional<String> s, Pageable pageable){
        Page<Employee> employees;
        if (s.isPresent()){
            employees=employeeService.findAllByNameContraining(s.get(),pageable);
        }else {
            employees=employeeService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("/employee/list");
        modelAndView.addObject("employees", employees);
        return modelAndView;
    }
    @GetMapping("/create-employee")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView=new ModelAndView("/employee/create");
        modelAndView.addObject("employee", new Employee());
        return modelAndView;
    }
    @PostMapping("/create-employee")
    public ModelAndView saveCustomer(@ModelAttribute("employee") Employee employee){
        employeeService.save(employee);
        ModelAndView modelAndView = new ModelAndView("/employee/create");
        modelAndView.addObject("employee", new Employee());
        modelAndView.addObject("message", "New employee created successfully");
        return modelAndView;
    }

    @GetMapping("/edit-employee/{id}")
    public ModelAndView showEditForm(@PathVariable Long id){
        Employee employee=employeeService.findById(id);
        if (employee!=null){
            ModelAndView modelAndView=new ModelAndView("/employee/edit");
            modelAndView.addObject("employee",employee);
            return modelAndView;
        }else{
            ModelAndView modelAndView=new ModelAndView("/error.404");
            return modelAndView;
        }
    }
    @PostMapping("/edit-employee")
    public ModelAndView updateEm(@ModelAttribute("employee") Employee employee){
        employeeService.save(employee);
        ModelAndView modelAndView=new ModelAndView("/employee/edit");
        modelAndView.addObject("employee",employee);
        modelAndView.addObject("message","Employee Update succeddful");
        return modelAndView;
    }
    @GetMapping("/delete-employee/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id){
        Employee employee = employeeService.findById(id);
        if(employee != null) {
            ModelAndView modelAndView = new ModelAndView("/employee/delete");
            modelAndView.addObject("employee", employee);
            return modelAndView;

        }else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete-employee")
    public String deleteEm(@ModelAttribute("employee") Employee employee){
        employeeService.remove(employee.getId());
        return "redirect:employee";
    }




}
