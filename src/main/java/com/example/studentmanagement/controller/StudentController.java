package com.example.studentmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.service.StudentService;

import jakarta.validation.Valid;

@Controller
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/addStudent")
    public String showAddStudentForm(Model model) {

        model.addAttribute("student", new Student());

        return "add-student";
    }

    @PostMapping("/saveStudent")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {

            if (student.getId() != null) {
                return "edit-student";
            }

            return "add-student";
        }

        boolean isNewStudent = (student.getId() == null);

        studentService.saveStudent(student);

        if (isNewStudent) {
            redirectAttributes.addFlashAttribute("success",
                    "Student Added Successfully!");
        } else {
            redirectAttributes.addFlashAttribute("success",
                    "Student Updated Successfully!");
        }

        return "redirect:/students";
    }

    @GetMapping("/students")
    public String viewStudents(
            @RequestParam(required = false) String keyword,
            Model model) {

        model.addAttribute("students",
                studentService.searchStudents(keyword));

        model.addAttribute("keyword", keyword);

        return "students";
    }

    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Long id, Model model) {

        Student student = studentService.getStudentById(id);

        model.addAttribute("student", student);

        return "edit-student";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {

        studentService.deleteStudent(id);

        redirectAttributes.addFlashAttribute("success",
                "Student Deleted Successfully!");

        return "redirect:/students";
    }

}