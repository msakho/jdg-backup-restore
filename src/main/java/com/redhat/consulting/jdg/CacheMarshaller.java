/**
 * @author Meissa
 */
package com.redhat.consulting.jdg;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.redhat.consulting.jdg.model.CacheItem;
import com.redhat.consulting.jdg.model.CacheWrapper;
import com.redhat.consulting.jdg.model.MessageKey;
import com.redhat.consulting.jdg.model.PacsBean;

/**
 * @author Meissa
 */
public class CacheMarshaller {
	public static final Logger log = LogManager.getLogger(CacheMarshaller.class);
	public static final String BACKUP_LOCATION_FOLDER="/usr/local/dev/workspace/codeready/backup-restore/store/";

	/**
	 * @param args
	 */
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		ObjectMapper mapper= new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		PacsBean pacs =createPacs();
		
		mapper.writeValue(new File("/usr/local/dev/workspace/codeready/backup-restore/store/pacs.json"), pacs);
		
		CacheItem<MessageKey, PacsBean> item = createCacheItem();
		mapper.writeValue(new File(BACKUP_LOCATION_FOLDER+"item.json"), item);
				
		ArrayList<CacheItem> data =new ArrayList<CacheItem>();
		data.add(item);
		data.add(item);
		CacheWrapper<CacheItem> cacheWrapper = new CacheWrapper<CacheItem>();
		cacheWrapper.setData(data);
		
		mapper.writeValue(new File(BACKUP_LOCATION_FOLDER+"cachedata.json"), cacheWrapper.getData());
		
		
		
		
	}
	
	public static PacsBean createPacs()
	{
		PacsBean pacs =new PacsBean();
		pacs.setAmount(new BigDecimal(123000.00));
		pacs.setTimeoutManagement(true);
		pacs.setTransactionId("001");
		pacs.setInstructionId("instr001");
		pacs.setMsgId("msg01");
		return pacs;
	}
	
	public static MessageKey createKey()
	{
		MessageKey key =new MessageKey("id1","code1");
		return key;
	}
	
	public static CacheItem<MessageKey, PacsBean> createCacheItem()
	{
		MessageKey key = createKey();
		PacsBean entry =createPacs();
		CacheItem<MessageKey,PacsBean> cacheItem= new CacheItem<MessageKey, PacsBean>(key,entry);
		
		return cacheItem;
	}
	


}
