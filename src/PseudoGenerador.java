import java.util.ArrayList;

public class PseudoGenerador {
    private ArrayList<Tupla> tuplas = new ArrayList<Tupla>();
    ArrayList<Token> tokens;

    public PseudoGenerador(ArrayList<Token> tokens){
        this.tokens = tokens;
    }

    public void crearTuplaAsignacion(int indiceInicial, int indiceFinal){
        if(indiceFinal - indiceInicial ==3){
            tuplas.add(new Asignacion(tokens.get(indiceInicial), tokens.get(indiceInicial + 2), tuplas.size()+1, tuplas.size()+1));
        }
        else if(indiceFinal - indiceInicial == 5){
            System.out.println("tuplas size: " + tuplas.size()+1);
            tuplas.add(new Asignacion(tokens.get(indiceInicial), tokens.get(indiceInicial + 2), tokens.get(indiceInicial + 3), tokens.get(indiceInicial + 4), tuplas.size()+1, tuplas.size()+1));

        }
    }

    public void crearTuplaLeer(int indiceInicial){
        tuplas.add(new Leer(tokens.get(indiceInicial), tuplas.size()+1, tuplas.size()+1));
    }

    public void crearTuplaEscribir(int indiceInicial, int indiceFinal){
        if(indiceFinal - indiceInicial == 1){
            tuplas.add(new Escribir(tokens.get(indiceInicial), tuplas.size()+1, tuplas.size()+1));
        }
        else if(indiceFinal - indiceInicial == 3){
            tuplas.add(new Escribir(tokens.get(indiceInicial), tokens.get(indiceInicial + 2), tuplas.size()+1, tuplas.size()+1));
        }
    }

    public void crearTuplaComparacion(int indiceInicial){
        tuplas.add(new Comparacion(tokens.get(indiceInicial), tokens.get(indiceInicial + 1), tokens.get(indiceInicial + 2), tuplas.size()+1, tuplas.size()+1));
    }

    public void crearTuplaFinPrograma(){
        tuplas.add(new FinPrograma());
    }

    public void crearMetodoTupla(int indiceInicial, Variable[] parametros){

        tuplas.add(new MetodoTupla(tokens.get(indiceInicial + 3), parametros, tuplas.size() + 1, tuplas.size() + 1));

    }

    public void crearTuplaFinMetodo(){
        tuplas.add(new FinMetodo(tuplas.size()+1, tuplas.size()+1));
    }

    public void crearTuplaLlamadaMetodo(int indiceInicial, Variable[] parametros){
        tuplas.add(new llamadaMetodo(tokens.get(indiceInicial), parametros, tuplas.size()+1, tuplas.size()+1));
    }

    public void crearTuplaRepite(int indiceInicial){
        tuplas.add(new Repite(tokens.get(indiceInicial+2), tokens.get(indiceInicial+4), (tokens.get(indiceInicial+6)), tuplas.size()+1, tuplas.size()+1));

    }

    public void conectarSi(int tuplaInicial){
        int tuplaFinal = tuplas.size()-1;

        if(tuplaInicial >= tuplas.size() || tuplaInicial >= tuplaFinal){
            return;
        }
        tuplas.get(tuplaInicial).setSaltoFalso(tuplaFinal+1);
    }

    public void conectarMientras(int tuplaInicial){
        int tuplaFinal = tuplas.size()-1;

        if(tuplaInicial >= tuplas.size() || tuplaInicial >= tuplaFinal){
            return;
        }

        tuplas.get(tuplaInicial).setSaltoFalso(tuplaFinal+1);
        tuplas.get(tuplaFinal).setSaltoVerdadero(tuplaInicial);
        tuplas.get(tuplaFinal).setSaltoFalso(tuplaInicial);

        for(int i = tuplaFinal; i > tuplaInicial; i--){
            Tupla t = tuplas.get(i);

            if(t instanceof Comparacion && t.getSaltoFalso() == tuplaFinal+1){
                t.setSaltoFalso(tuplaInicial);
            }
        }
    }

    public void conectarMetodo(int tuplaInicial){
        int tuplaFinal = tuplas.size()-1;

        if(tuplaInicial >= tuplas.size() || tuplaInicial >= tuplaFinal){
            return;
        }

        tuplas.get(tuplaInicial).setSaltoFalso(tuplaFinal+1);


        for(int i = tuplaFinal; i > tuplaInicial; i--){
            Tupla t = tuplas.get(i);

            if(t instanceof MetodoTupla && t.getSaltoFalso() == tuplaFinal+1){
                t.setSaltoFalso(tuplaInicial);
            }
        }
    }

    public void conectarLlamadaMetodo(int tuplaInicial) {
        int tuplaFinal = tuplas.size() - 1;
        tuplaInicial--;

        if (tuplaInicial == 0) {
            return;
        }

        for(int i = tuplaFinal; i > 0; i--){
            Tupla t = tuplas.get(i);

            if(t instanceof MetodoTupla && ((MetodoTupla) t).getNombre().getNombre().equals(((llamadaMetodo) tuplas.get(tuplaInicial)).getNombre().getNombre())){
                tuplas.get(tuplaInicial).setSaltoFalso(i);
                tuplas.get(tuplas.get(i).getSaltoFalso()).setSaltoVerdadero(tuplaFinal+1);
                tuplas.get(i).setSaltoFalso(i+1);
            }
        }
    }

    public void conectarRepite(int tuplaInicial){
        int tuplaFinal = tuplas.size()-1;

        if(tuplaInicial >= tuplas.size() || tuplaInicial >= tuplaFinal){
            return;
        }

        tuplas.get(tuplaInicial).setSaltoFalso(tuplaFinal+1);
        tuplas.get(tuplaFinal).setSaltoVerdadero(tuplaInicial);
        tuplas.get(tuplaFinal).setSaltoFalso(tuplaInicial);

        for(int i = tuplaFinal; i > tuplaInicial; i--){
            Tupla t = tuplas.get(i);

            if(t instanceof Repite && t.getSaltoFalso() == tuplaFinal+1){
                t.setSaltoFalso(tuplaInicial);
            }
        }

    }

    public ArrayList<Tupla> getTuplas(){
        return tuplas;
    }
}
