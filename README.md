# mydubbo
dubbo startup demo

# build and config
mvn install
使用navicat创建数据库test，并依次导入dubbo-consumer\mysql\目录下的：1-table.sql，2-base.sql，3-data.sql

# register
redis-server.exe

# dubbo-provider
mvn test -Dmaven.test.skip=false -Dtest=com.xlongwei.archetypes.dubbo.Provider#provide

mvn assembly:directory
sh bin/startup.sh OR start.bat

# dubbo-consumer
mvn test -Dmaven.test.skip=false -Dtest=com.xlongwei.archetypes.dubbo.facade.IdServiceTester
mvn test -Dmaven.test.skip=false -Dtest=com.xlongwei.archetypes.dubbo.repository.UserRepositoryTester
