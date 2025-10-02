package vallegrande.edu.pe.controller;

import vallegrande.edu.pe.model.Pedido;
import vallegrande.edu.pe.service.PedidoService;
import vallegrande.edu.pe.view.FrmPedidos;
import java.util.List;

public class PedidoController {
    private PedidoService service;
    private FrmPedidos vista;

    public PedidoController(FrmPedidos vista) {
        this.vista = vista;
        this.service = new PedidoService();
    }

    public void cargarPedidos() {
        try {
            List<Pedido> lista = service.obtenerPedidos();
            vista.mostrarPedidos(lista);
            actualizarEstadisticas(lista);
        } catch (Exception e) {
            vista.mostrarError("Error al cargar pedidos: " + e.getMessage());
        }
    }

    public void agregarPedido(Pedido pedido) {
        try {
            if (service.validarPedido(pedido)) {
                if (service.agregarPedido(pedido)) {
                    vista.mostrarMensaje("✅ Pedido registrado exitosamente");
                    cargarPedidos();
                } else {
                    vista.mostrarError("❌ Error al registrar el pedido");
                }
            } else {
                vista.mostrarError("❌ Datos del pedido no válidos");
            }
        } catch (Exception e) {
            vista.mostrarError("Error: " + e.getMessage());
        }
    }

    public void actualizarPedido(Pedido pedido) {
        try {
            if (service.validarPedido(pedido)) {
                if (service.actualizarPedido(pedido)) {
                    vista.mostrarMensaje("✅ Pedido actualizado exitosamente");
                    cargarPedidos();
                } else {
                    vista.mostrarError("❌ Error al actualizar el pedido");
                }
            } else {
                vista.mostrarError("❌ Datos del pedido no válidos");
            }
        } catch (Exception e) {
            vista.mostrarError("Error: " + e.getMessage());
        }
    }

    public void actualizarEstadoPedido(int id, String estado) {
        try {
            if (service.actualizarEstadoPedido(id, estado)) {
                vista.mostrarMensaje("✅ Estado del pedido actualizado a: " + estado);
                cargarPedidos();
            } else {
                vista.mostrarError("❌ Error al actualizar el estado del pedido");
            }
        } catch (Exception e) {
            vista.mostrarError("Error: " + e.getMessage());
        }
    }

    public void eliminarPedido(int id) {
        try {
            if (service.eliminarPedido(id)) {
                vista.mostrarMensaje("✅ Pedido eliminado exitosamente");
                cargarPedidos();
            } else {
                vista.mostrarError("❌ Error al eliminar el pedido");
            }
        } catch (Exception e) {
            vista.mostrarError("Error: " + e.getMessage());
        }
    }

    private void actualizarEstadisticas(List<Pedido> pedidos) {
        double ingresosTotales = service.calcularIngresosTotales(pedidos);
        long pedidosRecibidos = service.contarPedidosPorEstado(pedidos, "Recibido");
        long pedidosPreparacion = service.contarPedidosPorEstado(pedidos, "En Preparación");
        long pedidosCamino = service.contarPedidosPorEstado(pedidos, "En Camino");
        long pedidosEntregados = service.contarPedidosPorEstado(pedidos, "Entregado");

        vista.actualizarEstadisticas(ingresosTotales, pedidosRecibidos, pedidosPreparacion,
                pedidosCamino, pedidosEntregados, pedidos.size());
    }

    public Pedido buscarPedido(int id) {
        try {
            return service.buscarPedido(id);
        } catch (Exception e) {
            vista.mostrarError("Error al buscar pedido: " + e.getMessage());
            return null;
        }
    }
}