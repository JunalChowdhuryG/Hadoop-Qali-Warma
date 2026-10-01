package grupo6;

import java.util.HashMap;
import java.util.Map;

/**
 * Codificacion de variables categoricas a numeros, para poder usarlas en el
 * descenso de gradiente. Se usa un diccionario fijo porque el conjunto de
 * departamentos del Peru es pequeño y conocido (25 valores) -- no hace
 * falta un job de MapReduce completo solo para enumerar estas categorias.
 */
public class FeatureEncoder {

	private static final Map<String, Integer> DEPARTAMENTOS = new HashMap<String, Integer>();
	static {
		String[] nombres = {
			"AMAZONAS", "ANCASH", "APURIMAC", "AREQUIPA", "AYACUCHO",
			"CAJAMARCA", "CALLAO", "CUSCO", "HUANCAVELICA", "HUANUCO",
			"ICA", "JUNIN", "LA LIBERTAD", "LAMBAYEQUE", "LIMA",
			"LORETO", "MADRE DE DIOS", "MOQUEGUA", "PASCO", "PIURA",
			"PUNO", "SAN MARTIN", "TACNA", "TUMBES", "UCAYALI"
		};
		for (int i = 0; i < nombres.length; i++) {
			DEPARTAMENTOS.put(nombres[i], i + 1);
		}
	}

	/** Codigo 1-25 del departamento, o null si el nombre no se reconoce. */
	public static Integer codigoDepartamento(String nombre) {
		if (nombre == null) {
			return null;
		}
		return DEPARTAMENTOS.get(nombre.trim().toUpperCase());
	}

	/** INICIAL=1, PRIMARIA=2, SECUNDARIA=3, cualquier otro/desconocido=0 */
	public static int codigoNivelEducativo(String nivel) {
		if (nivel == null) {
			return 0;
		}
		String n = nivel.trim().toUpperCase();
		if (n.equals("INICIAL")) {
			return 1;
		}
		if (n.equals("PRIMARIA")) {
			return 2;
		}
		if (n.equals("SECUNDARIA")) {
			return 3;
		}
		return 0;
	}
}
