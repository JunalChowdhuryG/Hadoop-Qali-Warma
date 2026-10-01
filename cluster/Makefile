# Makefile del CLUSTER HADOOP (distinto del Makefile de la raiz, que solo
# compila los .java a .jar). Este orquesta un Hadoop 2.8.0 real corriendo
# en Docker, usando los jars que ya estan en ../dist/.
#
# Uso tipico:
#   make up          -> construye la imagen y levanta el cluster
#   make load-data    -> sube el dataset a HDFS (una sola vez)
#   make run QUERY=9 -> corre la Consulta 9 y muestra el resultado
#   make down         -> apaga el cluster

QUERY ?= 1

.PHONY: up down load-data run shell logs status

up:
	docker compose up -d --build
	@echo "Esperando a que HDFS/YARN terminen de iniciar (20s)..."
	@sleep 20
	@echo "Listo. NameNode: http://localhost:50070  ResourceManager: http://localhost:8088"

down:
	docker compose down

load-data:
	docker compose exec hadoop hadoop fs -mkdir -p /input_dir
	docker compose exec hadoop hadoop fs -put -f /data/dataQaliWarma.csv /input_dir/dataQaliWarma.csv
	docker compose exec hadoop hadoop fs -ls /input_dir/

run:
	docker compose exec hadoop bash /scripts/run-query.sh $(QUERY)

shell:
	docker compose exec hadoop bash

logs:
	docker compose logs -f

status:
	docker compose exec hadoop jps
