/**
 * @author Meissa
 */
package com.redhat.consulting.jdg.backup;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;

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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.redhat.consulting.jdg.model.CacheItem;
import com.redhat.consulting.jdg.model.CacheWrapper;
import com.redhat.consulting.jdg.model.MessageKey;
import com.redhat.consulting.jdg.model.PacsBean;


/**
 * @author Meissa
 * This start up class handle the backup of the cache named "cache" defined from the CacheManger.
 */
@Singleton
@Startup
public class BackupCacheStartup {
	public static final Logger logger = LogManager.getLogger(BackupCacheStartup.class.getName());
	
	@Resource
    private ManagedScheduledExecutorService scheduler;
	private EmbeddedCacheManager cacheManager;
	
	
	
	
	@PostConstruct
	public void init() {
		
		if(BackupConfig.IS_ACTIVE)
		{
		initilizeCaches();
		populateCaches();
		this.scheduler.scheduleAtFixedRate(this::processBackup, BackupConfig.INITIAL_DELAY, BackupConfig.TIME_INTERVAL, TimeUnit.MINUTES);
		}
		else 
		{
			logger.warn( "BACKUP IS NOT ACTIVE ON THIS NODE. NOTHING TO SCHEDULE");
		}
		
		
	}
	
	/**
	 * This method process the backup by iterating over the cache and marshallig the data in the single json file.
	 */
	
	public void processBackup()
	{
		logger.info("*****************  STARTING THE BACKUP PROCESS **********************");
		String jsonOutput = BackupConfig.BACKUP_LOCATION_FOLDER +BackupConfig.BACKUP_FILE_NAME;
		Cache<MessageKey, PacsBean> cache = cacheManager.getCache(BackupConfig.BACKUP_CACHE_NAME);

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

	}
	
	@PreDestroy
	public void cleanup() {
		
		//cacheManager.stop();
		
	}
	
	/**
	 * This method only serves as a test to inject data onto the cache
	 */
	private void initilizeCaches()
	{
	
		cacheManager = new DefaultCacheManager();
		Configuration conf = new ConfigurationBuilder()
				.build();
		cacheManager.defineConfiguration(BackupConfig.BACKUP_CACHE_NAME, conf);
		
		
	}
	
	/**
	 *  This method only serves as a test to populate some data into the cache.
	 */
	private void populateCaches()
	{
		Cache<MessageKey,PacsBean> cache= cacheManager.getCache(BackupConfig.BACKUP_CACHE_NAME);
		 for (int i = 0; i < 10; i++) {
	           
	            PacsBean pacs =new PacsBean("msg"+i,"00"+i,"instr"+i,true,new BigDecimal(123000.00));
	            MessageKey key =new MessageKey("id"+i,"code"+i);
	            cache.put(key, pacs);
	            
	         }
				
		
	}

}
