package com.demo.cache;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class CacheController {
	
//	@Autowired private CacheManager cacheManager;
	
	@GetMapping("/clearCache")
	public String clearCache() {
		
		log.info("Entering clearCache.");
		
		
//		cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());

		
		log.info("Exiting clearCache.");
		
		return "redirect:/home";
	}

}
