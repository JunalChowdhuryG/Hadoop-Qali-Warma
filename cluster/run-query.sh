#!/bin/bash
# Uso: run-query.sh <numero_de_consulta>
# Ejecuta la consulta N (1-14), limpia resultados previos, corre el jar
# correspondiente y muestra el resultado.

set -e

QUERY=$1
if [ -z "$QUERY" ]; then
    echo "Uso: run-query.sh <numero_de_consulta (1-14)>"
    exit 1
fi

DATASET="/input_dir/dataQaliWarma.csv"
JAR="/jars/Consulta${QUERY}.jar"
OUT="/output_c${QUERY}"

if [ ! -f "$JAR" ]; then
    echo "ERROR: no existe $JAR. Verifica que dist/ tenga Consulta${QUERY}.jar"
    echo "       (corre primero el build de compilacion en la raiz del repo)"
    exit 1
fi

case $QUERY in
    1) CLASS="grupo1.Consulta1Driver";  ARGS="$DATASET $OUT" ;;
    2) CLASS="grupo1.Consulta2Driver";  ARGS="$DATASET $OUT" ;;
    3) CLASS="grupo1.Consulta3Driver";  ARGS="$DATASET $OUT" ;;
    4) CLASS="grupo1.Consulta4Driver";  ARGS="$DATASET $OUT" ;;
    5) CLASS="grupo1.Consulta5Driver";  ARGS="$DATASET $OUT" ;;
    6) CLASS="grupo2.Consulta6Driver";  ARGS="$DATASET $OUT" ;;
    7) CLASS="grupo3.Consulta7Driver";  ARGS="$DATASET $OUT" ;;
    8) CLASS="grupo4.Consulta8Driver";  ARGS="$DATASET $OUT" ;;
    9)  CLASS="grupo5.Consulta9Driver";  ARGS="$DATASET /intermedio_c9 $OUT" ;;
    10) CLASS="grupo5.Consulta10Driver"; ARGS="$DATASET /intermedio_c10 $OUT" ;;
    11) CLASS="grupo6.Consulta11Driver"; ARGS="$DATASET /gd_c11" ;;
    12) CLASS="grupo6.Consulta12Driver"; ARGS="$DATASET /gd_c12" ;;
    13) CLASS="grupo7.Consulta13Driver"; ARGS="$DATASET /gd_c13" ;;
    14) CLASS="grupo7.Consulta14Driver"; ARGS="$DATASET /gd_c14" ;;
    *)  echo "Consulta '$QUERY' no reconocida (debe ser 1-14)"; exit 1 ;;
esac

echo ">>> Limpiando resultados previos de la Consulta $QUERY..."
hadoop fs -rm -r -f "$OUT" >/dev/null 2>&1 || true
case $QUERY in
    9)  hadoop fs -rm -r -f /intermedio_c9 >/dev/null 2>&1 || true ;;
    10) hadoop fs -rm -r -f /intermedio_c10 >/dev/null 2>&1 || true ;;
    11) hadoop fs -rm -r -f /gd_c11 >/dev/null 2>&1 || true ;;
    12) hadoop fs -rm -r -f /gd_c12 >/dev/null 2>&1 || true ;;
    13) hadoop fs -rm -r -f /gd_c13 >/dev/null 2>&1 || true ;;
    14) hadoop fs -rm -r -f /gd_c14 >/dev/null 2>&1 || true ;;
esac

echo ">>> Ejecutando: hadoop jar $JAR $CLASS $ARGS"
hadoop jar "$JAR" "$CLASS" $ARGS

echo ""
echo "--- Resultado (Consulta $QUERY) ---"
case $QUERY in
    11|12|13|14)
        echo "(Grupos 6-7: la tabla epoca/loss/accuracy ya se imprimio arriba."
        echo " Para ver el detalle de la ultima epoca en HDFS:)"
        LAST_EPOCH=$(hadoop fs -ls "$OUT" 2>/dev/null | grep epoch_ | awk -F'epoch_' '{print $2}' | sort -n | tail -1)
        if [ -n "$LAST_EPOCH" ]; then
            hadoop fs -cat "$OUT/epoch_${LAST_EPOCH}/part-00000"
        fi
        ;;
    *)
        hadoop fs -cat "$OUT/part-00000" 2>/dev/null | head -30
        ;;
esac
