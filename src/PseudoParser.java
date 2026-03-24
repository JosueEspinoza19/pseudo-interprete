import java.util.ArrayList;

public class PseudoParser {
    private ArrayList<Token> tokens;
    private int indiceToken = 0;
    private SyntaxException ex;
    private TablaSimbolos ts;
    private TipoIncorporado real;
    public Alcance alcanceActual = new AlcanceGlobal();
    private String nombreMetodoActual;
    private ArrayList<Variable> parametrosMetodoActual = new ArrayList<Variable>();
    private PseudoGenerador generador;

    public PseudoParser(TablaSimbolos ts, PseudoGenerador generador) {
        this.ts = ts;
        this.generador = generador;
    }

    public void analizar(PseudoLexer lexer) throws SyntaxException, SemanticException {
        tokens = lexer.getTokens();

        real = new TipoIncorporado("real");
        ts.definir(real);

        if(Programa()){
            if(indiceToken == tokens.size()) {
                System.out.println("La sintaxis del programa es correcta");
                return;
            }
        }
        throw ex;
    }

    private boolean Programa() {
        if(match(TipoToken.INICIOPROGRAMA)){
            if(DECLARACIONVARIABLES()){
                if (Enunciados()) {
                    if (match(TipoToken.FINPROGRAMA)) {
                        generador.crearTuplaFinPrograma();
                        alcanceActual.printScope();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean DECLARACIONVARIABLES(){
        if(match(TipoToken.DECLARACIONVARIABLES)){
            if(match(TipoToken.DOSPUNTOS)){
                if(conjuntoDeVariables()){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean conjuntoDeVariables() {
        int indiceAux = indiceToken;

        if(match(TipoToken.VARIABLE)){
            try {
                ts.definir(new Variable(tokens.get(indiceAux).getNombre(), real));
                alcanceActual.define(new Variable(tokens.get(indiceAux).getNombre(), real));
            } catch (SemanticException e) {
                System.out.println(e.getMessage());
            }
            System.out.print(tokens.get(indiceAux).getNombre() + " ");
            while(match(TipoToken.COMA)){
                if(match(TipoToken.VARIABLE)){
                    try {
                        ts.definir(new Variable(tokens.get(indiceToken-1).getNombre(), real));
                        alcanceActual.define(new Variable(tokens.get(indiceToken-1).getNombre(), real));
                    } catch (SemanticException e) {
                        System.out.println(e.getMessage());
                    }

                }
                else{
                    indiceToken = indiceAux;
                    return false;
                }
            }
            return true;
        }
        indiceToken = indiceAux;
        return false;
    }

    private boolean conjuntoDeParametros(){
        int indiceAux = indiceToken;

        if(match(TipoToken.VARIABLE)){
            parametrosMetodoActual.add(new Variable(tokens.get(indiceAux).getNombre(), real));
            System.out.print(tokens.get(indiceAux).getNombre() + " ");
            while(match(TipoToken.COMA)){
                if(match(TipoToken.VARIABLE)){
                    parametrosMetodoActual.add(new Variable(tokens.get(indiceToken-1).getNombre(), real));
                    System.out.print(tokens.get(indiceToken-1).getNombre() + " ");
                }
                else{
                    indiceToken = indiceAux;
                    return false;
                }
            }
            return true;
        }
        indiceToken = indiceAux;
        return false;

    }

    private boolean Enunciados() {
        int indiceAux = indiceToken;

        if(Enunciado()){
           while(Enunciado()){}
           return true;

        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Enunciado() {
        int indiceAux = indiceToken;


        if(currentToken(TipoToken.VARIABLE)){
            if(Asignacion()){
                if(alcanceActual.resolve(tokens.get(indiceAux).getNombre()) == null){
                    try {
                        throw new SemanticException("Variable no declarada: " + tokens.get(indiceAux).getNombre());
                    } catch (SemanticException e) {
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    return true;
                }
            }
        }

        indiceToken = indiceAux;

        if(currentToken(TipoToken.INICIOMETODO)){
            if(Metodo()){
                return true;
            }
        }

        indiceToken = indiceAux;

        if(llamadaMetodo()){
            return true;
        }

        indiceToken = indiceAux;

        if(currentToken(TipoToken.LEER)){
            if(Leer()){
                return true;
            }
        }

        indiceToken = indiceAux;

        if(currentToken(TipoToken.ESCRIBIR)){
            if(Escribir()){
                return true;
            }
        }

        indiceToken = indiceAux;

        if(currentToken(TipoToken.SI)){
            if(Si()){
                return true;
            }
        }

        indiceToken = indiceAux;

        if(currentToken(TipoToken.MIENTRAS)){
            if(Mientras()){
                return true;
            }
        }

        if(currentToken(TipoToken.REPITE)){
            if(Repite()){
                return true;
            }
        }

        indiceToken = indiceAux;
        return false;


    }

    private boolean Metodo(){
        int indiceAux = indiceToken;
        int indiceTupla = generador.getTuplas().size();

        if (match(TipoToken.INICIOMETODO)) {
            if (nombreMetodo()) {
                nombreMetodoActual = tokens.get(indiceToken-1).getNombre();
                if (sinParametros()||parametros()) {
                    Metodo metodo = new Metodo(nombreMetodoActual, parametrosMetodoActual.toArray(new Variable[parametrosMetodoActual.size()]), alcanceActual);
                    try {
                        ts.definir(metodo);
                        alcanceActual.define(metodo);
                    } catch (SemanticException e) {
                        System.out.println(e.getMessage());
                    }
                    alcanceActual = metodo;
                    generador.crearMetodoTupla(indiceAux, parametrosMetodoActual.toArray(new Variable[parametrosMetodoActual.size()]));
                    if(Enunciados()){
                        if(match(TipoToken.FINMETODO)){
                            alcanceActual = alcanceActual.getEnclosingScope();
                            generador.conectarMetodo(indiceTupla);
                            generador.crearTuplaFinMetodo();
                            return true;
                        }
                    }
                }
            }
        }
        indiceToken = indiceAux;
        return false;
    }

    private boolean nombreMetodo(){
        if(match(TipoToken.NOMBREMETODO)){
            if(match(TipoToken.DOSPUNTOS)) {
                indiceToken++;
                return true;
            }
        }
        return false;
    }

    private boolean parametros(){
        int indiceAux = indiceToken;

        if(match(TipoToken.PARAMETROS)){
            if(match(TipoToken.DOSPUNTOS)){
                if(conjuntoDeParametros()){
                    return true;
                }
            }
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean sinParametros(){
        int indiceAux = indiceToken;

        if(match(TipoToken.SINPARAMETROS)){
            return true;
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean llamadaMetodo(){
        int indiceAux = indiceToken;


        if(match(TipoToken.VARIABLE)){
            if(alcanceActual.resolve(tokens.get(indiceAux).getNombre()) != null){
                if(alcanceActual.resolve(tokens.get(indiceAux).getNombre()) instanceof Metodo){
                    if(match(TipoToken.PARENTESISIZQ)){
                        if(ParametrosLlamada()){
                            if(match(TipoToken.PARENTESISDER)){
                                generador.crearTuplaLlamadaMetodo(indiceAux, parametrosMetodoActual.toArray(new Variable[parametrosMetodoActual.size()]));
                                int indiceTupla = generador.getTuplas().size();
                                generador.conectarLlamadaMetodo(indiceTupla);
                                parametrosMetodoActual.clear();
                                return true;
                            }
                        }
                    }
                }
                else{
                    try {
                        throw new SemanticException("La variable no es un metodo: " + tokens.get(indiceAux).getNombre());
                    } catch (SemanticException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            else{
                try {
                    throw new SemanticException("Metodo no declarado: " + tokens.get(indiceAux).getNombre());
                } catch (SemanticException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean ParametrosLlamada(){
        int indiceAux = indiceToken;

        if(match(TipoToken.VARIABLE) || match(TipoToken.NUMERO)){
            parametrosMetodoActual.add(new Variable(tokens.get(indiceAux).getNombre(), real));
            while(match(TipoToken.COMA)){
                if(match(TipoToken.VARIABLE) || match(TipoToken.NUMERO)){
                    parametrosMetodoActual.add(new Variable(tokens.get(indiceToken-1).getNombre(), real));
                }
                else{
                    indiceToken = indiceAux;
                    return false;
                }
            }
            return true;

        }

        indiceToken = indiceAux;
        return false;
    }


    private boolean Repite() {
        int indiceAux = indiceToken;
        int indiceTupla = generador.getTuplas().size();

        if(match(TipoToken.REPITE)){
            if(condicionRepite()){
                generador.crearTuplaRepite(indiceAux);
                if(Enunciados()){
                    if(match(TipoToken.FINREPITE)){
                        generador.conectarRepite(indiceTupla);
                        return true;
                    }
                }
            }
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean condicionRepite() {
        int indiceAux = indiceToken;

        if(match(TipoToken.PARENTESISIZQ)) {
            if (match(TipoToken.VARIABLE)) {
                if(alcanceActual.resolve(tokens.get(indiceAux+1).getNombre()) != null){
                    if (match(TipoToken.COMA)) {
                        if (match(TipoToken.VARIABLE)) {
                            if(alcanceActual.resolve(tokens.get(indiceAux+3).getNombre()) != null) {
                                if (match(TipoToken.COMA)) {
                                    if (match(TipoToken.NUMERO)) {
                                        if (match(TipoToken.PARENTESISDER)) {
                                            return true;
                                        }
                                    }

                                }
                            }
                            else{
                                try {
                                    throw new SemanticException("Variable no declarada: " + tokens.get(indiceAux+3).getNombre());
                                } catch (SemanticException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                        if(match(TipoToken.NUMERO)){
                            if(match(TipoToken.COMA)){
                                if(match(TipoToken.NUMERO)){
                                    if(match(TipoToken.PARENTESISDER)){
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    try {
                        throw new SemanticException("Variable no declarada: " + tokens.get(indiceAux+1).getNombre());
                    } catch (SemanticException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Asignacion() {
        int indiceAux = indiceToken;

        if(match(TipoToken.VARIABLE)){
            if(match(TipoToken.IGUAL)){
                if(Expresion()){
                    generador.crearTuplaAsignacion(indiceAux, indiceToken);
                    return true;

                }
            }
        }
        indiceToken = indiceAux;
        return false;
    }

    private boolean Expresion(){
        int indiceAux = indiceToken;

        if(Valor()){
            if(match(TipoToken.OPARITMETICO)){
                if(Valor()){
                    return true;
                }
            }
        }

        indiceToken = indiceAux;

        if(Valor()){
            return true;
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Valor(){
        if(match(TipoToken.VARIABLE)){
            if(alcanceActual.resolve(tokens.get(indiceToken-1).getNombre()) != null){
                return true;
            }
            else{
                try {
                    throw new SemanticException("Variable no declarada: " + tokens.get(indiceToken-1).getNombre());
                } catch (SemanticException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        if(match(TipoToken.NUMERO)){
            return true;
        }
        return false;
    }

    private boolean Leer(){
        int indiceAux = indiceToken;

        if(match(TipoToken.LEER)){
            if(match(TipoToken.VARIABLE)){
                if(alcanceActual.resolve(tokens.get(indiceAux+1).getNombre()) != null) {
                    generador.crearTuplaLeer(indiceAux + 1);
                    return true;
                }
                else{
                    try {
                        throw new SemanticException("Variable no declarada: " + tokens.get(indiceAux+1).getNombre());
                    } catch (SemanticException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        indiceToken = indiceAux;
        return false;
    }

    private boolean Escribir(){
        int indiceAux = indiceToken;

        if(match(TipoToken.ESCRIBIR)){
            if(match(TipoToken.CADENA)){
                if(match(TipoToken.COMA)){
                    if(match(TipoToken.VARIABLE)){
                        if(alcanceActual.resolve(tokens.get(indiceAux+3).getNombre()) != null) {
                            generador.crearTuplaEscribir(indiceAux + 1, indiceToken);
                            return true;
                        }
                        else{
                            try {
                                throw new SemanticException("Variable no declarada: " + tokens.get(indiceAux+3).getNombre());
                            } catch (SemanticException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }
            }
        }

        indiceToken = indiceAux;

        if(match(TipoToken.ESCRIBIR)){
            if(match(TipoToken.CADENA)){
                generador.crearTuplaEscribir(indiceAux+1, indiceToken);
                return true;
            }
        }

        indiceToken = indiceAux;

        if (match(TipoToken.ESCRIBIR)){
            if(match(TipoToken.VARIABLE)){
                if(alcanceActual.resolve(tokens.get(indiceAux+1).getNombre()) != null) {
                    generador.crearTuplaEscribir(indiceAux + 1, indiceToken);
                    return true;
                }
                else{
                    try {
                        throw new SemanticException("Variable no declarada: " + tokens.get(indiceAux+1).getNombre());
                    } catch (SemanticException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Si(){
        int indiceAux = indiceToken;
        int indiceTupla = generador.getTuplas().size();

        if(match(TipoToken.SI)){
            if(Comparacion()){
                if(match(TipoToken.ENTONCES)){
                    if(Enunciados()){
                        if(match(TipoToken.FINSI)){
                            generador.conectarSi(indiceTupla);
                            return true;
                        }
                    }
                }
            }
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Mientras(){
        int indiceAux = indiceToken;
        int indiceTupla = generador.getTuplas().size();

        if(match(TipoToken.MIENTRAS)){
            if(Comparacion()){
                if(Enunciados()){
                    if(match(TipoToken.FINMIENTRAS)){
                        generador.conectarMientras(indiceTupla);
                        return true;
                    }
                }
            }
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Comparacion(){
        int indiceAux = indiceToken;

        if(match(TipoToken.PARENTESISIZQ)){
            if(Valor()){
                if(match(TipoToken.OPRELACIONAL)){
                    if(Valor()){
                        if(match(TipoToken.PARENTESISDER)){
                            generador.crearTuplaComparacion(indiceAux+1);
                            return true;
                        }
                    }
                }
            }

        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean match(String nombre){
        if(currentToken(nombre)){
            System.out.println(nombre + ": " + tokens.get(indiceToken).getNombre());

            indiceToken++;
            return true;
        }

        if(ex == null){
            ex = new SyntaxException(nombre, tokens.get(indiceToken).getTipo().getNombre());
        }

        return false;
    }

    private boolean currentToken(String name){
       return tokens.get(indiceToken).getTipo().getNombre().equals(name);

    }


}
