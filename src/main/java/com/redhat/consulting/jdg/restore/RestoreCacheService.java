/**
 * @author Meissa
 */
package com.redhat.consulting.jdg.restore;

import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

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
import com.redhat.consulting.jdg.model.CacheItem;
import com.redhat.consulting.jdg.model.MessageKey;
import com.redhat.consulting.jdg.model.PacsBean;

/**
 * @author Meissa
 */

@Singleton
@Startup
public class RestoreCacheService implements RestoreCacheMBean {
	public static final Logger logger = LogManager.getLogger(RestoreCacheService.class.getName());
	private ObjectName serviceName;
	private MBeanServer mbeanServer;
	private EmbeddedCacheManager cacheManager;

	@Override
	public void restoreCache(String cacheName) {
		String jsonFileName = RestoreConfig.RESTORE_LOCATION_FILE;
		processRestore(cacheName,jsonFileName);
		
	}

	@Override
	public void restoreCache(String cacheName, String jsonFileName) {
		processRestore(cacheName,jsonFileName);
		

	}
	
	private void processRestore(String cacheName, String jsonFileName)
	{
		logger.info("restore cache requested for " + cacheName);
		if (cacheManager.getCacheNames().contains(cacheName)) {
			Cache<MessageKey, PacsBean> cache = cacheManager.getCache(RestoreConfig.RESTORE_CACHE_NAME);

			ObjectMapper mapper = new ObjectMapper();
			String jsonInput = jsonFileName;
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
		} else {
			logger.warn("TARGET CACHE " + cacheName + " NOT FOUND. RESTORE CANNOT BE PROCESSED");
		}
		
	}
	
	@PostConstruct
	public void start() {
		try {
			serviceName = new ObjectName("com.redhat.consulting.restore:service=RestoreCacheService");
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mbeanServer = ManagementFactory.getPlatformMBeanServer();
		try {
			mbeanServer.registerMBean(this,  serviceName);
		} catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("*** REGISTERED MBEAN ***"+serviceName.getCanonicalName());
		initilizeCaches();
		
	}
	
	@PreDestroy
	public void stop() {

		try {
			mbeanServer.unregisterMBean(serviceName);
		} catch (MBeanRegistrationException | InstanceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void initilizeCaches()
	{
				
		
		cacheManager = new DefaultCacheManager();
		Configuration conf = new ConfigurationBuilder()
				.build();
		cacheManager.defineConfiguration(RestoreConfig.RESTORE_CACHE_NAME, conf);
		
		
		
	}

}
