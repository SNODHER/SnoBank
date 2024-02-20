import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class CadastroCliente {

    public static void main(String[] args) {
        try (Connection connection = conexaoBanco.obterConexao()) {
            String sqlVerificarId = "SELECT id FROM clientes WHERE id = ?";
            String sqlInserirCliente = "INSERT INTO clientes (id, nome, tipo_conta, saldo, senha, email, telefone, sexo, data_nascimento, nacionalidade, cpf, cep, estado) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            Scanner scanner = new Scanner(System.in);
            System.out.println("Digite o nome do cliente:");
            String nome = scanner.nextLine();
            System.out.println("Digite o tipo de conta:");
            String tipoConta = scanner.nextLine();
            System.out.println("Digite o saldo inicial:");
            double saldo = scanner.nextDouble();
            System.out.println("Digite sua senha:");
            String senha = scanner.next();
            System.out.println("Digite o email:");
            String email = scanner.next();
            System.out.println("Digite o telefone:");
            String telefone = scanner.next();
            System.out.println("Digite o sexo:");
            String sexo = scanner.next();
            System.out.println("Digite a data de nascimento (formato YYYY-MM-DD):");
            LocalDate dataNascimento = LocalDate.parse(scanner.next());
            System.out.println("Digite a nacionalidade:");
            String nacionalidade = scanner.next();
            System.out.println("Digite o CPF:");
            String cpf = scanner.next();
            System.out.println("Digite o CEP:");
            String cep = scanner.next();
            System.out.println("Digite o estado:");
            String estado = scanner.next();

            int novoId = encontrarIdDisponivel(connection, sqlVerificarId);

            inserirCliente(connection, sqlInserirCliente, novoId, nome, tipoConta, saldo, senha,
                    email, telefone, sexo, dataNascimento, nacionalidade, cpf, cep, estado);

            System.out.println("Cliente cadastrado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int encontrarIdDisponivel(Connection connection, String sql) throws SQLException {
        int novoId = 0;

        while (idOcupado(connection, sql, novoId)) {
            novoId++;
        }

        return novoId;
    }

    private static boolean idOcupado(Connection connection, String sql, int id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private static void inserirCliente(Connection connection, String sql, int id, String nome, String tipoConta, double saldo, String senha,
                                       String email, String telefone, String sexo, LocalDate dataNascimento, String nacionalidade,
                                       String cpf, String cep, String estado) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, nome);
            preparedStatement.setString(3, tipoConta);
            preparedStatement.setDouble(4, saldo);
            preparedStatement.setString(5, senha);
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, telefone);
            preparedStatement.setString(8, sexo);
            preparedStatement.setDate(9, java.sql.Date.valueOf(dataNascimento));
            preparedStatement.setString(10, nacionalidade);
            preparedStatement.setString(11, cpf);
            preparedStatement.setString(12, cep);
            preparedStatement.setString(13, estado);
            preparedStatement.executeUpdate();
        }
    }
}
