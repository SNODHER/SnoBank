import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SistemaBancario {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Bem vindo ao nosso sistema bancário.");
        try (Connection connection = conexaoBanco.obterConexao()) {

            System.out.println("Digite seu ID:");
            int idOrigem = scanner.nextInt();

            double saldoAtual = obterSaldo(idOrigem, connection);
            if (saldoAtual == -1) {
                System.out.println("Cliente não encontrado.");
                return;
            }

            System.out.println("Você é " + obterNomeCliente(idOrigem, connection) + "?");
            System.out.println("1 para sim, 2 para não:");
            int opcao = scanner.nextInt();
            if (opcao != 1) {
                System.out.println("Operação cancelada.");
                return;
            }

            System.out.println("Digite sua senha:");
            String senha = scanner.next();

            if (!verificarSenha(idOrigem, senha, connection)) {
                System.out.println("Senha incorreta. Operação cancelada.");
                return;
            }


            exibirMenu(idOrigem, connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void exibirMenu(int idOrigem, Connection connection) throws SQLException {
        int escolha;
        do {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1 - Verificar Saldo");
            System.out.println("2 - Enviar Dinheiro");
            System.out.println("0 - Sair");
            escolha = scanner.nextInt();

            switch (escolha) {
                case 1:
                    obterSaldo(idOrigem, connection);
                    break;
                case 2:
                    System.out.println("Digite o ID do destinatário:");
                    int idDestino = scanner.nextInt();

                    System.out.println("Digite o valor a ser transferido:");
                    double valor = scanner.nextDouble();

                    realizarTransferencia(idOrigem, idDestino, valor, connection);
                    break;
                case 0:
                    System.out.println("Saindo do sistema bancário. Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        } while (escolha != 0);
    }

    private static double obterSaldo(int idsaldo, Connection connection) throws SQLException {
        System.out.println("Obtendo saldo para o cliente com ID: " + idsaldo);
        String sql = "SELECT saldo FROM clientes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idsaldo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double saldo = rs.getDouble("saldo");
                System.out.println("Saldo encontrado: " + saldo);
                return saldo;
            } else {
                System.out.println("Cliente não encontrado.");
                return -1; // Cliente não encontrado
            }
        }
    }

    private static String obterNomeCliente(int idCliente, Connection connection) throws SQLException {
        String sql = "SELECT nome FROM clientes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("nome");
            } else {
                return "Cliente não encontrado";
            }
        }
    }

    private static boolean verificarSenha(int idCliente, String senha, Connection connection) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ? AND senha = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    private static void realizarTransferencia(int idOrigem, int idDestino, double valor, Connection connection) throws SQLException {
        String sql = "UPDATE clientes SET saldo = saldo - ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, valor);
            stmt.setInt(2, idOrigem);
            stmt.executeUpdate();
        }

        sql = "UPDATE clientes SET saldo = saldo + ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, valor);
            stmt.setInt(2, idDestino);
            stmt.executeUpdate();
        }
        System.out.println();
    }

}
