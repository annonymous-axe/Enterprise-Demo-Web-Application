package com.demo.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.demo.labelValue.LabelValueRepository;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class EmployeeController {
	
	@Autowired private EmployeeRepository empRepo;
	@Autowired private LabelValueRepository lblValRepo;
	
	@GetMapping("employeeView")
	public String clientViewPage(Model model) {
		
		List<EmployeeBean> employeeBeanList = empRepo.list();
		
		log.info("employeeBeanList :: "+employeeBeanList);
		
		model.addAttribute("employeeBeanList", employeeBeanList);
		
		return "/pages/employeeView";
	}
	
	@GetMapping("createEmployee")
	public String createEmployee(Model model) {
		
		log.info("Entering createEmployee.");
		
		EmployeeBean employeeBean = new EmployeeBean();
		
		employeeBean.setEntityLblValList(lblValRepo.getAllEntities());
		
		model.addAttribute("employee", employeeBean);
		model.addAttribute("empID", "");
		
		
		log.info("Exiting createEmployee.");
		
		return "/pages/employee";
	}
	
	@PostMapping(value = {"/employeeSave", "/employeeApprove", "/employeeUpdate", "/employeeDecline", "/employeeDelete"})
	public String progressEmployee(EmployeeBean employeeBean, @ModelAttribute("btnAction") String btnAction, Model model) {
		
		log.info("Entring progressEmployee.");
		
		log.info("Employee :: "+employeeBean.getEmpId()+", action :: "+btnAction);
		
		if(btnAction.equalsIgnoreCase("create")) {
			empRepo.save(employeeBean);
		}else if(btnAction.equalsIgnoreCase("approve")) {
			empRepo.authorize(employeeBean);
		}else if(btnAction.equalsIgnoreCase("update")) {
			empRepo.update(employeeBean);
		}else if(btnAction.equalsIgnoreCase("decline")) {
			empRepo.decline(employeeBean);
		}else if(btnAction.equalsIgnoreCase("delete")) {
			empRepo.delete(employeeBean);
		}
		
		log.info("Exiting progressEmployee.");
		
		return "redirect:/employeeView";
	}
	
	@PostMapping("getEmployee")
	public String getEmployee(@ModelAttribute("formData") String formData, Model model) {
		
		log.info("Entring getEmployee.");
		
		EmployeeBean employeeBean = empRepo.get(formData);
		
		employeeBean.setEntityLblValList(lblValRepo.getAllEntities());
		
		model.addAttribute("employee", employeeBean);
		model.addAttribute("empID", employeeBean.getEmpId());
		
		log.info("Exiting getEmployee.");
		
		return "/pages/employee";
	}	

}
