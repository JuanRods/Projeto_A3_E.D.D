package entities;

// As listas dinamicas
import java.util.ArrayList;
import java.util.List;

/**
 * Essa classe é responsável por armazenar os prontuários que foram cancelados,
 * junto com uma justificativa. Mantendo um histórico global desses cancelamentos.
 */
public class Cancelamento {
    
    // Lista estática para guardar todos os cancelamentos realizados no sistema
    private static List<Cancelamento> cancelamentos = new ArrayList<>();

    private Prontuario paciente;          // O prontuário que foi cancelado
    private String justificativa;         // O motivo pelo qual foi cancelado

    /**
     * Construtor da classe Cancelamento.
     * Recebe o prontuário e a justificativa, altera o status para CANCELADO,
     * e armazena o objeto na lista global de cancelamentos.
     */
    public Cancelamento(Prontuario paciente, String justificativa) {
        this.paciente = paciente;
        this.justificativa = justificativa;
        paciente.setStatus(Status.CANCELADO);                      // Marca o status do prontuário como cancelado
        paciente.setJustificativaCancelamento(justificativa);      // Salva o motivo do cancelamento no prontuário
        cancelamentos.add(this);                                   // Adiciona o cancelamento à lista estática
    }

    // Formata e retorna o conteúdo do cancelamento de forma visual no terminal.
    
    @Override
    public String toString() {
        return String.format(
            "\n╔══════════════════════════════════════════════════════════════════════════════════╗\n" +
            "║                              PRONTUÁRIO CANCELADO                                ║\n" +
            "╠══════════════════════════════════════════════════════════════════════════════════╣\n" +
            "║ Paciente: %-70s ║\n" +
            "║ Justificativa: %-65s ║\n" +
            "╚══════════════════════════════════════════════════════════════════════════════════╝",
            paciente.getNome(), justificativa
        );
    }

    // Retorna a lista completa de cancelamentos que já ocorreram no sistema.

    public static List<Cancelamento> getCancelamentos() {
        return cancelamentos;
    }

    // Remove o prontuario da fila e já cria o registro de cancelamento com justificativa.
     
    public static void cancelarProntuario(Prontuario paciente, String justificativa, HeapProntuario heap) {
        heap.remover(paciente);                       // Remove da fila de prioridade
        new Cancelamento(paciente, justificativa);    // Cria e armazena o cancelamento
    }
}
