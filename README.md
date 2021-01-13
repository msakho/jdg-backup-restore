# jdg-backup-restore
This project demonstrates how to perform a backup of an Infinispan Cache to a file or a restore of an Infinispan Cache from a file.
# Architecture
The approach that is defined for the backup is to iterate through the cache for a given node and create a single dump file.
The restore appoach is to load a dump file on any node.
The marshalling and unmarshalling process of the cached datas relies on the [Jackson databinding framework](https://github.com/FasterXML/jackson-docs) 
The backup and restore exists in two flavor:
1) A scheduled Startup class the process the backup given a scheduled configuration.
2) A JMX MBean that offers two operations to process the backup.
3) A scheduled Startup class the process the restore given a scheduled configuration.
4) A JMX Mbean that offers two operations to process the restore.

A CacheItem Object is designed to encapsulate the Cache Key and the Cache Value.
This gives us the flexibility to define custom typed keys and values.
A messageKey and a PacsBean object are used respectively for keys and values.
Finally a CacheWrapper is designed to encasulate the cache data.
All these object are defined in respect of jackson databinding framework. 
That gives us to possibility to marshall the cache into json and to load them back in a cache as desired.

```
public class CacheItem<K,V> implements Serializable {
	private K key;
	private V entry;

```


# Instructions

## Build the project

```
mvn clean install
```

## Start Jboss instance
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
## deploy the application

```
mvn wildfly:deploy
```
