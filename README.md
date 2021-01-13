# jdg-backup-restore
This project demonstrates how to perform a backup of an Infinispan Cache to a file or a restore of an Infinispan Cache from a file.
# Architecture
The approach that is defined for the backup is to iterate through the cache for a given node and create a single dump file.
The restore appoach is to load a dump file on any node.
The marshalling and unmarshalling process of the cached datas relies on the Jackson databinding framework.


