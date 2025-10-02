package vallegrande.edu.pe.service;

import vallegrande.edu.pe.model.Pedido;
import vallegrande.edu.pe.model.PedidoDAO;
import java.util.List;

public class PedidoService {
    private PedidoDAO dao = new PedidoDAO();

    public List<Pedido> obtenerPedidos() {
        return dao.listar();
    }

    public Pedido buscarPedido(int id) {
        return dao.buscarPorId(id);
    }

    public boolean agregarPedido(Pedido pedido) {
        return dao.insertar(pedido);
    }

    public boolean actualizarPedido(Pedido pedido) {
        return dao.actualizar(pedido);
    }

    public boolean actualizarEstadoPedido(int id, String estado) {
        return dao.actualizarEstado(id, estado);
    }

    public boolean eliminarPedido(int id) {
        return dao.eliminar(id);
    }

    public boolean validarPedido(Pedido pedido) {
        if (pedido.getClienteNombre() == null || pedido.getClienteNombre().trim().isEmpty()) {
            return false;
        }
        if (pedido.getClienteTelefono() == null || pedido.getClienteTelefono().trim().isEmpty()) {
            return false;
        }
        if (pedido.getClienteDireccion() == null || pedido.getClienteDireccion().trim().isEmpty()) {
            return false;
        }
        if (pedido.getProducto() == null || pedido.getProducto().trim().isEmpty()) {
            return false;
        }
        if (pedido.getCantidad() <= 0) {
            return false;
        }
        if (pedido.getPrecio() <= 0) {
            return false;
        }
        return true;
    }

    // Métodos para estadísticas
    public double calcularIngresosTotales(List<Pedido> pedidos) {
        return pedidos.stream()
                .filter(p -> !"Cancelado".equals(p.getEstado()))
                .mapToDouble(Pedido::getTotal)
                .sum();
    }

    public long contarPedidosPorEstado(List<Pedido> pedidos, String estado) {
        return pedidos.stream()
                .filter(p -> estado.equals(p.getEstado()))
                .count();
    }
}