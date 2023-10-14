import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class consulta {
    public static void main(String[] args) {
        Connection conexion = null;
        try {
            // Establecer la conexión con la base de datos
            String url = "jdbc:mariadb://localhost:3306/bdclientes";
            String usuario = "root";
            String contraseña = "6321";
            conexion = DriverManager.getConnection(url, usuario, contraseña);

            System.out.println("Conexión exitosa a la base de datos");

            List<Cliente> listaClientes = new ArrayList<>();

            // Crear un menú
            while (true) {
                System.out.println("\nMenú:");
                System.out.println("1. Realizar consulta");
                System.out.println("2. Cargar lista");
                System.out.println("3. Desplegar lista");
                System.out.println("4. Salir");

                Scanner scanner = new Scanner(System.in);
                int opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        realizarConsulta(conexion);
                        break;
                    case 2:
                        listaClientes = cargarLista(conexion);
                        System.out.println("Datos cargados en la lista.");
                        break;
                    case 3:
                        if (!listaClientes.isEmpty()) {
                            desplegarLista(listaClientes);
                        } else {
                            System.out.println("La lista está vacía. Cargue datos primero.");
                        }
                        break;
                    case 4:
                        conexion.close();
                        System.exit(0);
                    default:
                        System.out.println("Opción no válida. Por favor, elija una opción válida.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al conectarse a la base de datos: " + e.getMessage());
        }
    }

    public static void realizarConsulta(Connection conexion) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el NIT del cliente: ");
        String nit = scanner.next();

        try {
            String consulta = "SELECT * FROM clientes WHERE nit = ?";
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);
            preparedStatement.setString(1, nit);
            ResultSet resultado = preparedStatement.executeQuery();

            if (resultado.next()) {
                System.out.println("Datos del cliente:");
                System.out.println("NIT: " + resultado.getString("nit"));
                System.out.println("Nombre: " + resultado.getString("nombre"));
            } else {
                System.out.println("No se encontraron resultados para ese NIT.");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error al realizar la consulta: " + e.getMessage());
        }
    }

    public static List<Cliente> cargarLista(Connection conexion) {
        List<Cliente> listaClientes = new ArrayList<>();
        try {
            String consulta = "SELECT * FROM clientes";
            Statement statement = conexion.createStatement();
            ResultSet resultado = statement.executeQuery(consulta);

            while (resultado.next()) {
                String nit = resultado.getString("nit");
                String nombre = resultado.getString("nombre");
                listaClientes.add(new Cliente(nit, nombre));
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println("Error al cargar la lista: " + e.getMessage());
        }

        return listaClientes;
    }

    public static void desplegarLista(List<Cliente> listaClientes) {
        System.out.println("Lista de clientes:");
        for (Cliente cliente : listaClientes) {
            System.out.println("NIT: " + cliente.getNit() + ", Nombre: " + cliente.getNombre());
        }
    }
}

