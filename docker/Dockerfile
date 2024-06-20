FROM openjdk:8
VOLUME ["/tmp","/sharedata"]
ENV TZ=Asia/Shanghai
ENV LANG=C.UTF-8
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ADD md4x-0.0.1-SNAPSHOT.jar  md4x.jar
#如果基础镜像是java8，需要把这两个jar加到docker路径下覆盖原来的jar，镜像名必须为小写，否则出错
#ADD US_export_policy.jar  $JAVA_HOME/jre/lib/security
#ADD local_policy.jar  $JAVA_HOME/jre/lib/security
ENTRYPOINT ["java","-jar","/md4x.jar"]