package br.com.starlog;

import br.com.starlog.dao.ProdutoDAO;
import br.com.starlog.dao.ProdutoDAOSqlite;
import br.com.starlog.model.Produto;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ProdutoDAO dao = new ProdutoDAOSqlite();

        // [Create]
        System.out.println("=== [Create] Cadastrando produtos ===");
        Produto teclado = new Produto("Teclado Mecânico RGB", 249.90, 10);
        Produto mousepad = new Produto("Mousepad Speed XL", 59.90, 30);
        dao.salvar(teclado);
        dao.salvar(mousepad);
        System.out.println("ID gerado (teclado): " + teclado.getId());
        System.out.println("ID gerado (mousepad): " + mousepad.getId());

        // [Read]
        System.out.println("\n=== [Read] Catálogo inicial ===");
        imprimirTabela(dao.listarTodos());

        // [Update]
        System.out.println("\n=== [Update] Atualizando o teclado ===");
        teclado.setEstoque(15);
        teclado.setPreco(289.90);
        boolean atualizado = dao.atualizar(teclado);
        System.out.println("atualizar() retornou: " + atualizado);
        dao.buscarPorId(teclado.getId()).ifPresent(p -> System.out.println("Registro atual: " + p));

        // [Delete]
        System.out.println("\n=== [Delete] Removendo o mousepad ===");
        boolean removido = dao.excluir(mousepad.getId());
        System.out.println("excluir(" + mousepad.getId() + ") retornou: " + removido);

        boolean removidoInexistente = dao.excluir(888L);
        System.out.println("excluir(888) retornou: " + removidoInexistente
                + (removidoInexistente ? "" : " -> ID inexistente, nada foi apagado (sem erro)."));

        // [Final State]
        System.out.println("\n=== [Final State] Catálogo final ===");
        List<Produto> finais = dao.listarTodos();
        imprimirTabela(finais);
        System.out.println("Total de produtos restantes: " + finais.size());
    }

    private static void imprimirTabela(List<Produto> produtos) {
        System.out.printf("%-4s | %-24s | %10s | %7s%n", "ID", "NOME", "PREÇO", "ESTOQUE");
        System.out.println("-".repeat(54));
        for (Produto p : produtos) {
            System.out.printf("%-4d | %-24s | R$ %7.2f | %7d%n",
                    p.getId(), p.getNome(), p.getPreco(), p.getEstoque());
        }
    }
}
