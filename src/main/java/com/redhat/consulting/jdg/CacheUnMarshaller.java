/**
 * @author Meissa
 */
package com.redhat.consulting.jdg;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.consulting.jdg.model.CacheItem;
import com.redhat.consulting.jdg.model.CacheWrapper;
import com.redhat.consulting.jdg.model.MessageKey;
import com.redhat.consulting.jdg.model.PacsBean;


/**
 * @author Meissa
 */
public class CacheUnMarshaller {
	public static final Logger log = LogManager.getLogger(CacheUnMarshaller.class);
	public static final String BACKUP_LOCATION_FOLDER="/usr/local/dev/workspace/codeready/backup-restore/store/";


	/**
	 * @param args
	 */
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception{
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonInput = BACKUP_LOCATION_FOLDER +"items.json";
		//CacheEntry entry = mapper.readValue(new File(jsonInput), CacheEntry.class);
		
		//List<CacheItem>  items = entry.getItems();
		 List<CacheItem<MessageKey,PacsBean>>  items = mapper.readValue(new FileReader(jsonInput), new TypeReference<List<CacheItem<MessageKey,PacsBean>>>() {});
		 log.info("marshalled cacheData =" +items);
		/*for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			CacheItem cacheItem = (CacheItem) iterator.next();
			MessageKey key = (MessageKey) cacheItem.getKey();
			PacsBean value = (PacsBean) cacheItem.getEntry();
			log.info("key="+key);
			log.info("value="+value);
			
		}*/
		for (CacheItem<MessageKey, PacsBean> cacheItem : items) {
			MessageKey key = cacheItem.getKey();
			PacsBean value = cacheItem.getEntry();
			log.info("key="+key);
			log.info("value="+value);
			
			
		}
		
		
		
		

	}

}
