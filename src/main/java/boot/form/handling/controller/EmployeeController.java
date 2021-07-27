package boot.form.handling.controller;

import java.util.ArrayList;
import java.util.List;

import boot.form.handling.model.Employee;
import boot.form.handling.model.UserRegistration;
import boot.form.handling.service.EmployeeService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EmployeeController {

    @Autowired
    public EmployeeService employeeService;

    @Autowired
    protected BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @RequestMapping("/welcome")

    public ModelAndView firstPage() {
        return new ModelAndView("welcome");
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register() {
        return new ModelAndView("registration", "user", new UserRegistration());
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView processRegister(@ModelAttribute("user") UserRegistration userRegistrationObject) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        String encodedPassword = bCryptPasswordEncoder.encode(userRegistrationObject.getPassword());

        val user = new User(userRegistrationObject.getUsername(), encodedPassword, authorities);
        jdbcUserDetailsManager.createUser((UserDetails) user);
        return new ModelAndView("redirect:/welcome");
    }

    @RequestMapping(value = "/addNewEmployee", method = RequestMethod.GET)
    public ModelAndView show() {
        return new ModelAndView("addEmployee", "emp", new Employee());
    }

    @RequestMapping(value = "/addNewEmployee", method = RequestMethod.POST)
    public ModelAndView processRequest(@ModelAttribute("emp") Employee emp) {

        employeeService.insertEmployee(emp);
        List<Employee> employees = employeeService.getAllEmployees();
        ModelAndView model = new ModelAndView("getEmployees");
        model.addObject("employees", employees);
        return model;
    }

    @RequestMapping("/getEmployees")
    public ModelAndView getEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        ModelAndView model = new ModelAndView("getEmployees");
        model.addObject("employees", employees);
        return model;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        if (logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");

        return "login";
    }
}
