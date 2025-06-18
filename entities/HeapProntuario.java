package entities;

import java.util.ArrayList;

import java.util.List;

/* Classe que implementa uma fila de prioridade (heap mínimo) para organizar prontuários
  com base no risco do paciente. Quanto mais grave, mais no topo da fila.*/

public class HeapProntuario {

    private List<Prontuario> heap;

    public HeapProntuario() {
        heap = new ArrayList<>();
    }

    // Insere um novo prontuário na fila, respeitando a ordem de prioridade.

    public void inserir(Prontuario paciente) {
        heap.add(paciente);
        subir(heap.size() - 1);
    }

    // Remove o primeiro elemento da fila (o de maior prioridade).

    public Prontuario remover() {

        if (estaVazia())
            return null;

        Prontuario raiz = heap.get(0);

        Prontuario ultimo = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {

            heap.set(0, ultimo);

            descer(0);

        }

        return raiz;

    }

    // Remove um prontuário específico da fila, se ele estiver presente.

    public void remover(Prontuario paciente) {

        int index = heap.indexOf(paciente);

        if (index == -1)
            return; // Se não encontrou, sai

        Prontuario ultimo = heap.remove(heap.size() - 1);

        if (index < heap.size()) {

            heap.set(index, ultimo);

            ajustar(index);
        }

    }

    // Retorna (sem remover) o próximo prontuário a ser atendido

    public Prontuario peek() {

        return estaVazia() ? null : heap.get(0);

    }

    // Verifica se a fila está vazia.

    public boolean estaVazia() {

        return heap.isEmpty();

    }
    // Retorna uma cópia da lista atual de prontuários na heap.

    public List<Prontuario> getElementos() {

        return new ArrayList<>(heap);

    }

    // Reorganiza a heap "subindo" o elemento que acabou de ser inserido,

    private void subir(int index) {

        while (index > 0) {

            int pai = (index - 1) / 2;

            if (heap.get(index).compareTo(heap.get(pai)) < 0) {

                trocar(index, pai);

                index = pai;

            } else
                break;

        }

    }

    // Reorganiza a heap "descendo" um elemento para a posição correta.

    private void descer(int index) {

        int tamanho = heap.size();

        while (index < tamanho) {

            int menor = index;

            int esq = 2 * index + 1;

            int dir = 2 * index + 2;

            if (esq < tamanho && heap.get(esq).compareTo(heap.get(menor)) < 0)
                menor = esq;

            if (dir < tamanho && heap.get(dir).compareTo(heap.get(menor)) < 0)
                menor = dir;

            if (menor != index) {

                trocar(index, menor);

                index = menor;

            } else

                break;

        }

    }

    // Após substituir um elemento, ajusta ele na posição correta subindo ou descendo.

    private void ajustar(int index) {
       
        subir(index);
       
        descer(index);
    
    }

    // Troca dois elementos de lugar na heap.
    
    private void trocar(int i, int j) {
    
        Prontuario temp = heap.get(i);
    
        heap.set(i, heap.get(j));
    
        heap.set(j, temp);
    }
}
