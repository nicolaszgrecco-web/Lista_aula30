# 📦 Almoxarifado StarLog — CRUD completo com Padrão DAO (Java + SQLite)

Projeto da **Aula 30** (CRUD de negócio de ponta a ponta com DAO).

## Estrutura

```
src/main/java/br/com/starlog/
├── Main.java                   # Simulação do ciclo completo (C, R, U, D)
├── model/
│   └── Produto.java            # Entidade + invariantes de domínio
└── dao/
    ├── ProdutoDAO.java         # Contrato CRUD (5 métodos)
    ├── ProdutoDAOSqlite.java   # Implementação JDBC/SQLite
    └── ConexaoFactory.java     # Fábrica de conexões
```

## Como executar

Requisitos: **JDK 17+** e **Maven 3.8+**.

```bash
mvn compile exec:java
```

O arquivo `starlog.db` é criado automaticamente. Apague-o para recomeçar com IDs 1 e 2.

## Decisões de projeto

- **`Produto`**: lança `IllegalArgumentException` se o nome for nulo ou tiver menos de 3 caracteres, se o preço for ≤ 0 ou se o estoque for negativo. Os setters `setPreco` e `setEstoque` reaplicam as mesmas validações, para que a entidade nunca fique em estado inválido.
- **`ProdutoDAO`**: `salvar`, `buscarPorId` (`Optional`), `listarTodos`, `atualizar` e `excluir` (ambos retornam `boolean`).
- **`ProdutoDAOSqlite`**: cria a tabela no construtor; usa `PreparedStatement` e `try-with-resources`; `atualizar` e `excluir` usam `WHERE id = ?` e retornam `linhasAfetadas > 0`; `listarTodos` usa `ORDER BY nome ASC`.
- **`Main`**: cria 2 produtos, lista, atualiza o teclado (estoque 15, preço 289.90), exclui o mousepad, tenta excluir o ID 888 (retorna `false` sem travar) e confirma que sobrou 1 produto.

## Respostas das questões teóricas

| Questão | Resposta | Justificativa |
|---|---|---|
| 1 — Origem do CRUD | **A** | Formalizado por James Martin (1983); resume o ciclo de vida dos dados em sistemas OLTP: Create/INSERT, Read/SELECT, Update/UPDATE, Delete/DELETE. |
| 2 — Retorno de `executeUpdate()` | **C** | O inteiro é o número de linhas afetadas; `> 0` confirma que o registro existia e foi modificado, evitando falsos positivos na interface. |
| 3 — DELETE sem WHERE | **D** | Todas as linhas da tabela são excluídas, esvaziando o estoque. O banco não reclama e o Java não detecta isso. |
| 4 — Fluxo MVC | **B** | A View coleta os dados e repassa ao Controller, que valida, cria a entidade e chama o DAO, que grava via JDBC; a View só recarrega. |
