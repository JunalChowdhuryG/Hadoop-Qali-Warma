

FROM ubuntu:20.04

ENV DEBIAN_FRONTEND=noninteractive
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV HADOOP_HOME=/opt/hadoop-2.8.0
ENV HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
ENV PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

RUN apt-get update && apt-get install -y \
    openjdk-8-jdk \
    openssh-server \
    openssh-client \
    curl \
    rsync \
    && rm -rf /var/lib/apt/lists/*

# Descargar e instalar Hadoop 2.8.0
RUN curl -fsSL https://archive.apache.org/dist/hadoop/common/hadoop-2.8.0/hadoop-2.8.0.tar.gz \
        -o /tmp/hadoop.tar.gz \
    && tar -xzf /tmp/hadoop.tar.gz -C /opt \
    && rm /tmp/hadoop.tar.gz

# SSH sin password a localhost (Hadoop lo exige incluso en pseudo-distribuido,
# porque start-dfs.sh / start-yarn.sh arrancan los daemons via ssh)
RUN mkdir -p /root/.ssh \
    && ssh-keygen -t rsa -P '' -f /root/.ssh/id_rsa \
    && cat /root/.ssh/id_rsa.pub >> /root/.ssh/authorized_keys \
    && chmod 0600 /root/.ssh/authorized_keys \
    && printf "Host *\n  StrictHostKeyChecking no\n  UserKnownHostsFile /dev/null\n" > /root/.ssh/config

# Configuracion pseudo-distribuida
COPY cluster/config/core-site.xml   $HADOOP_CONF_DIR/core-site.xml
COPY cluster/config/hdfs-site.xml   $HADOOP_CONF_DIR/hdfs-site.xml
COPY cluster/config/mapred-site.xml $HADOOP_CONF_DIR/mapred-site.xml
COPY cluster/config/yarn-site.xml   $HADOOP_CONF_DIR/yarn-site.xml

RUN echo "export JAVA_HOME=$JAVA_HOME" >> $HADOOP_CONF_DIR/hadoop-env.sh \
    && mkdir -p /hadoop-data/namenode /hadoop-data/datanode


COPY dist/ /jars/
COPY dataQaliWarma.csv /data/dataQaliWarma.csv
COPY cluster/run-query.sh /scripts/run-query.sh
COPY cluster/entrypoint.sh /entrypoint.sh
RUN chmod +x /scripts/run-query.sh /entrypoint.sh

EXPOSE 50070 8088 9000

ENTRYPOINT ["/entrypoint.sh"]
