/**
 * @author Meissa
 */
package com.redhat.consulting.jdg.restore;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.consulting.jdg.backup.BackupConfig;
import com.redhat.consulting.jdg.model.CacheItem;
import com.redhat.consulting.jdg.model.CacheWrapper;
import com.redhat.consulting.jdg.model.MessageKey;
import com.redhat.consulting.jdg.model.PacsBean;

/**
 * @author Meissa
 */
@Singleton
@Startup
public class RestoreCacheStartup {
	public static final Logger logger = LogManager.getLogger(RestoreCacheStartup.class.getName());
	private EmbeddedCacheManager cacheManager;
	@Resource
    private ManagedScheduledExecutorService scheduler;
	
	@PostConstruct
	public void init()
	{
		
		if(RestoreConfig.IS_ACTIVE)
		{
		
		initilizeCaches();
		processRestore();
		this.scheduler.schedule(this::processRestore, RestoreConfig.INITIAL_DELAY, TimeUnit.MINUTES);
		}
		else
		{
			logger.warn(" **** RESTORE IS NOT ACTIVE ON THIS NODE. NOTHING TO SCHEDULE ****");
		}
		

	}
	
	@PreDestroy
	public void cleanup()
	{
		
		//cacheManager.stop();
		
		
	}
	
	
	private void initilizeCaches()
	{
						
		cacheManager = new DefaultCacheManager();
		Configuration conf = new ConfigurationBuilder()
				.build();
		cacheManager.defineConfiguration(RestoreConfig.RESTORE_CACHE_NAME, conf);
		
		
	}
	
	private void processRestore()
	{
		Cache<MessageKey, PacsBean> cache = cacheManager.getCache(RestoreConfig.RESTORE_CACHE_NAME);

		ObjectMapper mapper = new ObjectMapper();
		String jsonInput = RestoreConfig.RESTORE_LOCATION_FILE;
		List<CacheItem<MessageKey, PacsBean>> cacheData = new ArrayList<CacheItem<MessageKey, PacsBean>>();
		try {
			cacheData = mapper.readValue(new FileReader(jsonInput),
					new TypeReference<List<CacheItem<MessageKey, PacsBean>>>() {
					});
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}

		for (CacheItem<MessageKey, PacsBean> cacheItem : cacheData) {
			MessageKey key = cacheItem.getKey();
			PacsBean value = cacheItem.getEntry();

			cache.put(key, value);

		}
		
		
		
		logger.info("*** RESTORED DATA INTO CACHE*** =" +cache.size());
		
	}

}
