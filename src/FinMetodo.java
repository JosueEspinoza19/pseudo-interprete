public class FinMetodo extends Tupla{
    public FinMetodo(int sv, int sf){
        super(sv, sf);
    }

    public String toString(){
        return "( " + super.toString() + " )";
    }

    public int ejecutar(TablaSimbolos ts){
        return saltoVerdadero;
    }
}
