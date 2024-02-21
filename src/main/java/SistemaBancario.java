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
            // Obter ID do cliente
            System.out.println("Digite seu ID:");
            int idOrigem = scanner.nextInt();

            // Verificar se o cliente existe
            double saldoAtual = obterSaldo(idOrigem, connection);
            if (saldoAtual == -1) {
                System.out.println("Cliente não encontrado.");
                return;
            }

            // Confirmar se o usuário deseja realizar a transferência
            System.out.println("Você é " + obterNomeCliente(idOrigem, connection) + "?");
            System.out.println("1 para sim, 2 para não:");
            int opcao = scanner.nextInt();
            if (opcao != 1) {
                System.out.println("Transferência cancelada.");
                return;
            }

            // Solicitar senha
            System.out.println("Digite sua senha:");
            String senha = scanner.next();

            // Verificar se a senha está correta
            if (!verificarSenha(idOrigem, senha, connection)) {
                System.out.println("Senha incorreta. Transferência cancelada.");
                return;
            }

            // Continuar com a transferência
            System.out.println("Digite o valor a ser transferido:");
            double valorDaTransacao = scanner.nextDouble();

            System.out.println("Digite o ID de destino:");
            int idDestino = scanner.nextInt();

            // Verificar se há saldo suficiente antes de realizar a transação
            if (saldoAtual >= valorDaTransacao) {
                realizarTransferencia(idOrigem, idDestino, valorDaTransacao, connection);
                System.out.println("Transferência realizada com sucesso.");
            } else {
                System.out.println("Saldo insuficiente para realizar a transação.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static double obterSaldo(int idCliente, Connection connection) throws SQLException {
        String sql = "SELECT saldo FROM clientes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("saldo");
            } else {
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
    }
}
