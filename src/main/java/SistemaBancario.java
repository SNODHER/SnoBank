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
            String sqlSaldo = "SELECT saldo FROM clientes WHERE id = ?";
            String sqlAtualizarSaldo = "UPDATE clientes SET saldo = saldo - ? WHERE id = ?";
            PreparedStatement stmtSaldo = connection.prepareStatement(sqlSaldo);
            PreparedStatement stmtAtualizarSaldo = connection.prepareStatement(sqlAtualizarSaldo);

            System.out.println("Digite seu id:");
            int idOrigem = scanner.nextInt();
            stmtSaldo.setInt(1, idOrigem);
            ResultSet rs = stmtSaldo.executeQuery();

            double saldoAtual = 0.0;
            if (rs.next()) {
                saldoAtual = rs.getDouble("saldo");
            }

            System.out.println("Digite o valor a ser enviado?");
            double valorDaTransacao = scanner.nextDouble();

            if (saldoAtual >= valorDaTransacao) {

                stmtAtualizarSaldo.setDouble(1, valorDaTransacao);
                stmtAtualizarSaldo.setInt(2, idOrigem);
                stmtAtualizarSaldo.executeUpdate();

                System.out.println("Qual id de destino?");
                int idDestino = scanner.nextInt();
                stmtAtualizarSaldo.setDouble(1, -valorDaTransacao);
                stmtAtualizarSaldo.setInt(2, idDestino);
                stmtAtualizarSaldo.executeUpdate();

                System.out.println("Transação realizada com sucesso.");
            } else {
                System.out.println("Saldo insuficiente para realizar a transação.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
