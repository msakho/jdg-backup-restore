/**
 * @author Meissa
 */
package com.redhat.consulting.jdg;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infinispan.cdi.embedded.ConfigureCache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.VersioningScheme;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * @author Meissa
 */
public class CacheManagerProducer {
	public static final Logger logger = LogManager.getLogger(CacheManagerProducer.class.getName());
	private EmbeddedCacheManager cacheManager;
	
	
	
	
	
	@Produces
	@ApplicationScoped
	public EmbeddedCacheManager defaultClusteredCacheManager()
	{
				
		
		GlobalConfiguration glob = new GlobalConfigurationBuilder()
				.clusteredDefault()					
				.build(); // Builds the GlobalConfiguration object
	
		Configuration conf = new ConfigurationBuilder().clustering()
				.cacheMode(CacheMode.DIST_SYNC)
				.build();
		cacheManager = new DefaultCacheManager(glob);
		cacheManager.defineConfiguration("cache", conf);
		
		cacheManager.defineConfiguration("firstToMigrate", conf);
		
		
		return cacheManager;
		
		
	}


}
