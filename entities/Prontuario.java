package entities;

// Classe que representa um prontuário médico de um paciente

public class Prontuario implements Comparable<Prontuario> {

    private String nome;

    private int idade;

    private String sintoma;

    private Risco risco;

    private Status status;

    private String justificativaCancelamento;

    public Prontuario(String nome, int idade, String sintoma, String cor) {

        // Atributos do paciente

        this.nome = nome.trim();

        this.idade = idade;

        this.sintoma = sintoma.trim();

        this.risco = Risco.fromString(cor); // Enum com a cor de risco (preto, vermelho, amarelo, verde)

        this.status = Status.PENDENTE; // Enum com o status (pendente, atendido, cancelado)

        this.justificativaCancelamento = ""; // Texto da justificativa caso tenha sido cancelado

    }

    public Prontuario(String nome, int idade, String sintoma, Risco risco) {

        this.nome = nome.trim();

        this.idade = idade;

        this.sintoma = sintoma.trim();

        this.risco = risco;

        this.status = Status.PENDENTE;

        this.justificativaCancelamento = "";

    }

    // Getters e Setters

    public String getNome() {

        return nome;

    }

    public Status getStatus() {

        return status;

    }

    public void setStatus(Status status) {

        this.status = status;

    }

    public String getJustificativaCancelamento() {

        return justificativaCancelamento;

    }

    public void setJustificativaCancelamento(String justificativa) {

        this.justificativaCancelamento = justificativa;

    }

    // Exibe as informações do prontuário em formato visual bonito

    public String toprontuario() {

        return String.format(
                "\n╔══════════════════════════════════════╗\n" +
                        "║ Nome: %-30s ║\n" +
                        "║ Idade: %-29d ║\n" +
                        "║ Sintoma: %-27s ║\n" +
                        "║ Risco: %-29s ║\n" +
                        "╚══════════════════════════════════════╝",

                nome, idade, sintoma, risco.name());

    }

    // Tentei simular como se fosse papel impresso

    public String toProntuario() {

        return String.format(
                "\n========= PRONTUÁRIO DO PACIENTE =========\n" +
                        "Nome: %s+\nIdade: %d anos\nSintoma: %s\nClassificação de Risco: %s\nAtendimento realizado com sucesso.\n" +
                        "==========================================\n",

                nome, idade, sintoma, risco.name());
    }

    @Override

    public int compareTo(Prontuario outro) {

        return Integer.compare(this.risco.getPrioridade(), outro.risco.getPrioridade());

    }
}