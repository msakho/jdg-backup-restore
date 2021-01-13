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


