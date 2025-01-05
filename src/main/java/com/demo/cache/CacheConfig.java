//package com.demo.cache;
//
//import org.ehcache.CacheManager;
//import org.ehcache.config.builders.CacheManagerBuilder;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.jcache.JCacheCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableCaching
//public class CacheConfig {
//	
//	@Bean
//	public CacheManager cacheManager() {
//		
//		CacheManager ehCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
//				.build(true);
//		
//		return ehCacheManager;
//	}
//	
//	@Bean
//	public JCacheCacheManager jCacheCacheManager() {
//		JCacheCacheManager jCacheManager = new JCacheCacheManager();
//		return jCacheManager;
//	}
//
//}
