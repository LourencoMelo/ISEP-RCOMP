package utils;

import java.util.ArrayList;
import java.util.List;

public class ListaRequest {

    private static List<String> lista;

    public ListaRequest() {
    }

    public static String getLista(){
        lista = new ArrayList<>();
        lista.add("1- Teste request\n");
        lista.add("2- Fim request\n");
        lista.add("3- Entendido request\n");
        lista.add("4- Disponibilidade request\n");
        lista.add("5- Segmento request\n");
        return lista.toString();
    }

}
