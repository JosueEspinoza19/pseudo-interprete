import java.util.LinkedHashMap;
import java.util.Map;

public class Metodo extends Simbolo implements Alcance {
    Alcance enclosingScope;
    Map<String, Simbolo> member = new LinkedHashMap<>();


    public Metodo(String nombre, Variable[] parametros, Alcance enclosingScope) {
        super(nombre);
        this.enclosingScope = enclosingScope;
        if(parametros != null) {
            for(Variable parametro : parametros) {
                define(parametro);
            }
        }
    }

    public String getScopeName() {
        return getNombre();
    }

    public Alcance getEnclosingScope() {
        return enclosingScope;
    }

    public void define(Simbolo simbolo) {
        member.put(simbolo.getNombre(), simbolo);
    }

    public Simbolo resolve(String nombre) {
        Simbolo s = member.get(nombre);
        if(s != null) {
            return s;
        }
        if(enclosingScope != null) {
            return enclosingScope.resolve(nombre);
        }
        return null;
    }

    public void printScope() {
        System.out.println("Scope: " + getScopeName());
        for (String key : member.keySet()) {
            System.out.println(key);
        }
        if(enclosingScope != null) {
            enclosingScope.printScope();
        }
    }
}
