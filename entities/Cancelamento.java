package entities;

import java.util.ArrayList;
import java.util.List;

public class Cancelamento {
    private static List<Cancelamento> cancelamentos = new ArrayList<>();

    private Prontuario paciente;
    private String justificativa;

    public Cancelamento(Prontuario paciente, String justificativa) {
        this.paciente = paciente;
        this.justificativa = justificativa;
        paciente.setStatus(Status.CANCELADO);
        paciente.setJustificativaCancelamento(justificativa);
        cancelamentos.add(this);
    }

    @Override
    public String toString() {
        return String.format(
            "\n╔══════════════════════════════════════════════════════════════════════════════════╗\n" +
            "║                              PRONTUÁRIO CANCELADO                                ║\n" +
            "╠══════════════════════════════════════════════════════════════════════════════════╣\n" +
            "║ Paciente: %-70s ║\n" +
            "║ Justificativa: %-65s ║\n" +
            "╚══════════════════════════════════════════════════════════════════════════════════╝",
            paciente.getNome(), justificativa);
    }

    public static List<Cancelamento> getCancelamentos() {
        return cancelamentos;
    }

    public static void cancelarProntuario(Prontuario paciente, String justificativa, HeapProntuario heap) {
        heap.remover(paciente);
        new Cancelamento(paciente, justificativa); // adiciona à lista automaticamente
    }
}
