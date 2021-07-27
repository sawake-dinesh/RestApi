package boot.form.handling.service.impl;

import boot.form.handling.dao.EmployeeDao;
import boot.form.handling.model.Employee;
import boot.form.handling.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class EmployeeServiceImpl implements EmployeeService
{
    @Autowired
    EmployeeDao employeeDao;

    @Override
    public void insertEmployee(Employee employee) {
        employeeDao.insertEmployee(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeDao.getAllEmployees();
    }

    //@Override
    public void getEmployeeById(String empId) {
        Employee employee = employeeDao.getEmployeeById(empId);
        System.out.println(employee);
    }

}
