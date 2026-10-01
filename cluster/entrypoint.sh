#!/bin/bash
set -e

service ssh start

if [ ! -d "/hadoop-data/namenode/current" ]; then
    echo ">>> Formateando NameNode (primera vez)..."
    $HADOOP_HOME/bin/hdfs namenode -format -force
fi

echo ">>> Iniciando HDFS..."
$HADOOP_HOME/sbin/start-dfs.sh

echo ">>> Iniciando YARN..."
$HADOOP_HOME/sbin/start-yarn.sh

echo ""
echo "================================================================"
echo "Cluster Hadoop 2.8.0 pseudo-distribuido listo."
echo "  NameNode UI:        http://localhost:50070"
echo "  ResourceManager UI: http://localhost:8088"
echo "================================================================"
echo ""

# Mantener el contenedor vivo
tail -f /dev/null
