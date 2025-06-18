package entities;

import java.util.ArrayList;
import java.util.List;

public class HeapProntuario {
    private List<Prontuario> heap;

    public HeapProntuario() {
        heap = new ArrayList<>();
    }

    public void inserir(Prontuario paciente) {
        heap.add(paciente);
        subir(heap.size() - 1);
    }

    public Prontuario remover() {
        if (estaVazia()) return null;
        Prontuario raiz = heap.get(0);
        Prontuario ultimo = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, ultimo);
            descer(0);
        }
        return raiz;
    }

    public void remover(Prontuario paciente) {
        int index = heap.indexOf(paciente);
        if (index == -1) return;
        Prontuario ultimo = heap.remove(heap.size() - 1);
        if (index < heap.size()) {
            heap.set(index, ultimo);
            ajustar(index);
        }
    }

    public Prontuario peek() {
        return estaVazia() ? null : heap.get(0);
    }

    public boolean estaVazia() {
        return heap.isEmpty();
    }

    public List<Prontuario> getElementos() {
        return new ArrayList<>(heap);
    }

    private void subir(int index) {
        while (index > 0) {
            int pai = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(pai)) < 0) {
                trocar(index, pai);
                index = pai;
            } else break;
        }
    }

    private void descer(int index) {
        int tamanho = heap.size();
        while (index < tamanho) {
            int menor = index;
            int esq = 2 * index + 1;
            int dir = 2 * index + 2;

            if (esq < tamanho && heap.get(esq).compareTo(heap.get(menor)) < 0) menor = esq;
            if (dir < tamanho && heap.get(dir).compareTo(heap.get(menor)) < 0) menor = dir;

            if (menor != index) {
                trocar(index, menor);
                index = menor;
            } else break;
        }
    }

    private void ajustar(int index) {
        subir(index);
        descer(index);
    }

    private void trocar(int i, int j) {
        Prontuario temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
