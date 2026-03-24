public interface Alcance {
    public String getScopeName();
    public Alcance getEnclosingScope();
    public void define(Simbolo sym) throws SemanticException;
    public Simbolo resolve(String name);
    public void printScope();

}
