package com.demo.yarnType;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.labelValue.LabelValue;
import com.demo.labelValue.LabelValueRepository;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class YarnTypeController {
	
	@Autowired
	private YarnTypeRepository yarnTypeRepo;
	@Autowired private LabelValueRepository lblValRepo;
	
	@GetMapping("yarnTypeView")
	public String yarnTypeViewPage(Model model) {
		
		List<YarnTypeBean> yarnTypeBeanList = yarnTypeRepo.list();
		
		log.info("yarnTypeBeanList :: "+yarnTypeBeanList);
		
		model.addAttribute("yarnTypeBeanList", yarnTypeBeanList);
		
		return "/pages/yarnTypeView";
	}
	
	@GetMapping("createYarnType")
	public String createYarnType(Model model) {
		
		YarnTypeBean clientBean = new YarnTypeBean();
		
		try {
	    	ArrayList<String> clientList = lblValRepo.getClientNameLabelValues();
	    	log.info("clientList ::"+clientList);
	    	model.addAttribute("clientList", clientList);
		}catch(Exception e) {
			log.info("Exception :: "+e);
		}
		
		model.addAttribute("client", clientBean);
		model.addAttribute("clientID", "");
		
		return "/pages/yarnTypeView";
	}
	
	@PostMapping(value = {"/yarnTypeSave", "/yarnTypeApprove", "/yarnTypeUpdate", "/yarnTypeDecline", "/yarnTypeDelete"})
	public String progressYarnType(@ModelAttribute("formData") String formData, @ModelAttribute("btnAction") String btnAction, Model model) {
		
		log.info("Entring progressYarnType.");
		
		log.info("formData :: "+formData);
		
		if(btnAction.equalsIgnoreCase("create")) {
			yarnTypeRepo.save(formData);
		}else if(btnAction.equalsIgnoreCase("delete")) {
			YarnTypeBean YarnTypeBean = yarnTypeRepo.get(formData);
			yarnTypeRepo.delete(YarnTypeBean);
		}
		
		log.info("Exiting progressYarnType.");
		
		return "redirect:/yarnTypeView";
	}
	
	@ResponseBody
	@PostMapping("getYarnType")
	public YarnTypeBean getYarnType(@ModelAttribute("yarnTypeId") String yarnTypeId) {
		
		log.info("Entring getYarnType.");
		
		YarnTypeBean yarnTypeBean = yarnTypeRepo.get(yarnTypeId);
		
//		clientBean.setEntityLblValList(lblValRepo.getAllEntities());
		
		log.info("Exiting getYarnType.");
		
		return yarnTypeBean;
	}

}
