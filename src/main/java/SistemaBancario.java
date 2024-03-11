import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Calendar;
import java.util.Scanner;

public class SistemaBancario {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Bem vindo ao nosso sistema bancário.");
        // Conexão com o banco de Dados.
        try (Connection connection = conexaoBanco.obterConexao()) {

            System.out.println("Digite seu ID:");
            int idOrigem = scanner.nextInt();
            if (!verificarExistenciaCliente(idOrigem, connection)) {
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
            System.out.println("3 - Ver Histórico de Transferências");
            System.out.println("0 - Sair");
            escolha = scanner.nextInt();

            switch (escolha) {
                case 1:
                    obterSaldo(idOrigem, connection);
                    break;
                case 2:
                    realizarTransferencia(idOrigem, connection);
                    break;
                case 3:
                    exibirHistoricoTransferencias(idOrigem, connection);
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

    private static void realizarTransferencia(int idOrigem, Connection connection) throws SQLException {
        String sql = "UPDATE clientes SET saldo = saldo - ? WHERE id = ?";
        System.out.println("Digite o ID do destinatário:");
        int idDestino = scanner.nextInt();

        System.out.println("Digite o valor a ser transferido:");
        double valor = scanner.nextDouble();

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
            // Insere uma entrada no histórico de transferências
            String nomeOrigem = obterNomeCliente(idOrigem, connection);
            String nomeDestino = obterNomeCliente(idDestino, connection);
            inserirHistoricoTransferencia(idOrigem, idDestino, nomeOrigem, nomeDestino, valor, connection);
        }
        System.out.println("Tranferência realizada com sucesso!");
    }
    private static void inserirHistoricoTransferencia(int idOrigem, int idDestino, String nomeOrigem, String nomeDestino, double valor, Connection connection) throws SQLException {
        java.util.Date dataAtual = Calendar.getInstance().getTime();
        Date dataSQL = new Date(dataAtual.getTime());

        String sql = "INSERT INTO historico_transferencias (id_origem, nome_origem, id_destino, nome_destino, valor_transferido, data_transferencia) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idOrigem);
            stmt.setString(2, nomeOrigem);
            stmt.setInt(3, idDestino);
            stmt.setString(4, nomeDestino);
            stmt.setDouble(5, valor);
            stmt.setDate(6, dataSQL);
            stmt.executeUpdate();
        }
    }

    private static void exibirHistoricoTransferencias(int idCliente, Connection connection) throws SQLException {
        String sql = "SELECT * FROM historico_transferencias WHERE id_origem = ? OR id_destino = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.setInt(2, idCliente);
            ResultSet rs = stmt.executeQuery();

            System.out.println("Histórico de Transferências:");
            while (rs.next()) {
                int idTransferencia = rs.getInt("id_transferencia");
                int idOrigem = rs.getInt("id_origem");
                String nomeOrigem = rs.getString("nome_origem");
                int idDestino = rs.getInt("id_destino");
                String nomeDestino = rs.getString("nome_destino");
                double valorTransferido = rs.getDouble("valor_transferido");
                Date dataTransferencia = rs.getDate("data_transferencia");

                System.out.println("ID Transferência: " + idTransferencia);
                System.out.println("Origem: " + nomeOrigem + " (ID: " + idOrigem + ")");
                System.out.println("Destino: " + nomeDestino + " (ID: " + idDestino + ")");
                System.out.println("Valor Transferido: " + valorTransferido);
                System.out.println("Data da Transferência: " + dataTransferencia);
                System.out.println();
            }
        }
    }

    private static boolean verificarExistenciaCliente(int idCliente, Connection connection) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}
