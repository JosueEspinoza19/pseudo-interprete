public class Repite extends Tupla{
    Token op1, op2, op3;
    int contador;
    private Float valoroperando1 = null;

    public Repite(Token op1, Token op2, Token op3, int sv, int sf){
        super(sv, sf);
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
    }

    public String toString(){
        return "( " + super.toString() + ", [ " + op1 + ", " + op2 + ", " + op3 + " ] )";
    }

    public int ejecutar(TablaSimbolos ts){
        float operando1 = 0, operando2 = 0, incremento = 0;

        if(op1.getTipo().getNombre().equals("NUMERO")){
            operando1 = Float.parseFloat(op1.getNombre());
        }
        else{
            operando1 = ((Variable) ts.resolver(op1.getNombre())).getValor();
        }

        if(valoroperando1 == null){
            valoroperando1 = operando1;
        }

        if(op2.getTipo().getNombre().equals("NUMERO")){
            operando2 = Float.parseFloat(op2.getNombre());
        }
        else{
            operando2 = ((Variable) ts.resolver(op2.getNombre())).getValor();
        }

        if(op3.getTipo().getNombre().equals("NUMERO")){
            incremento = Float.parseFloat(op3.getNombre());
        }
        else{
            incremento = ((Variable) ts.resolver(op3.getNombre())).getValor();
        }

        if(operando1<operando2){
            operando1 += incremento;
            Variable v = (Variable) ts.resolver(op1.getNombre());
            v.setValor(operando1);
            return saltoVerdadero;
        }
        else{
            Variable v = (Variable) ts.resolver(op1.getNombre());
            v.setValor(valoroperando1);
            return saltoFalso;

        }

    }
}
