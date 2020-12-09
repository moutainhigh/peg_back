FROM mysql:8.0.20

MAINTAINER kbopark(wangiegie@gmail.com)

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./1schema.sql /docker-entrypoint-initdb.d

COPY ./2kboparkx.sql /docker-entrypoint-initdb.d

COPY ./3kboparkx_ac.sql /docker-entrypoint-initdb.d

COPY ./4kboparkx_job.sql /docker-entrypoint-initdb.d

COPY ./5kboparkx_mp.sql /docker-entrypoint-initdb.d

COPY ./6kboparkx_config.sql /docker-entrypoint-initdb.d

COPY ./7kboparkx_pay.sql /docker-entrypoint-initdb.d

COPY ./8kboparkx_codegen.sql /docker-entrypoint-initdb.d