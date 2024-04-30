package com.tpe.controller;

import com.tpe.domain.Student;
import com.tpe.exception.StudentNotFoundException;
import com.tpe.service.IStudentService;
import com.tpe.service.StudentService;
import net.bytebuddy.matcher.StringMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller//requestler bu class ta karşılanacak ve ilgili metotlarla maplenecek
@RequestMapping("/students")//http:localhost:8080/SpringMvc/students/...
//class: tüm metotlar için geçerli olur
//metot seviyesinde kullanırsak sadece ilgili metodu request ile mapler
public class StudentController {

    @Autowired
    private IStudentService service;


    //http:localhost:8080/SpringMvc/students/hi + GET
    @GetMapping("/hi")
    public ModelAndView sayHi(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", "Hi, ");
        mav.addObject("messagebody", "I'm a Student Management System");
        mav.setViewName("hi");
        return mav;

    }
    //view resolver : /WEB-INF/views/hi.jsp dosyasını bulur ve mav içindeki modalı
    //dosyaya bind eder ve clienta gönderir.

    //1- tüm öğrencileri listeleme:
    //http://localhost:8080/SpringMvc/students + GET
    @GetMapping
    public ModelAndView getStudents(){

        List<Student> allStudents = service.listAllStudents();

        ModelAndView mav = new ModelAndView();
        mav.addObject("studentList", allStudents);
        mav.setViewName("students");

        return mav;
    }

    //2-öğrenciyi kaydetme
    //http://localhost:8080/SpringMvc/students/new + GET
    @GetMapping("/new")
    public String sendForm(@ModelAttribute("student") Student student){
        return "studentForm";
    }

    //ModelAttribute anotasyonu view katmanı ile controller arasında
    //modelin transferini sağlar.
    //işlem sonunda: student ın firstname, lastname ve grade değerleri set edilmiş halde
    //controller classında yer alır.

    //2-a : öğrenciyi DB ye ekleme
    //http://localhost:8080/SpringMvc/students/saveStudent + POST
    @PostMapping("/saveStudent")
    //modelattribute studentform jsp de de var onu koyacağımızı gösteriyoruz
    public String addStudent(@Valid @ModelAttribute("student") Student student, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "studentForm";
        }
        service.addOrUpdateStudent(student);
        return "redirect:/students";//http://localhost:8080/SpringMvc/students + GET
    }

    //3-mevcut öğrenciyi güncelleme
    //http://localhost:8080/SpringMvc/students/update?id=1 + GET

    @GetMapping("/update")
    //@RequestParam içindeki id adres satırında gelen ile aynı olmak zorunda fakat sonrasında gelen değişken
    //bizim atadığımız depişken
    public ModelAndView sendFormUpdate(@RequestParam("id") Long identity){
        Student foundStudent = service.findStudentById(identity);
        ModelAndView mav = new ModelAndView();
        mav.addObject("student", foundStudent);
        mav.setViewName("studentForm");
        return mav;
    }


    //4-mevcut öğrenciyi silme
    //http://localhost:8080/SpringMvc/students/delete/1 +GET

    //@GetMapping("/delete/{id}/{name}")
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long identity){
        service.deleteStudent(identity);
        return "redirect:/students";
    }

    //@ExceptionHandler: try - catch bloğu mantığıyla benzer çalışır.
    @ExceptionHandler(StudentNotFoundException.class)
    public ModelAndView handleException(Exception ex){
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", ex.getMessage());
        mav.setViewName("notFound");
        return mav;
    }
}
