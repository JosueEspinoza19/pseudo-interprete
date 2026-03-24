public class MetodoTupla extends Tupla {
    Token nombre;
    Variable[] parametros;

    public MetodoTupla(Token nombre, Variable[] parametros, int sv, int sf) {
        super(sv, sf);
        this.nombre = nombre;
        this.parametros = parametros;

    }

    public MetodoTupla(Token nombre, int sv, int sf) {
        super(sv, sf);
        this.nombre = nombre;
    }

    public Token getNombre() {
        return nombre;
    }

    public String toString() {

        if(parametros != null){
            String params = "";
            for (Variable v : parametros) {
                params += v + ", ";
            }
            return "( " + super.toString() + ", [ " + nombre + ", [ " + params + " ] ] )";
        }
        else {
            return "( " + super.toString() + ", [ " + nombre + " ] )";
        }
    }

    public int ejecutar(TablaSimbolos ts) {
        return saltoFalso;
    }

}
