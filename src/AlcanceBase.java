import java.util.HashMap;
import java.util.Map;

public abstract class AlcanceBase implements Alcance{
    Alcance enclosingScope;
    Map<String, Simbolo> members = new HashMap<>();

    public AlcanceBase(Alcance alcanceActual) {
        this.enclosingScope = alcanceActual;
    }

   public Alcance getEnclosingScope() {
       return enclosingScope;
   }

    public void define(Simbolo simbolo) {
         members.put(simbolo.getNombre(), simbolo);
    }

    public Simbolo resolve(String nombre) {
        Simbolo s = members.get(nombre);
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
        for (String key : members.keySet()) {
            System.out.println(key + " : " + members.get(key).getTipo());
        }
    }
}
