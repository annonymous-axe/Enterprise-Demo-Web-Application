package com.demo.client;

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
public class ClientController {
	
	@Autowired
	private ClientRepository clientRepo;
	@Autowired private LabelValueRepository lblValRepo;
	
	@GetMapping("clientView")
	public String clientViewPage(Model model) {
		
		List<ClientBean> clientBeanList = clientRepo.list();
		
		log.info("clientBeanList :: "+clientBeanList);
		
		model.addAttribute("clientBeanList", clientBeanList);
		
		return "/pages/clientView";
	}
	
	@GetMapping("createClient")
	public String createClient(Model model) {
		
		ClientBean clientBean = new ClientBean();
		
		clientBean.setCountryLblValList(lblValRepo.getAllCounties());
		clientBean.setEntityLblValList(lblValRepo.getAllEntities());
		
		try {
	    	ArrayList<String> clientList = lblValRepo.getClientNameLabelValues();
	    	log.info("clientList ::"+clientList);
	    	model.addAttribute("clientList", clientList);
		}catch(Exception e) {
			log.info("Exception :: "+e);
		}
		
		model.addAttribute("client", clientBean);
		model.addAttribute("clientID", "");
		
		return "/pages/client";
	}
	
	@PostMapping(value = {"/clientSave", "/clientApprove", "/clientUpdate", "/clientDecline", "/clientDelete"})
	public String progressClient(ClientBean clientBean, @ModelAttribute("btnAction") String btnAction, Model model) {
		
		log.info("Entring progressClient.");
		
		log.info("Client :: "+clientBean.getClientId()+", action :: "+btnAction);
		
		if(btnAction.equalsIgnoreCase("create")) {
			clientRepo.save(clientBean);
		}else if(btnAction.equalsIgnoreCase("approve")) {
			clientRepo.authorize(clientBean);
		}else if(btnAction.equalsIgnoreCase("update")) {
			clientRepo.update(clientBean);
		}else if(btnAction.equalsIgnoreCase("decline")) {
			clientRepo.decline(clientBean);
		}else if(btnAction.equalsIgnoreCase("delete")) {
			clientRepo.delete(clientBean);
		}
		
		log.info("Exiting progressClient.");
		
		return "redirect:/clientView";
	}
	
	@PostMapping("getClient")
	public String getClient(@ModelAttribute("formData") String formData, Model model) {
		
		log.info("Entring getClient.");
		
		ClientBean clientBean = clientRepo.get(formData);
		
		clientBean.setEntityLblValList(lblValRepo.getAllEntities());
		
		model.addAttribute("client", clientBean);
		model.addAttribute("clientID", clientBean.getClientId());
		
		log.info("Exiting getClient.");
		
		return "/pages/client";
	}
	
	@ResponseBody
	@PostMapping("/getStates")
	public ArrayList<LabelValue> getStatesFromCountry(@RequestParam String countryCode){
		
		ArrayList<LabelValue> states = null;
		
		states = lblValRepo.getAllStates(countryCode);
		
		return states;
	}
	
	@ResponseBody
	@PostMapping("/getCities")
	public ArrayList<LabelValue> getCitiesFromState(@RequestParam String stateCode){
		
		ArrayList<LabelValue> states = null;
		
		states = lblValRepo.getAllCities(stateCode);
		
		return states;
	}	

}
