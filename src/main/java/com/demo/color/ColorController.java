package com.demo.color;

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
public class ColorController {
	
	@Autowired
	private ColorRepository colorRepo;
	@Autowired private LabelValueRepository lblValRepo;
	
	@GetMapping("colorView")
	public String clientViewPage(Model model) {
		
		List<ColorBean> colorBeanList = colorRepo.list();
		
		log.info("colorBeanList :: "+colorBeanList);
		
		model.addAttribute("colorBeanList", colorBeanList);
		
		return "/pages/colorView";
	}
	
	@GetMapping("createColor")
	public String createClient(Model model) {
		
		ColorBean clientBean = new ColorBean();
		
		try {
	    	ArrayList<String> clientList = lblValRepo.getClientNameLabelValues();
	    	log.info("clientList ::"+clientList);
	    	model.addAttribute("clientList", clientList);
		}catch(Exception e) {
			log.info("Exception :: "+e);
		}
		
		model.addAttribute("client", clientBean);
		model.addAttribute("clientID", "");
		
		return "/pages/colorView";
	}
	
	@PostMapping(value = {"/colorSave", "/colorApprove", "/colorUpdate", "/colorDecline", "/colorDelete"})
	public String progressClient(@ModelAttribute("formData") String formData, @ModelAttribute("btnAction") String btnAction, Model model) {
		
		log.info("Entring progressClient.");
		
		log.info("formData :: "+formData);
		
		if(btnAction.equalsIgnoreCase("create")) {
			colorRepo.save(formData);
		}else if(btnAction.equalsIgnoreCase("delete")) {
			ColorBean colorBean = colorRepo.get(formData);
			colorRepo.delete(colorBean);
		}
		
		log.info("Exiting progressClient.");
		
		return "redirect:/colorView";
	}
	
	@ResponseBody
	@PostMapping("getColor")
	public ColorBean getClient(@ModelAttribute("colorId") String colorId) {
		
		log.info("Entring getClient.");
		
		ColorBean colorBean = colorRepo.get(colorId);
		
//		clientBean.setEntityLblValList(lblValRepo.getAllEntities());
		
		log.info("Exiting getClient.");
		
		return colorBean;
	}

}
