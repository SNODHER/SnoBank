import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class CadastroCliente {

    public static void main(String[] args) {
        try (Connection connection = conexaoBanco.obterConexao()) {
            String sqlInserirCliente = "INSERT INTO clientes (nome, tipo_conta, saldo, senha, email, telefone, sexo, data_nascimento, nacionalidade, cpf, cep, estado) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

            inserirCliente(connection, sqlInserirCliente, nome, tipoConta, saldo, senha,
                    email, telefone, sexo, dataNascimento, nacionalidade, cpf, cep, estado);
            System.out.println("Cliente cadastrado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void inserirCliente(Connection connection, String sql, String nome, String tipoConta, double saldo, String senha,
                                       String email, String telefone, String sexo, LocalDate dataNascimento, String nacionalidade,
                                       String cpf, String cep, String estado) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, tipoConta);
            preparedStatement.setDouble(3, saldo);
            preparedStatement.setString(4, senha);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, telefone);
            preparedStatement.setString(7, sexo);
            preparedStatement.setDate(8, java.sql.Date.valueOf(dataNascimento));
            preparedStatement.setString(9, nacionalidade);
            preparedStatement.setString(10, cpf);
            preparedStatement.setString(11, cep);
            preparedStatement.setString(12, estado);
            preparedStatement.executeUpdate();
        }
    }
}
