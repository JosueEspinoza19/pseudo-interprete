public class llamadaMetodo extends Tupla {
    Token nombre;
    Variable[] parametros;

    public llamadaMetodo(Token nombre, Variable[] parametros, int sv, int sf) {
        super(sv, sf);
        this.nombre = nombre;
        this.parametros = parametros;

    }

    public Token getNombre() {
        return nombre;
    }

    public String toString() {
        String params = "";
        for (Variable v : parametros) {
            params += v + ", ";
        }
        return "( " + super.toString() + ", [ " + nombre + ", [ " + params + " ] ] )";
    }

    public int ejecutar(TablaSimbolos ts) {
        return saltoFalso;
    }
}
