package com.binance.feign.sample.provider.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.binance.feign.sample.provider.domain.Employee;
import com.binance.feign.sample.provider.mapper.EmployeeMapper;

@Service
@Transactional
public class EmployeeServiceImpl {

	@Autowired
	protected EmployeeMapper employeeRepository;

	public void saveEmployee(Employee emp) {
		employeeRepository.insert(emp);
	}

	public Boolean deleteEmployee(String empId) {
		Employee temp = employeeRepository.selectById(empId);
		if (temp != null) {
			employeeRepository.deleteById(empId);
			return true;
		}
		return false;
	}

	public void editEmployee(Employee emp) {
		employeeRepository.updateById(emp);
	}

	public Collection<Employee> getAllEmployees() {
		List<Employee> itr = employeeRepository.selectList(Wrappers.emptyWrapper());
		return itr;
	}

	public Employee findEmployee(String empId) {
		return employeeRepository.selectById(empId);
	}

}
