# Sistema Bancário

Bem-vindo ao **Sistema Bancário**, um projeto simples em Java para realizar operações bancárias básicas. Este sistema permite que os usuários verifiquem saldos, enviem dinheiro para outros clientes e realizem outras operações relacionadas a uma conta bancária.

## Configuração

1. Abra o projeto em sua IDE favorita (por exemplo, Eclipse, IntelliJ).
2. Configure as informações do banco de dados no arquivo `conexaoBanco.java`.

## Uso

1. Execute a classe `SistemaBancario.java` para iniciar o sistema.
2. Insira o ID do cliente para acessar o sistema bancário.
3. Selecione a opção desejada no menu.

   - **Opção 1: Verificar Saldo**
     - Exibe o saldo atual da conta do cliente.

   - **Opção 2: Enviar Dinheiro**
     - Solicita o ID do destinatário e o valor a ser transferido.
     - Atualiza os saldos das contas envolvidas na transferência.

   - **Opção 0: Sair**
     - Encerra o sistema bancário.

## Funcionalidades Adicionais

- **Segurança:** O sistema exige a inserção de uma senha para realizar operações, proporcionando uma camada adicional de segurança.
  
- **Verificação de Cliente:** Antes de realizar operações, o sistema verifica se o cliente existe no banco de dados.

## Contribuições

Sinta-se à vontade para contribuir para este projeto! Se você encontrar problemas ou tiver melhorias a sugerir, abra uma issue ou envie um pull request.
