package vallegrande.edu.pe.model;

import vallegrande.edu.pe.database.ConexionBD;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {
    // Consultas SQL
    private static final String SQL_SELECT = "SELECT * FROM pedidos ORDER BY fecha_pedido DESC";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM pedidos WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO pedidos (cliente_nombre, cliente_telefono, cliente_direccion, producto, categoria, cantidad, precio, estado, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE pedidos SET cliente_nombre=?, cliente_telefono=?, cliente_direccion=?, producto=?, categoria=?, cantidad=?, precio=?, estado=?, observaciones=? WHERE id=?";
    private static final String SQL_DELETE = "DELETE FROM pedidos WHERE id = ?";
    private static final String SQL_UPDATE_ESTADO = "UPDATE pedidos SET estado = ? WHERE id = ?";

    // Listar todos los pedidos
    public List<Pedido> listar() {
        List<Pedido> pedidos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        rs.getString("cliente_nombre"),
                        rs.getString("cliente_telefono"),
                        rs.getString("cliente_direccion"),
                        rs.getString("producto"),
                        rs.getString("categoria"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio"),
                        rs.getString("estado"),
                        rs.getString("observaciones"),
                        rs.getTimestamp("fecha_pedido").toLocalDateTime()
                );
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en listar(): " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return pedidos;
    }

    // Buscar por ID
    public Pedido buscarPorId(int id) {
        Pedido pedido = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.getConexion();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                pedido = new Pedido(
                        rs.getInt("id"),
                        rs.getString("cliente_nombre"),
                        rs.getString("cliente_telefono"),
                        rs.getString("cliente_direccion"),
                        rs.getString("producto"),
                        rs.getString("categoria"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio"),
                        rs.getString("estado"),
                        rs.getString("observaciones"),
                        rs.getTimestamp("fecha_pedido").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en buscarPorId(): " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
        return pedido;
    }

    // Insertar nuevo pedido
    public boolean insertar(Pedido pedido) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;

        try {
            conn = ConexionBD.getConexion();
            stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, pedido.getClienteNombre());
            stmt.setString(2, pedido.getClienteTelefono());
            stmt.setString(3, pedido.getClienteDireccion());
            stmt.setString(4, pedido.getProducto());
            stmt.setString(5, pedido.getCategoria());
            stmt.setInt(6, pedido.getCantidad());
            stmt.setDouble(7, pedido.getPrecio());
            stmt.setString(8, pedido.getEstado());
            stmt.setString(9, pedido.getObservaciones());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    pedido.setId(generatedKeys.getInt(1));
                }
                exito = true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error en insertar(): " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return exito;
    }

    // Actualizar pedido
    public boolean actualizar(Pedido pedido) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;

        try {
            conn = ConexionBD.getConexion();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, pedido.getClienteNombre());
            stmt.setString(2, pedido.getClienteTelefono());
            stmt.setString(3, pedido.getClienteDireccion());
            stmt.setString(4, pedido.getProducto());
            stmt.setString(5, pedido.getCategoria());
            stmt.setInt(6, pedido.getCantidad());
            stmt.setDouble(7, pedido.getPrecio());
            stmt.setString(8, pedido.getEstado());
            stmt.setString(9, pedido.getObservaciones());
            stmt.setInt(10, pedido.getId());

            int filasAfectadas = stmt.executeUpdate();
            exito = (filasAfectadas > 0);
        } catch (SQLException e) {
            System.err.println("❌ Error en actualizar(): " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return exito;
    }

    // Actualizar solo el estado
    public boolean actualizarEstado(int id, String estado) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;

        try {
            conn = ConexionBD.getConexion();
            stmt = conn.prepareStatement(SQL_UPDATE_ESTADO);
            stmt.setString(1, estado);
            stmt.setInt(2, id);

            int filasAfectadas = stmt.executeUpdate();
            exito = (filasAfectadas > 0);
        } catch (SQLException e) {
            System.err.println("❌ Error en actualizarEstado(): " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return exito;
    }

    // Eliminar pedido
    public boolean eliminar(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;

        try {
            conn = ConexionBD.getConexion();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, id);

            int filasAfectadas = stmt.executeUpdate();
            exito = (filasAfectadas > 0);
        } catch (SQLException e) {
            System.err.println("❌ Error en eliminar(): " + e.getMessage());
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
        return exito;
    }

    // Método auxiliar para cerrar recursos
    private void cerrarRecursos(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}