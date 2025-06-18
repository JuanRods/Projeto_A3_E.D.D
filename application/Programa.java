package application; // Define o pacote (pasta) ao qual esta classe pertence

// Imports das entidades do projeto elas, eu preferi separar o codigo o minimo possivel do arquivo do main.
import entities.HeapProntuario; // Classe que coloca uma heap para gerenciar a fila de prontuarios
import entities.Prontuario; // Classe que armazena o prontuario
import entities.Risco; // Enum que armazena as prioridades de risco (classificação de cor) do prontuario
import entities.Status; // Enum que armazena o status do prontuário (pendente, atendido ou cancelado)
import entities.Cancelamento; // Classe que armazena prontuarios cancelados

// Imports da biblioteca Java
import java.util.ArrayList; // Lista dinâmica usada para armazenar histórico e prontuarios cancelados
import java.util.Comparator; // Interface para definir critérios de ordenação de listas
import java.util.List; // Interface genérica de listas, eu usei pro ArrayList
import java.util.Optional; // Tipo contêiner que pode conter ou não um valor (evita null ao buscar)
import java.util.Scanner; // Classe para ler entradas do usuário no console

public class Programa {

    private static Scanner sc = new Scanner(System.in);

    private static HeapProntuario fila = new HeapProntuario();

    private static List<Prontuario> historico = new ArrayList<>();

    private static List<Cancelamento> cancel = Cancelamento.getCancelamentos();

    public static void main(String[] args) {

        // Inicia a fila com 4 prontuarios

        PreAddProntuario(fila);

        // Inicia a fila com 1 prontuario Cancelado
        AddProntuarioCancelado(fila);

        // Loop principal do sistema
        while (true) {

            MostrarMenu(); /*
                            * Mostra opções do Menu principal o usuário no console
                            * (eu acabei editando pra ficar bonito e ficou de nada.)
                            */

            String opcao = lerOpcaoMenu(1, 6); // Lê opção validada

            switch (opcao) {

                case "1" -> AddProntuario(); // Adiciona novo prontuario na fila (Auto explicativo)

                case "2" -> ImprimirProntuario(); /*
                                                   * Imprime prontuario e pergunta sobre atendimento
                                                   * ( Se atender ele sai da fila, se não ele continua na fila como
                                                   * pendente)
                                                   */

                case "3" -> VerPontruarios(); /*
                                               * Visualiza prontuários pendentes/atendidos/cancelados ( Pendentes por
                                               * ( Pendentes por prioridade, Atendidos como Impressos e Cancelados como
                                               * excluidos)
                                               */

                case "4" -> cancelarProntuario(); // Cancela prontuário com justificativa (Apenas medida de segurança
                                                  // para "Transparência do sistema")

                case "5", "6" -> { // Encerra o Sistema
                    System.out.println("\n╔══════════════════════════════════╗");
                    System.out.println("║        SISTEMA ENCERRADO         ║");
                    System.out.println("╚══════════════════════════════════╝");
                    sc.close();
                    return;
                }
            }
        }
    }

    // Metodo que exibe o menu principal com as opções disponíveis no sistema.

    private static void MostrarMenu() {

        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║         SISTEMA DE FILA HOSPITALAR        ║");
        System.out.println("╠═══════════════════════════════════════════╣");
        System.out.println("║ 1 - Adicionar prontuario médico           ║");
        System.out.println("║ 2 - Imprimir próxima prontuario           ║");
        System.out.println("║ 3 - Visualização geral de prontuarios     ║");
        System.out.println("║ 4 - Cancelar prontuário                   ║");
        System.out.println("║ 5 - Sair                                  ║");
        System.out.println("╚═══════════════════════════════════════════╝");
        System.out.print("\nDigite sua opção (1-5): ");
    }

    /*
     * Lê uma opção digitada pelo usuário e valida se está dentro do intervalo
     * permitido.
     * os parametros min e max definem os valores minimos e maximos aceitos na
     * validação
     * e retorna uma mensagem de erro caso a o numero inserido seja invalido
     */
    private static String lerOpcaoMenu(int min, int max) {

        String opcao;

        while (true) {

            opcao = sc.nextLine().trim();

            if (opcao.matches("[" + min + "-" + max + "]")) {

                break;

            } else {
                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║    Opção inválida. Tente novamente.  ║");
                System.out.println("╚══════════════════════════════════════╝");
                System.out.print("Digite um número entre " + min + " e " + max + ": ");
            }

        }
        return opcao;
    }

    // Solicita informações do prontuario ao usuário e insere na fila de
    // atendimento.

    private static void AddProntuario() {

        System.out.print("Nome: ");

        String nome = validarStringVazia();

        System.out.print("Idade: ");

        int idade = lerIntValido();

        System.out.print("Sintoma: ");

        String sintoma = validarStringVazia();

        Risco risco;

        while (true) {

            System.out.print("Classificação de risco (verde/amarelo/vermelho/preto): ");

            String cor = sc.nextLine().trim().toLowerCase();

            risco = Risco.fromString(cor);

            if (risco != Risco.INDEFINIDO)

                break;

            System.out.println("\n╔══════════════════════════════════════════════╗");
            System.out.println("║    Cor de risco inválida. Tente novamente.   ║");
            System.out.println("╚══════════════════════════════════════════════╝");
        }

        fila.inserir(new Prontuario(nome, idade, sintoma, risco));

        System.out.println("\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║    Prontuário do prontuario adicionado com sucesso!   ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }

    /**
     * Imprime a próxima prontuario da fila e pergunta se o prontuario será
     * atendido.
     * Atualiza o status conforme a resposta.
     */
    private static void ImprimirProntuario() {
        if (fila.estaVazia()) {
            System.out.println("\n╔═════════════════════════════════════════════╗");
            System.out.println("║          Nenhum prontuário na fila.         ║");
            System.out.println("╚═════════════════════════════════════════════╝");
            return;
        }

        List<Prontuario> pulados = new ArrayList<>();

        while (!fila.estaVazia()) {
            Prontuario p = fila.peek();

            System.out.println(p.toprontuario());
            System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
            System.out.println("║     Deseja imprimir a prontuario do próximo prontuario?   ║");
            System.out.println("║           (s = sim / p = pular / x = cancelar)            ║");
            System.out.println("╚═══════════════════════════════════════════════════════════╝");
            System.out.print("Opção: ");
            String opcao = ValidarConfirmacao().toLowerCase();

            switch (opcao) {
                case "s": {
                    p = fila.remover();
                    p.setStatus(Status.ATENDIDO);
                    System.out.println("\n╔════════════════════════════════════════╗");
                    System.out.println("║    Prontuário atendido com sucesso.    ║");
                    System.out.println("╚════════════════════════════════════════╝");
                    System.out.println(p.toProntuario());

                    if (!historico.contains(p)) {
                        historico.add(p);
                    }
                    break;
                }

                case "p": {
                    fila.remover(); // Remove da fila principal
                    pulados.add(p); // Armazena temporariamente
                    System.out.println("\n╔══════════════════════════════════════╗");
                    System.out.println("║     Prontuário pulado com sucesso    ║");
                    System.out.println("╚══════════════════════════════════════╝");

                    break;
                }

                case "x": {
                    System.out.println("\n╔══════════════════════════════════════╗");
                    System.out.println("║    Retornando ao menu principal.     ║");
                    System.out.println("╚══════════════════════════════════════╝");
                    for (Prontuario prontuarioPulado : pulados) {
                        fila.inserir(prontuarioPulado);
                    }
                    return;
                }

                default: {
                    System.out.println("\n╔══════════════════════════════════════╗");
                    System.out.println("║    Opção inválida. Tente novamente.  ║");
                    System.out.println("╚══════════════════════════════════════╝");
                    for (Prontuario prontuarioPulado : pulados) {
                        fila.inserir(prontuarioPulado);
                    }
                    break;
                }
            }
        }

        // Reinsere os prontuários pulados no final da fila
        for (Prontuario prontuarioPulado : pulados) {
            fila.inserir(prontuarioPulado);
        }
    }

    /*
     * Permite ao usuário escolher entre visualizar prontuarios pendentes (por
     * risco),atendidos (ordem alfabética) ou cancelados.
     */

    private static void VerPontruarios() {

        while (true) {

            System.out.println("\n╔════════════════════════════════════════════════════╗");
            System.out.println("║          VISUALIZAÇÃO GERAL DE PRONTUÁRIOS         ║");
            System.out.println("╠════════════════════════════════════════════════════╣");
            System.out.println("║ 1 - Ver prontuarios pendentes                      ║");
            System.out.println("║ 2 - Ver prontuarios atendidos                      ║");
            System.out.println("║ 3 - Ver prontuarios cancelados                     ║");
            System.out.println("║ 4 - Voltar ao menu principal                       ║");
            System.out.println("╚════════════════════════════════════════════════════╝");
            System.out.print("\nEscolha a visualização (1-4): ");

            String opcao = lerOpcaoMenu(1, 4);

            switch (opcao) {

                // Imprime a lista atual por ordem de prioridade.

                case "1" -> {

                    if (fila.estaVazia()) {
                        System.out.println("\n╔═════════════════════════════════════════════╗");
                        System.out.println("║          Nenhum prontuario na fila.         ║");
                        System.out.println("╚═════════════════════════════════════════════╝");
                    } else {
                        System.out.println("\n╔════════════════════════════════════════╗");
                        System.out.println("║        ProntuarioS PENDENTES           ║");
                        System.out.println("╚════════════════════════════════════════╝");

                        List<Prontuario> pendentes = fila.getElementos();

                        pendentes.sort(Comparator.naturalOrder());

                        pendentes.forEach(p -> System.out.println(p.toprontuario()));
                    }

                }

                // Imprime o Historico de atendimentos (Já saiu da fila de prioridade) em ordem
                // alfabetica

                case "2" -> {

                    if (fila.estaVazia()) {
                        System.out.println("\n╔═════════════════════════════════════════════╗");
                        System.out.println("║          Nenhum prontuário na fila.         ║");
                        System.out.println("╚═════════════════════════════════════════════╝");
                    } else {
                        System.out.println("\n╔════════════════════════════════════════╗");
                        System.out.println("║        PRONTUARIOS ATENDIDOS           ║");
                        System.out.println("╚════════════════════════════════════════╝");

                        historico.stream()

                                .filter(p -> p.getStatus() == Status.ATENDIDO)

                                .sorted(Comparator.comparing(p -> p.getNome().toLowerCase()))

                                .forEach(p -> System.out.println(p.toprontuario()));

                    }
                }

                // Imprime os Prontuarios que foram cancelados antes de serem atendidos

                case "3" -> {
                    System.out.println("\n╔════════════════════════════════════════╗");
                    System.out.println("║        PRONTUARIOS CANCELADOS          ║");
                    System.out.println("╚════════════════════════════════════════╝");

                    if (cancel.isEmpty()) {
                        System.out.println("\n╔═════════════════════════════════════════════╗");
                        System.out.println("║      Nenhum prontuario foi cancelado.       ║");
                        System.out.println("╚═════════════════════════════════════════════╝");
                        return;
                    }

                    for (Cancelamento c : cancel) {
                        System.out.println(c); // já imprime todos os que foram cancelados formatado com prontuario e
                                               // justificativa
                    }
                }

                case "4" -> {
                    System.out.println("\n╔══════════════════════════════════════╗");
                    System.out.println("║    Retornando ao menu principal.     ║");
                    System.out.println("╚══════════════════════════════════════╝");

                    return;
                }
            }
        }
    }

    /*
     * Solicita o nome do prontuario a ser cancelado, busca na fila e remove,
     * requerendo uma justificativa para o cancelamento.
     */

    private static void cancelarProntuario() {
        if (fila.estaVazia()) {

            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║   Nenhum prontuario na fila para cancelar. ║");
            System.out.println("╚════════════════════════════════════════════╝");

            return;
        }

        System.out.print("Digite o nome do prontuario a cancelar: ");

        String buscaNome = sc.nextLine().trim().toLowerCase();

        List<Prontuario> lista = fila.getElementos();

        Optional<Prontuario> prontuarioEncontrado = lista.stream()

                .filter(p -> p.getNome().toLowerCase().equals(buscaNome))

                .findFirst();

        if (prontuarioEncontrado.isEmpty()) {

            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║   Prontuario não encontrado na fila.   ║");
            System.out.println("╚════════════════════════════════════════╝");

            return;

        }

        Prontuario p = prontuarioEncontrado.get();

        System.out.print("Informe a justificativa para o cancelamento: ");

        String justificativa = validarStringVazia();

        // Usa método da classe Cancelamento
        Cancelamento.cancelarProntuario(p, justificativa, fila);

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║   Prontuário cancelado com sucesso.  ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    /*
     * Lê uma string do usuário que não pode ser vazia. Se tiver vazia repete o
     * ciclo se não segue o codigo
     */

    private static String validarStringVazia() {

        String entrada;

        while (true) {
            entrada = sc.nextLine().trim();

            if (!entrada.isEmpty()) {

                return entrada;

            }
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║   Entrada inválida. Tente novamente.   ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.print("Digite novamente: ");
        }
    }

    /*
     * Se o numero lido for positivo ele o metodo o retorna se não ele pede que
     * insira novamente.
     * E o mesmo se não for um número
     */

    private static int lerIntValido() {

        while (true) {

            String entrada = sc.nextLine().trim();

            try {

                int valor = Integer.parseInt(entrada);

                if (valor >= 0) {

                    return valor;

                } else {
                    System.out.println("\n╔══════════════════════════════════════╗");
                    System.out.println("║     Digite um número positivo.       ║");
                    System.out.println("╚══════════════════════════════════════╝");
                    System.out.print("Tente novamente: ");
                }

            } catch (NumberFormatException e) {

                System.out.println("\n╔══════════════════════════════════════╗");
                System.out.println("║  Entrada inválida. Digite um número. ║");
                System.out.println("╚══════════════════════════════════════╝");
                System.out.print("Tente novamente: ");
            }
        }
    }

    // Lê uma resposta do usuário que deve ser 's' , 'p' ou 'x'. e retorna eles
    // validados.

    private static String ValidarConfirmacao() {

        while (true) {

            String resp = sc.nextLine().trim().toLowerCase();

            if (resp.equals("s") || resp.equals("p") || resp.equals("x")) {

                return resp;

            }
            System.out.println("\n╔═════════════════════════════════════════╗");
            System.out.println("║   Resposta inválida. Digite s, p ou x.  ║");
            System.out.println("╚═════════════════════════════════════════╝");
            System.out.print("\nDigite novamente: ");
        }
    }

    // Adiciona Prontuarios no inicio do codigo

    public static void PreAddProntuario(HeapProntuario fila) {

        fila.inserir(new Prontuario("Ana Silva", 34, "Dor no peito", "vermelho"));

        fila.inserir(new Prontuario("Carlos Lima", 60, "Febre alta", "amarelo"));

        fila.inserir(new Prontuario("Beatriz Costa", 22, "Tosse leve", "verde"));

        fila.inserir(new Prontuario("João Pedro", 80, "Inconsciência", "preto"));

    }

    public static void AddProntuarioCancelado(HeapProntuario fila) {

        Prontuario cancelado = new Prontuario("Carlos Alberto", 23, "Fatiga", "amarelo");

        fila.inserir(cancelado);

        Cancelamento.cancelarProntuario(cancelado, "Paciente desistiu do atendimento.", fila);
    }

}