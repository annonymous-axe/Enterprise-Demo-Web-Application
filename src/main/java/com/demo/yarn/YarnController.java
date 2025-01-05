package com.demo.yarn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.demo.labelValue.LabelValue;
import com.demo.labelValue.LabelValueRepository;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class YarnController {
	
	@Autowired
	private YarnRepository yarnRepo;
	@Autowired private LabelValueRepository lblValRepo;
	
	@GetMapping("yarnView")
	public String yarnViewPage(Model model) {
		
		List<YarnBean> yarnBeanList = yarnRepo.list();
		
		log.info("yarnBeanList :: "+yarnBeanList);
		
		model.addAttribute("yarnBeanList", yarnBeanList);
		
		return "/pages/yarnView";
	}
	
	@GetMapping("createYarn")
	public String createYarn(Model model) {
		
		YarnBean yarnBean = new YarnBean();
		
		yarnBean.setEntityLblValList(lblValRepo.getAllEntities());
		yarnBean.setYarnTypeLblValList(lblValRepo.getAllYarnTypes());
		
		ArrayList<ItemDetailBean> itemDetailList = new ArrayList<>();
		for(LabelValue colorList : lblValRepo.getAllColorList()) {
			ItemDetailBean itemDetailBean = new ItemDetailBean();
			itemDetailBean.setColorId(colorList.getValue());
			itemDetailBean.setColorName(colorList.getLabel());
			itemDetailList.add(itemDetailBean);
		}
		yarnBean.setItemDetailBeanList(itemDetailList);
//		try {
//	    	ArrayList<String> yarnList = lblValRepo.getYarnNameLabelValues();
//	    	log.info("yarnList ::"+yarnList);
//	    	model.addAttribute("yarnList", yarnList);
//		}catch(Exception e) {
//			log.info("Exception :: "+e);
//		}
		
		model.addAttribute("yarn", yarnBean);
		model.addAttribute("yarnID", "");
		
		return "/pages/yarn";
	}
	
	@PostMapping(value = {"/yarnSave", "/yarnApprove", "/yarnUpdate", "/yarnDecline", "/yarnDelete"})
	public String progressYarn(YarnBean yarnBean, @ModelAttribute("btnAction") String btnAction, Model model) {
		
		log.info("Entring progressYarn.");
		
		log.info("Yarn :: "+yarnBean.getYarnId()+", action :: "+btnAction);
		
		if(btnAction.equalsIgnoreCase("create")) {
			yarnRepo.save(yarnBean);
		}else if(btnAction.equalsIgnoreCase("approve")) {
			yarnRepo.authorize(yarnBean);
		}else if(btnAction.equalsIgnoreCase("update")) {
			yarnRepo.update(yarnBean);
		}else if(btnAction.equalsIgnoreCase("decline")) {
			yarnRepo.decline(yarnBean);
		}else if(btnAction.equalsIgnoreCase("delete")) {
			yarnRepo.delete(yarnBean);
		}
		
		log.info("Exiting progressYarn.");
		
		return "redirect:/yarnView";
	}
	
	@PostMapping("getYarn")
	public String getYarn(@ModelAttribute("formData") String formData, Model model) {
		
		log.info("Entring getYarn.");
		
		YarnBean yarnBean = yarnRepo.get(formData);
		
		yarnBean.setEntityLblValList(lblValRepo.getAllEntities());
		yarnBean.setYarnTypeLblValList(lblValRepo.getAllYarnTypes());
		
		model.addAttribute("yarn", yarnBean);
		model.addAttribute("yarnID", yarnBean.getYarnId());
		
		log.info("Exiting getYarn.");
		
		return "/pages/yarn";
	}

}
