import java.util.ArrayList;

public class TablaSimbolos {
    private ArrayList<Simbolo> simbolos = new ArrayList<Simbolo>();

    public void definir(Simbolo simbolo) throws SemanticException{
        for (Simbolo s : simbolos) {
            if (s.getNombre().equals(simbolo.getNombre())) {
                throw new SemanticException("El simbolo " + simbolo.getNombre() + " ya fue declarado");
            }
        }
        simbolos.add(simbolo);

    }

    public void modificar(Simbolo simbolo) throws SemanticException{
        for (Simbolo s : simbolos) {
            if (s.getNombre().equals(simbolo.getNombre())) {
                simbolos.remove(s);
                simbolos.add(simbolo);
                return;
            }
        }
        throw new SemanticException("El simbolo " + simbolo.getNombre() + " no ha sido declarado");
    }

    public Simbolo resolver(String nombre) {
        for (Simbolo s : simbolos) {
            if (s.getNombre().equals(nombre)) {
                return s;
            }
        }
        return null;
    }

    public ArrayList<Simbolo> getSimbolos() {
        return simbolos;
    }

}
