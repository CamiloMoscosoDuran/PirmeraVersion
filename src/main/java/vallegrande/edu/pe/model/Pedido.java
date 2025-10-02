package vallegrande.edu.pe.model;

import java.time.LocalDateTime;

public class Pedido {
    private int id;
    private String clienteNombre;
    private String clienteTelefono;
    private String clienteDireccion;
    private String producto;
    private String categoria;
    private int cantidad;
    private double precio;
    private String estado;
    private String observaciones;
    private LocalDateTime fechaPedido;

    // Constructor completo
    public Pedido(int id, String clienteNombre, String clienteTelefono, String clienteDireccion,
                  String producto, String categoria, int cantidad, double precio,
                  String estado, String observaciones, LocalDateTime fechaPedido) {
        this.id = id;
        this.clienteNombre = clienteNombre;
        this.clienteTelefono = clienteTelefono;
        this.clienteDireccion = clienteDireccion;
        this.producto = producto;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.precio = precio;
        this.estado = estado;
        this.observaciones = observaciones;
        this.fechaPedido = fechaPedido;
    }

    // Constructor para nuevos pedidos
    public Pedido(String clienteNombre, String clienteTelefono, String clienteDireccion,
                  String producto, String categoria, int cantidad, double precio,
                  String observaciones) {
        this.clienteNombre = clienteNombre;
        this.clienteTelefono = clienteTelefono;
        this.clienteDireccion = clienteDireccion;
        this.producto = producto;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.precio = precio;
        this.estado = "Recibido";
        this.observaciones = observaciones;
        this.fechaPedido = LocalDateTime.now();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteTelefono() { return clienteTelefono; }
    public void setClienteTelefono(String clienteTelefono) { this.clienteTelefono = clienteTelefono; }

    public String getClienteDireccion() { return clienteDireccion; }
    public void setClienteDireccion(String clienteDireccion) { this.clienteDireccion = clienteDireccion; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public double getTotal() {
        return cantidad * precio;
    }

    @Override
    public String toString() {
        return String.format("Pedido #%d - %s: %s", id, clienteNombre, producto);
    }
}