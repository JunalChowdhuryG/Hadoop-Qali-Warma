SRC_DIR   := src
BUILD_DIR := build
DIST_DIR  := dist
LIB_DIR   := lib

# Classpath con todos los jars de Hadoop que pongas en lib/
HADOOP_CP := $(shell echo $(LIB_DIR)/*.jar | tr ' ' ':')

CONSULTAS_GRUPO1 := 1 2 3 4 5
CONSULTAS_GRUPO2 := 6
CONSULTAS_GRUPO3 := 7
CONSULTAS_GRUPO4 := 8
CONSULTAS_GRUPO5 := 9 10
CONSULTAS_GRUPO6 := 11 12
CONSULTAS_GRUPO7 := 13 14

.PHONY: all clean compile jars

all: jars

compile:
	mkdir -p $(BUILD_DIR)
	javac -classpath "$(HADOOP_CP)" -d $(BUILD_DIR) $(SRC_DIR)/grupo1/*.java $(SRC_DIR)/grupo2/*.java $(SRC_DIR)/grupo3/*.java $(SRC_DIR)/grupo4/*.java $(SRC_DIR)/grupo5/*.java $(SRC_DIR)/grupo6/*.java $(SRC_DIR)/grupo7/*.java


jars: compile
	mkdir -p $(DIST_DIR)
	@for n in $(CONSULTAS_GRUPO1); do \
		echo "==> Empaquetando Consulta$$n.jar (grupo1)"; \
		rm -rf tmp_$$n; \
		mkdir -p tmp_$$n/grupo1; \
		cp $(BUILD_DIR)/grupo1/Consulta$${n}Driver.class  tmp_$$n/grupo1/; \
		cp $(BUILD_DIR)/grupo1/Consulta$${n}Mapper.class  tmp_$$n/grupo1/; \
		cp $(BUILD_DIR)/grupo1/SumReducer.class           tmp_$$n/grupo1/; \
		( cd tmp_$$n && jar -cvf ../$(DIST_DIR)/Consulta$$n.jar grupo1 ); \
		rm -rf tmp_$$n; \
	done
	@for n in $(CONSULTAS_GRUPO2); do \
		echo "==> Empaquetando Consulta$$n.jar (grupo2)"; \
		rm -rf tmp_$$n; \
		mkdir -p tmp_$$n/grupo2; \
		cp $(BUILD_DIR)/grupo2/Consulta$${n}Driver.class   tmp_$$n/grupo2/; \
		cp $(BUILD_DIR)/grupo2/Consulta$${n}Mapper.class   tmp_$$n/grupo2/; \
		cp $(BUILD_DIR)/grupo2/Consulta$${n}Reducer.class  tmp_$$n/grupo2/; \
		( cd tmp_$$n && jar -cvf ../$(DIST_DIR)/Consulta$$n.jar grupo2 ); \
		rm -rf tmp_$$n; \
	done
	@for n in $(CONSULTAS_GRUPO3); do \
		echo "==> Empaquetando Consulta$$n.jar (grupo3)"; \
		rm -rf tmp_$$n; \
		mkdir -p tmp_$$n/grupo3; \
		cp $(BUILD_DIR)/grupo3/Consulta$${n}Driver.class   tmp_$$n/grupo3/; \
		cp $(BUILD_DIR)/grupo3/Consulta$${n}Mapper.class   tmp_$$n/grupo3/; \
		cp $(BUILD_DIR)/grupo3/Consulta$${n}Reducer.class  tmp_$$n/grupo3/; \
		( cd tmp_$$n && jar -cvf ../$(DIST_DIR)/Consulta$$n.jar grupo3 ); \
		rm -rf tmp_$$n; \
	done
	@for n in $(CONSULTAS_GRUPO4); do \
		echo "==> Empaquetando Consulta$$n.jar (grupo4)"; \
		rm -rf tmp_$$n; \
		mkdir -p tmp_$$n/grupo4; \
		cp $(BUILD_DIR)/grupo4/Consulta$${n}Driver.class   tmp_$$n/grupo4/; \
		cp $(BUILD_DIR)/grupo4/Consulta$${n}Mapper.class   tmp_$$n/grupo4/; \
		cp $(BUILD_DIR)/grupo4/Consulta$${n}Reducer.class  tmp_$$n/grupo4/; \
		( cd tmp_$$n && jar -cvf ../$(DIST_DIR)/Consulta$$n.jar grupo4 ); \
		rm -rf tmp_$$n; \
	done
	@for n in $(CONSULTAS_GRUPO5); do \
		echo "==> Empaquetando Consulta$$n.jar (grupo5, MapReduce encadenado)"; \
		rm -rf tmp_$$n; \
		mkdir -p tmp_$$n/grupo5; \
		cp $(BUILD_DIR)/grupo5/Consulta$${n}Driver.class     tmp_$$n/grupo5/; \
		cp $(BUILD_DIR)/grupo5/Consulta$${n}Job1Mapper.class tmp_$$n/grupo5/; \
		cp $(BUILD_DIR)/grupo5/Consulta$${n}Job2Mapper.class tmp_$$n/grupo5/; \
		cp $(BUILD_DIR)/grupo5/SumReducer.class              tmp_$$n/grupo5/; \
		cp $(BUILD_DIR)/grupo5/PropZonaReducer.class         tmp_$$n/grupo5/; \
		( cd tmp_$$n && jar -cvf ../$(DIST_DIR)/Consulta$$n.jar grupo5 ); \
		rm -rf tmp_$$n; \
	done
	@for n in $(CONSULTAS_GRUPO6); do \
		echo "==> Empaquetando Consulta$$n.jar (grupo6, gradiente iterativo)"; \
		rm -rf tmp_$$n; \
		mkdir -p tmp_$$n/grupo6; \
		cp $(BUILD_DIR)/grupo6/Consulta$${n}Driver.class  tmp_$$n/grupo6/; \
		cp $(BUILD_DIR)/grupo6/Consulta$${n}Mapper.class  tmp_$$n/grupo6/; \
		cp $(BUILD_DIR)/grupo6/FeatureEncoder.class       tmp_$$n/grupo6/; \
		cp $(BUILD_DIR)/grupo6/SumDoubleReducer.class     tmp_$$n/grupo6/; \
		( cd tmp_$$n && jar -cvf ../$(DIST_DIR)/Consulta$$n.jar grupo6 ); \
		rm -rf tmp_$$n; \
	done
	@for n in $(CONSULTAS_GRUPO7); do \
		echo "==> Empaquetando Consulta$$n.jar (grupo7, regresion lineal)"; \
		rm -rf tmp_$$n; \
		mkdir -p tmp_$$n/grupo7; \
		cp $(BUILD_DIR)/grupo7/Consulta$${n}Driver.class  tmp_$$n/grupo7/; \
		cp $(BUILD_DIR)/grupo7/Consulta$${n}Mapper.class  tmp_$$n/grupo7/; \
		cp $(BUILD_DIR)/grupo7/FeatureEncoder.class       tmp_$$n/grupo7/; \
		cp $(BUILD_DIR)/grupo7/SumDoubleReducer.class     tmp_$$n/grupo7/; \
		( cd tmp_$$n && jar -cvf ../$(DIST_DIR)/Consulta$$n.jar grupo7 ); \
		rm -rf tmp_$$n; \
	done
	@echo "Listo. Jars generados en $(DIST_DIR)/:"
	@ls -la $(DIST_DIR)/

clean:
	rm -rf $(BUILD_DIR) $(DIST_DIR) tmp_*
