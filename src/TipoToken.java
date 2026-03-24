public class TipoToken {

    public static final String INICIOPROGRAMA = "INICIOPROGRAMA";
    public static final String FINPROGRAMA = "FINPROGRAMA";
    public static final String VARIABLE = "VARIABLE";
    public static final String IGUAL = "IGUAL";
    public static final String NUMERO = "NUMERO";
    public static final String CADENA = "CADENA";
    public static final String OPARITMETICO = "OPARITMETICO";
    public static final String OPRELACIONAL = "OPRELACIONAL";
    public static final String COMA = "COMA";
    public static final String PARENTESISIZQ = "PARENTESISIZQ";
    public static final String PARENTESISDER = "PARENTESISDER";
    public static final String LEER = "LEER";
    public static final String INICIOMETODO = "INICIOMETODO";
    public static final String NOMBREMETODO = "NOMBREMETODO";
    public static final String PARAMETROS = "PARAMETROS";
    public static final String SINPARAMETROS = "SINPARAMETROS";
    public static final String RETORNO = "RETORNO";
    public static final String FINMETODO = "FINMETODO";
    public static final String ESCRIBIR = "ESCRIBIR";
    public static final String SI = "SI";
    public static final String ENTONCES = "ENTONCES";
    public static final String FINSI = "FINSI";
    public static final String MIENTRAS = "MIENTRAS";
    public static final String FINMIENTRAS = "FINMIENTRAS";
    public static final String DECLARACIONVARIABLES = "DECLARACIONVARIABLES";
    public static final String DOSPUNTOS = "DOSPUNTOS";
    public static final String REPITE = "REPITE";
    public static final String FINREPITE = "FINREPITE";
    public static final String ESPACIO = "ESPACIO";
    public static final String ERROR = "ERROR";

    private String nombre;
    private String patron;

    public TipoToken(String nombre, String patron) {
        this.nombre = nombre;
        this.patron = patron;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPatron() {
        return patron;
    }
}
