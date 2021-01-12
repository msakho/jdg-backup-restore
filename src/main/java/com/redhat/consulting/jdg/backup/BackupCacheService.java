/**
 * @author Meissa
 */
package com.redhat.consulting.jdg.backup;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infinispan.Cache;
import org.infinispan.CacheSet;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.context.Flag;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.redhat.consulting.jdg.model.CacheItem;
import com.redhat.consulting.jdg.model.CacheWrapper;
import com.redhat.consulting.jdg.model.MessageKey;
import com.redhat.consulting.jdg.model.PacsBean;

/**
 * @author Meissa
 */
@Singleton
@Startup
public class BackupCacheService implements BackupCacheMBean {
	public static final Logger logger = LogManager.getLogger(BackupCacheService.class.getName());
	private EmbeddedCacheManager cacheManager;
	private ObjectName serviceName;
	private MBeanServer mbeanServer;

	@Override
	public void backupCache(String cacheName) {
		processBackup(cacheName,BackupConfig.BACKUP_LOCATION_FOLDER);

	}

	@Override
	public void backupCache(String cacheName, String backupLocation) {
		processBackup(cacheName,backupLocation);

	}
	
	@PostConstruct
	public void init()
	{
		try {
			serviceName = new ObjectName("com.redhat.consulting.backup:service=BackupCacheService");
		} catch (MalformedObjectNameException e) {
			
			e.printStackTrace();
		}
		mbeanServer = ManagementFactory.getPlatformMBeanServer();
		try {
			mbeanServer.registerMBean(this,  serviceName);
		} catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
			
			e.printStackTrace();
		}
		
		logger.info("*** REGISTERED MBEAN ***"+serviceName.getCanonicalName());
		initilizeCaches();
		
	}
	
	private void initilizeCaches()
	{
	
		
		cacheManager = new DefaultCacheManager();
		Configuration conf = new ConfigurationBuilder()
				.build();
		cacheManager.defineConfiguration(BackupConfig.BACKUP_CACHE_NAME, conf);
		populateCaches();
		
	}
	
	private void processBackup(String cacheName, String backupLocation)
	{
		logger.info("backup cache requested for " + cacheName +" to "+backupLocation);
		String jsonOutput = backupLocation +BackupConfig.BACKUP_FILE_NAME;
		if (cacheManager.getCacheNames().contains(cacheName))
		{
			logger.info("*****************  STARTING THE BACKUP PROCESS **********************");
			Cache<MessageKey, PacsBean> cache = cacheManager.getCache(cacheName);

			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			ArrayList<CacheItem<MessageKey, PacsBean>> data = new ArrayList<CacheItem<MessageKey, PacsBean>>();

			CacheSet<Entry<MessageKey, PacsBean>> entries = cache.getAdvancedCache().withFlags(Flag.SKIP_REMOTE_LOOKUP)
					.entrySet();

			for (Entry<MessageKey, PacsBean> entry : entries) {
				CacheItem<MessageKey, PacsBean> item = new CacheItem<MessageKey, PacsBean>();
				MessageKey key = entry.getKey();
				PacsBean value = entry.getValue();
				item.setEntry(value);
				item.setKey(key);
				data.add(item);

			}
			CacheWrapper<CacheItem<MessageKey, PacsBean>> cacheWrapper = new CacheWrapper<CacheItem<MessageKey, PacsBean>>();
			cacheWrapper.setData(data);
			logger.info("**** BACKUP FILE LOCATION=" + jsonOutput);

			try {
				mapper.writeValue(new File(jsonOutput), cacheWrapper.getData());
			} catch (IOException e) {
				
				e.printStackTrace();
			}

			logger.info("*************** BACKUP FROM CACHE COMPLETED *********");
			
		} else
		{
			logger.warn("TARGET CACHE " + cacheName + " NOT FOUND. BACKUP CANNOT BE PROCESSED");
		}
		
	}
	
	private void populateCaches()
	{
		Cache<MessageKey,PacsBean> cache= cacheManager.getCache(BackupConfig.BACKUP_CACHE_NAME);
		 for (int i = 0; i < 5; i++) {
	           
	            PacsBean pacs =new PacsBean("msg"+i,"00"+i,"instr"+i,true,new BigDecimal(123000.00));
	            MessageKey key =new MessageKey("id"+i,"code"+i);
	 
	            cache.put(key, pacs);
	            
	         }
				
		
	}

}
