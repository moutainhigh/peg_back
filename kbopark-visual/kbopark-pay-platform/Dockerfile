FROM pig4cloud/java:8-jre

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /kbopark-pay-platform

WORKDIR /kbopark-pay-platform

EXPOSE 5010

ADD ./target/kbopark-pay-platform.jar ./

CMD sleep 180;java $JAVA_OPTS -jar kbopark-pay-platform.jar
