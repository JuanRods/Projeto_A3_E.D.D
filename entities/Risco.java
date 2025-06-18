package entities;

// Enumeração que representa os níveis de risco de um paciente em um sistema de triagem.

public enum Risco {
    PRETO(1), VERMELHO(2), AMARELO(3), VERDE(4), INDEFINIDO(5);

    private final int prioridade;

    Risco(int prioridade) {

        this.prioridade = prioridade;

    }

    public int getPrioridade() {

        return prioridade;

    }

    public static Risco fromString(String cor) {
      
        return switch (cor.toLowerCase()) {
      
            case "preto" -> PRETO;
      
            case "vermelho" -> VERMELHO;
      
            case "amarelo" -> AMARELO;
      
            case "verde" -> VERDE;
      
            default -> INDEFINIDO;
      
        };
    
    }

}