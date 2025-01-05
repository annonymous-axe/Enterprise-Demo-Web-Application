package com.demo.entity;

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
public class EntityController {
	
	@Autowired private EntityRepository entityRepo;
	
	@GetMapping("entityView")
	public String clientViewPage(Model model) {
		
		List<EntityBean> entityBeanList = entityRepo.list();
		
		log.info("entityBeanList :: "+entityBeanList);
		
		model.addAttribute("entityBeanList", entityBeanList);
		
		return "/pages/entityView";
	}
	
	@GetMapping("createEntity")
	public String createEmployee(Model model) {
		
		model.addAttribute("entity", new EntityBean());
		model.addAttribute("entityID", "");
		
		return "/pages/entity";
	}
	
	@PostMapping(value = {"/entitySave", "/entityApprove", "/entityUpdate", "/entityDecline", "/entityDelete"})
	public String progressEmployee(EntityBean entityBean, @ModelAttribute("btnAction") String btnAction, Model model) {
		
		log.info("Entring progressEntity.");
		
		log.info("Entity :: "+entityBean.getEntityId()+", action :: "+btnAction);
		
		if(btnAction.equalsIgnoreCase("create")) {
			entityRepo.save(entityBean);
		}else if(btnAction.equalsIgnoreCase("approve")) {
			entityRepo.authorize(entityBean);
		}else if(btnAction.equalsIgnoreCase("update")) {
			entityRepo.update(entityBean);
		}else if(btnAction.equalsIgnoreCase("decline")) {
			entityRepo.decline(entityBean);
		}else if(btnAction.equalsIgnoreCase("delete")) {
			entityRepo.delete(entityBean);
		}
		
		log.info("Exiting progressEntity.");
		
		return "redirect:/entityView";
	}
	
	@PostMapping("getEntity")
	public String getEmployee(@ModelAttribute("formData") String formData, Model model) {
		
		log.info("Entring getEntity.");
		
		EntityBean employeeBean = entityRepo.get(formData);
		
		model.addAttribute("entity", employeeBean);
		model.addAttribute("entityID", employeeBean.getEntityId());
		
		log.info("Exiting getEntity.");
		
		return "/pages/entity";
	}	

}
