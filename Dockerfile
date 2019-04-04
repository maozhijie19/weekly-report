#指定基础镜像，在其上进行定制
FROM openjdk:8-jre-alpine
#维护者信息
MAINTAINER liuzhuoming <liuzhuoming23@live.com>
# 设置时区为上海
RUN apk add tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata
#这里的/tmp目录就会在运行时自动挂载为匿名卷，任何向/tmp中写入的信息都不会记录进容器存储层
VOLUME /tmp
#复制上下文目录下的target/weekly-report.jar到容器里
COPY target/weekly-report.jar weekly-report.jar
#声明运行时容器提供服务端口，这只是一个声明，在运行时并不会因为这个声明应用就会开启这个端口的服务
EXPOSE 8076
#指定容器启动程序及参数<ENTRYPOINT><CMD>
ENTRYPOINT ["java","-jar","weekly-report.jar"]
