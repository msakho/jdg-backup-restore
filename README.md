# jdg-backup-restore
This project demonstrates how to perform a backup of an Infinispan Cache to a file or a restore of an Infinispan Cache from a file.
## Table of contents
* [Architecture](#Architecture)
* [Design](#Design)
* [Deployment](#Deployment)
## Architecture
The approach that is defined for the backup is to iterate through the cache for a given node and create a single dump file.
The restore approach is to load a dump file on any node on demand. 
The marshalling and unmarshalling process of the cached datas relies on the [Jackson databinding framework](https://github.com/FasterXML/jackson-docs) 
The backup and restore exists in multiple flavors:
1) A scheduled Startup class to process the backup given a scheduled configuration.
2) A JMX MBean that offers two operations to process the backup on demand.
3) A scheduled Startup class to process the restore given a scheduled configuration.
4) A JMX Mbean that offers two operations to process the restore on demand.

## Design

In order to process the backup by dumping the cache data in a file, the infinispan cached datas will have to be read and encapsulated to java suitable java objects before being marshalled to json files. 
The same logic applies for the restore back to a cache from a json file. The json input will have to be marshalled back to java objects that will correspond to keys and value pairs, representing a cache entry.
In order to fulfill these two statements, the following objects have been designed:
1) A CacheItem Object is designed to encapsulate the Cache Key and the Cache Value. This gives us the flexibility to define custom typed keys and values. 
2) A messageKey and a PacsBean object are used respectively for keys and values. 
3) Finally a CacheWrapper is designed to encapsulate the cache data. 
All these objects are defined in respect of jackson data binding framework. That gives us the possibility to marshall the cache into json and to load them back in a cache as desired. The extract of the differents object definition are described below:


The extract of the differents object definition are described below:

The CacheItem object that represent the generic key, value pair.

```
public class CacheItem<K,V> implements Serializable {
	private K key;
	private V entry;

```
The CacheWrapper Object that handle a list of CacheItem.
```
public class CacheWrapper<CacheItem> implements Serializable {	
	private List<CacheItem> data;
```
The MessageKey object which is an Example of key object.
```
public class MessageKey implements Serializable {

	private String id;
	private String bicCode;
```
The PacsBean object which represents an Example of value.
```
public class PacsBean implements Serializable {
	
	private String msgId;
	private String transactionId;
	private String instructionId;
	boolean timeoutManagement;
	private BigDecimal amount;
```
An example of usage of the above objects might look likes the following snipped:
```
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
```
The jackson invocation to generate the dump from the cache look like the following instrcution:
```
mapper.writeValue(new File(jsonOutput), cacheWrapper.getData());
```

## Deployment

### Build the project

```
mvn clean install
```

### Start Jboss instance
The jboss must be started with the system properties that you want to define for both services.


```
./standalone.sh -P /usr/local/dev/eap/jboss-eap-7.3/standalone/configuration/backup-restore-config.properties
```

A complete of the expected properties are defined below. 
```
jdg.backup.location.folder=/usr/local/dev/workspace/codeready/backup-restore/store/
jdg.backup.time.interval=120
jdg.backup.initial.delay=1
jdg.backup.active=false
jdg.backup.file.name=cachedata.json
jdg.backup.cache.name=cache

jdg.restore.cache.name=restoredCache
jdg.restore.location.file=/usr/local/dev/workspace/codeready/backup-restore/store/cachedata.json
jdg.restore.active=false
jdg.restore.initial.delay=2
```
### deploy the application

```
mvn wildfly:deploy
```
