
## Postgres
Please find at attached a simple `AsyncListenerWriteToPostgres` that I used to write to my local postgres instance.
Please note that you’ll have to put in your hostname in the file, but that should not be too hard to do.

Also you need to deploy all related jar files with this code. In my case I have 3 jars being deployed: “postgres”,”hikaricp” and the asyncListener.jar.

You can do that with the following command:
```
start server --name server1 --classpath=/Users/udo/projects/AsyncDemo/build/libs/AsyncDemo-1.0-SNAPSHOT.jar:/Users/udo/projects/AsyncDemo/runtime/HikariCP-4.0.3.jar:/Users/udo/projects/AsyncDemo/runtime/postgresql-42.6.0.jar
```

## How to Deploy?
gfsh>stop server --name=server1
Stopping Cache Server running in /home/dmitry/vmware-gemfire-10.0.1/bin/server1 on 172.17.0.1[40411] as server1...
Process ID: 14590
Log File: /home/dmitry/vmware-gemfire-10.0.1/bin/server1/server1.log
.....
gfsh>deploy jar --jars=/home/dmitry/code/TCS/Lib/AsyncDemo-1.0-SNAPSHOT.jar
Option '' is not available for this command. Use tab assist or the "help" command to see the legal options
gfsh>deploy --jars=/home/dmitry/code/TCS/Lib/AsyncDemo-1.0-SNAPSHOT.jar

Deploying files: AsyncDemo-1.0-SNAPSHOT.jar
Total file size is: 0.00MB

Continue?  (Y/n): Y                 
                   
gfsh>start server --name=server1 --server-port=40411 --classpath=/home/dmitry/code/TCS/Lib/AsyncDemo-1.0-SNAPSHOT.jar
Starting a GemFire Server in /home/dmitry/vmware-gemfire-10.0.1/bin/server1...
.....
Server in /home/dmitry/vmware-gemfire-10.0.1/bin/server1 on 172.17.0.1[40411] as server1 is currently online.
Process ID: 59758
Uptime: 4 seconds
Geode Version: 10.0.1
Java Version: 17.0.8.1
Log File: /home/dmitry/vmware-gemfire-10.0.1/bin/server1/server1.log
JVM Arguments: --add-exports=java.management/com.sun.jmx.remote.security=ALL-UNNAMED --add-exports=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED -Dgemfire.default.locators=172.17.0.1[10334] -Dgemfire.start-dev-rest-api=false -Dgemfire.use-cluster-configuration=true -XX:OnOutOfMemoryError=kill -KILL %p -XX:+UseZGC -Dgemfire.launcher.registerSignalHandlers=true -Djava.awt.headless=true -Dsun.rmi.dgc.server.gcInterval=9223372036854775806
Class-Path: /home/dmitry/vmware-gemfire-10.0.1/lib/gemfire-bootstrap-10.0.1.jar


gfsh>create disk-store --name=asyncEventQueue-diskstore --dir=/home/dmitry/vmware-gemfire-10.0.1
Member  | Status | Message
------- | ------ | --------------------------------------------
server1 | OK     | Created disk store asyncEventQueue-diskstore

Cluster configuration for group 'cluster' is updated.


gfsh>create async-event-queue --id=myAsyncQueue1 --listener=org.example.AsyncListener --persistent=true --disk-store=asyncEventQueue-diskstore --batch-size=1
Member  | Status | Message
------- | ------ | -------
server1 | OK     | Success

Cluster configuration for group 'cluster' is updated.

gfsh>list async-event-queue
Member  |      ID       | Batch Size | Persistent |        Disk Store         | Max Memory |         Listener          | Created with paused event processing | Currently Paused
------- | ------------- | ---------- | ---------- | ------------------------- | ---------- | ------------------------- | ------------------------------------ | ----------------
server1 | myAsyncQueue1 | 1        | true       | asyncEventQueue-diskstore | 100        | org.example.AsyncListener | false                                | false

gfsh>list disk-store
Member Name |              Member Id              |      Disk Store Name      | Disk Store ID
----------- | ----------------------------------- | ------------------------- | ------------------------------------
server1     | 172.17.0.1(server1:59758)<v3>:54096 | asyncEventQueue-diskstore | 4c73c6b7-2993-4f76-9f89-4054433ee450

gfsh>create region --name=MyRegion --async-event-queue-id=myAsyncQueue1 --type= …
Member  | Status | Message
------- | ------ | ---------------------------------------
server1 | OK     | Region "/MyRegion" created on "server1"

Cluster configuration for group 'cluster' is updated.


