package vallegrande.edu.pe.view;

import vallegrande.edu.pe.controller.PedidoController;
import vallegrande.edu.pe.model.Pedido;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FrmPedidos extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtClienteNombre, txtClienteTelefono, txtClienteDireccion;
    private JTextField txtCantidad, txtPrecio, txtBuscar;
    private JTextArea txtObservaciones;
    private JComboBox<String> cmbCategoria, cmbProductosPredefinidos;
    private JButton btnAgregar, btnEditar, btnEliminar, btnLimpiar, btnBuscar;
    private JButton btnRecibido, btnPreparacion, btnCamino, btnEntregado;
    private PedidoController controlador;
    private JLabel lblIngresos, lblTotalPedidos, lblRecibidos, lblPreparacion, lblCamino, lblEntregados;

    // Colores para restaurante
    private final Color COLOR_PRIMARIO = new Color(220, 20, 60);
    private final Color COLOR_SECUNDARIO = new Color(255, 140, 0);
    private final Color COLOR_ACENTO = new Color(34, 139, 34);
    private final Color COLOR_FONDO = new Color(255, 250, 240);
    private final Color COLOR_TEXTO = new Color(51, 51, 51);

    // Opciones predefinidas
    private final String[] CATEGORIAS = {"Pollo", "Pizza", "Pasta"};
    private final String[] PRODUCTOS_POLLO = {
            "Pollo a la Brasa Familiar", "Pollo a la Brasa Mediano", "Alitas BBQ",
            "Alitas Picantes", "Pierna de Pollo", "Pechuga Grill", "Combo Familiar"
    };
    private final String[] PRODUCTOS_PIZZA = {
            "Pizza Hawaiana Grande", "Pizza Pepperoni Mediana", "Pizza 4 Estaciones",
            "Pizza Vegetariana", "Pizza Margarita", "Pizza Suprema", "Pizza Familiar"
    };
    private final String[] PRODUCTOS_PASTA = {
            "Lasagna de Carne", "Lasagna Vegetariana", "Spaghetti Bolognesa",
            "Fettuccine Alfredo", "Penne al Pesto", "Ravioli de Ricotta", "Tallarines Verdes"
    };

    // Precios predefinidos
    private final double[] PRECIOS_POLLO = {45.00, 28.00, 18.00, 18.00, 15.00, 20.00, 65.00};
    private final double[] PRECIOS_PIZZA = {42.00, 38.00, 48.00, 35.00, 32.00, 45.00, 55.00};
    private final double[] PRECIOS_PASTA = {32.00, 28.00, 25.00, 28.00, 26.00, 30.00, 22.00};

    public FrmPedidos() {
        initComponents();
        initController();
        cargarPedidosIniciales();
    }

    private void initComponents() {
        setTitle("🍗🍕🍝 Restaurante Sabores - Sistema de Pedidos Delivery");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_FONDO);

        // Panel superior
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);

        // Panel de estadísticas
        JPanel panelEstadisticas = crearPanelEstadisticas();
        add(panelEstadisticas, BorderLayout.WEST);

        // Panel central
        JPanel panelCentral = crearPanelCentral();
        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = crearPanelInferior();
        add(panelInferior, BorderLayout.SOUTH);

        configurarEventos();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("🍗🍕🍝 Restaurante Sabores - Delivery");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(COLOR_PRIMARIO);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBusqueda.setBackground(Color.WHITE);

        txtBuscar = new JTextField(20);
        txtBuscar.setText("🔍 Buscar pedidos por cliente...");
        txtBuscar.setForeground(Color.GRAY);

        btnBuscar = new JButton("Buscar");
        stylizeButton(btnBuscar, COLOR_SECUNDARIO);

        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);

        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(panelBusqueda, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_PRIMARIO, 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setPreferredSize(new Dimension(250, 0));

        JLabel lblTituloStats = new JLabel("📊 Estadísticas del Día");
        lblTituloStats.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTituloStats.setForeground(COLOR_PRIMARIO);
        lblTituloStats.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblIngresos = crearLabelEstadistica("💰 Ingresos: S/. 0.00", COLOR_ACENTO);
        lblTotalPedidos = crearLabelEstadistica("📦 Total Pedidos: 0", COLOR_PRIMARIO);
        lblRecibidos = crearLabelEstadistica("⏳ Recibidos: 0", new Color(0, 0, 0));
        lblPreparacion = crearLabelEstadistica("👨‍🍳 En Preparación: 0", new Color(0, 0, 0));
        lblCamino = crearLabelEstadistica("🚗 En Camino: 0", new Color(52, 152, 219));
        lblEntregados = crearLabelEstadistica("✅ Entregados: 0", COLOR_ACENTO);

        JButton btnActualizarStats = new JButton("🔄 Actualizar");
        stylizeButton(btnActualizarStats, COLOR_SECUNDARIO);
        btnActualizarStats.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnActualizarStats.addActionListener(e -> controlador.cargarPedidos());

        panel.add(lblTituloStats);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(lblIngresos);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblTotalPedidos);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(lblRecibidos);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(lblPreparacion);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(lblCamino);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(lblEntregados);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnActualizarStats);

        return panel;
    }

    private JLabel crearLabelEstadistica(String texto, Color color) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 20));
        panel.setBackground(COLOR_FONDO);

        JPanel panelControlesEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelControlesEstado.setBackground(Color.WHITE);
        panelControlesEstado.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_SECUNDARIO, 1), "🚀 Cambio Rápido de Estado"));

        btnRecibido = new JButton("🔄 Recibido");
        btnPreparacion = new JButton("👨‍🍳 En Preparación");
        btnCamino = new JButton("🚗 En Camino");
        btnEntregado = new JButton("✅ Entregado");

        stylizeButtonEstado(btnRecibido, new Color(255, 193, 7));
        stylizeButtonEstado(btnPreparacion, new Color(255, 140, 0));
        stylizeButtonEstado(btnCamino, new Color(52, 152, 219));
        stylizeButtonEstado(btnEntregado, COLOR_ACENTO);

        panelControlesEstado.add(btnRecibido);
        panelControlesEstado.add(btnPreparacion);
        panelControlesEstado.add(btnCamino);
        panelControlesEstado.add(btnEntregado);

        modelo = new DefaultTableModel(new String[]{
                "ID", "Cliente", "Teléfono", "Producto", "Categoría", "Cant", "Total", "Estado", "Fecha"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setSelectionBackground(COLOR_SECUNDARIO);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(COLOR_PRIMARIO);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_PRIMARIO, 1, true),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));

        panel.add(panelControlesEstado, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        panel.setBackground(COLOR_FONDO);

        JPanel panelFormulario = new JPanel(new BorderLayout(10, 10));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(
                        new LineBorder(COLOR_PRIMARIO, 2, true),
                        "📝 Gestión de Pedidos",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14),
                        COLOR_PRIMARIO
                ),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Panel de datos del cliente - CORREGIDO
        JPanel panelCliente = new JPanel(new GridLayout(2, 4, 10, 10));
        panelCliente.setBackground(Color.WHITE);

        // Inicializar campos ANTES de usarlos
        txtClienteNombre = new JTextField();
        txtClienteTelefono = new JTextField();
        txtClienteDireccion = new JTextField();

        addFormField(panelCliente, "Nombre del Cliente:", txtClienteNombre);
        addFormField(panelCliente, "Teléfono:", txtClienteTelefono);
        addFormField(panelCliente, "Dirección Delivery:", txtClienteDireccion);
        panelCliente.add(new JLabel());

        // Panel de datos del pedido - CORREGIDO
        JPanel panelPedido = new JPanel(new GridLayout(2, 4, 10, 10));
        panelPedido.setBackground(Color.WHITE);

        panelPedido.add(new JLabel("Categoría:"));
        cmbCategoria = new JComboBox<>(CATEGORIAS);
        stylizeComboBox(cmbCategoria);
        panelPedido.add(cmbCategoria);

        panelPedido.add(new JLabel("Producto:"));
        cmbProductosPredefinidos = new JComboBox<>();
        actualizarProductosPorCategoria();
        panelPedido.add(cmbProductosPredefinidos);

        // Inicializar campos ANTES de usarlos
        txtCantidad = new JTextField();
        txtPrecio = new JTextField(); // ¡ESTA ES LA LÍNEA CRÍTICA QUE FALTABA!

        addFormField(panelPedido, "Cantidad:", txtCantidad);
        addFormField(panelPedido, "Precio Unitario:", txtPrecio);

        // Panel de observaciones
        JPanel panelObservaciones = new JPanel(new BorderLayout(5, 5));
        panelObservaciones.setBackground(Color.WHITE);
        panelObservaciones.add(new JLabel("Observaciones:"), BorderLayout.NORTH);
        txtObservaciones = new JTextArea(3, 20);
        stylizeTextArea(txtObservaciones);
        panelObservaciones.add(new JScrollPane(txtObservaciones), BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(Color.WHITE);

        btnAgregar = new JButton("➕ Agregar Pedido");
        btnEditar = new JButton("✏️ Editar Pedido");
        btnEliminar = new JButton("🗑️ Eliminar Pedido");
        btnLimpiar = new JButton("🧹 Limpiar Formulario");

        stylizeButton(btnAgregar, COLOR_ACENTO);
        stylizeButton(btnEditar, COLOR_SECUNDARIO);
        stylizeButton(btnEliminar, new Color(231, 76, 60));
        stylizeButton(btnLimpiar, new Color(149, 165, 166));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        panelFormulario.add(panelCliente, BorderLayout.NORTH);
        panelFormulario.add(panelPedido, BorderLayout.CENTER);
        panelFormulario.add(panelObservaciones, BorderLayout.SOUTH);

        panel.add(panelFormulario, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        // Configurar valores iniciales DESPUÉS de crear todos los componentes
        configurarValoresIniciales();

        return panel;
    }

    private void configurarValoresIniciales() {
        // Configurar placeholders después de que todos los campos estén inicializados
        txtClienteNombre.setText("Ej: Juan Pérez");
        txtClienteNombre.setForeground(Color.GRAY);

        txtClienteTelefono.setText("Ej: 987654321");
        txtClienteTelefono.setForeground(Color.GRAY);

        txtClienteDireccion.setText("Ej: Av. Las Flores 123 - Miraflores");
        txtClienteDireccion.setForeground(Color.GRAY);

        txtCantidad.setText("1");
        txtCantidad.setForeground(COLOR_TEXTO);

        txtPrecio.setText("0.00");
        txtPrecio.setForeground(COLOR_TEXTO);

        // Configurar focus listeners
        configurarFocusListeners();
    }

    private void configurarFocusListeners() {
        configurarFocusListener(txtClienteNombre, "Ej: Juan Pérez");
        configurarFocusListener(txtClienteTelefono, "Ej: 987654321");
        configurarFocusListener(txtClienteDireccion, "Ej: Av. Las Flores 123 - Miraflores");
    }

    private void configurarFocusListener(JTextField field, String placeholder) {
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(COLOR_TEXTO);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    private void addFormField(JPanel panel, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(COLOR_TEXTO);
        panel.add(label);
        panel.add(textField);
    }

    private void stylizeTextArea(JTextArea textArea) {
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }

    private void stylizeComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void stylizeButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(color.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void stylizeButtonEstado(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(color.darker(), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void initController() {
        this.controlador = new PedidoController(this);
    }

    private void cargarPedidosIniciales() {
        controlador.cargarPedidos();
    }

    private void configurarEventos() {
        btnAgregar.addActionListener(e -> agregarPedido());
        btnEditar.addActionListener(e -> editarPedido());
        btnEliminar.addActionListener(e -> eliminarPedido());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarPedido());

        btnRecibido.addActionListener(e -> cambiarEstadoPedido("Recibido"));
        btnPreparacion.addActionListener(e -> cambiarEstadoPedido("En Preparación"));
        btnCamino.addActionListener(e -> cambiarEstadoPedido("En Camino"));
        btnEntregado.addActionListener(e -> cambiarEstadoPedido("Entregado"));

        cmbCategoria.addActionListener(e -> actualizarProductosPorCategoria());
        cmbProductosPredefinidos.addActionListener(e -> cargarPrecioDesdeProducto());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                cargarPedidoSeleccionado();
            }
        });
    }

    private void actualizarProductosPorCategoria() {
        String categoria = (String) cmbCategoria.getSelectedItem();
        cmbProductosPredefinidos.removeAllItems();

        String[] productos;
        switch (categoria) {
            case "Pollo": productos = PRODUCTOS_POLLO; break;
            case "Pizza": productos = PRODUCTOS_PIZZA; break;
            case "Pasta": productos = PRODUCTOS_PASTA; break;
            default: productos = new String[]{};
        }

        for (String producto : productos) {
            cmbProductosPredefinidos.addItem(producto);
        }

        if (productos.length > 0) {
            cargarPrecioDesdeProducto();
        }
    }

    private void cargarPrecioDesdeProducto() {
        String productoSeleccionado = (String) cmbProductosPredefinidos.getSelectedItem();
        if (productoSeleccionado != null && txtPrecio != null) {
            String categoria = (String) cmbCategoria.getSelectedItem();
            int index = cmbProductosPredefinidos.getSelectedIndex();

            double precio = 0.0;
            switch (categoria) {
                case "Pollo": if (index < PRECIOS_POLLO.length) precio = PRECIOS_POLLO[index]; break;
                case "Pizza": if (index < PRECIOS_PIZZA.length) precio = PRECIOS_PIZZA[index]; break;
                case "Pasta": if (index < PRECIOS_PASTA.length) precio = PRECIOS_PASTA[index]; break;
            }

            txtPrecio.setText(String.format("%.2f", precio));
            txtPrecio.setForeground(COLOR_TEXTO);
        }
    }

    private void agregarPedido() {
        try {
            Pedido pedido = obtenerPedidoDesdeFormulario();
            if (pedido != null) {
                controlador.agregarPedido(pedido);
                limpiarFormulario();
            }
        } catch (Exception ex) {
            mostrarError("Error al agregar pedido: " + ex.getMessage());
        }
    }

    private void editarPedido() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Seleccione un pedido de la tabla para editar");
            return;
        }

        try {
            int id = (int) modelo.getValueAt(filaSeleccionada, 0);
            Pedido pedido = obtenerPedidoDesdeFormulario();
            if (pedido != null) {
                pedido.setId(id);
                controlador.actualizarPedido(pedido);
                limpiarFormulario();
            }
        } catch (Exception ex) {
            mostrarError("Error al editar pedido: " + ex.getMessage());
        }
    }

    private void eliminarPedido() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Seleccione un pedido de la tabla para eliminar");
            return;
        }

        int id = (int) modelo.getValueAt(filaSeleccionada, 0);
        String cliente = (String) modelo.getValueAt(filaSeleccionada, 1);
        String producto = (String) modelo.getValueAt(filaSeleccionada, 3);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el pedido?\n\nCliente: " + cliente + "\nProducto: " + producto,
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            controlador.eliminarPedido(id);
        }
    }

    private void cambiarEstadoPedido(String nuevoEstado) {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Seleccione un pedido de la tabla para cambiar su estado");
            return;
        }

        int id = (int) modelo.getValueAt(filaSeleccionada, 0);
        controlador.actualizarEstadoPedido(id, nuevoEstado);
    }

    private void buscarPedido() {
        String textoBusqueda = txtBuscar.getText().trim();
        if (textoBusqueda.isEmpty() || textoBusqueda.equals("🔍 Buscar pedidos por cliente...")) {
            controlador.cargarPedidos();
            return;
        }

        for (int i = 0; i < modelo.getRowCount(); i++) {
            String cliente = ((String) modelo.getValueAt(i, 1)).toLowerCase();
            if (cliente.contains(textoBusqueda.toLowerCase())) {
                tabla.setRowSelectionInterval(i, i);
                return;
            }
        }
        mostrarMensaje("No se encontraron pedidos para: \"" + textoBusqueda + "\"");
    }

    private void cargarPedidoSeleccionado() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada != -1) {
            try {
                txtClienteNombre.setText(modelo.getValueAt(filaSeleccionada, 1).toString());
                txtClienteNombre.setForeground(COLOR_TEXTO);

                txtClienteTelefono.setText(modelo.getValueAt(filaSeleccionada, 2).toString());
                txtClienteTelefono.setForeground(COLOR_TEXTO);

                String categoria = modelo.getValueAt(filaSeleccionada, 4).toString();
                cmbCategoria.setSelectedItem(categoria);
                actualizarProductosPorCategoria();

                String producto = modelo.getValueAt(filaSeleccionada, 3).toString();
                for (int i = 0; i < cmbProductosPredefinidos.getItemCount(); i++) {
                    if (cmbProductosPredefinidos.getItemAt(i).equals(producto)) {
                        cmbProductosPredefinidos.setSelectedIndex(i);
                        break;
                    }
                }

                txtCantidad.setText(modelo.getValueAt(filaSeleccionada, 5).toString());
                txtCantidad.setForeground(COLOR_TEXTO);

            } catch (Exception e) {
                mostrarError("Error al cargar datos del pedido: " + e.getMessage());
            }
        }
    }

    private Pedido obtenerPedidoDesdeFormulario() {
        if (txtClienteNombre.getText().isEmpty() || txtClienteNombre.getText().equals("Ej: Juan Pérez")) {
            mostrarError("El nombre del cliente es obligatorio");
            return null;
        }

        if (txtClienteTelefono.getText().isEmpty() || txtClienteTelefono.getText().equals("Ej: 987654321")) {
            mostrarError("El teléfono del cliente es obligatorio");
            return null;
        }

        if (txtClienteDireccion.getText().isEmpty() || txtClienteDireccion.getText().equals("Ej: Av. Las Flores 123 - Miraflores")) {
            mostrarError("La dirección de delivery es obligatoria");
            return null;
        }

        String productoSeleccionado = (String) cmbProductosPredefinidos.getSelectedItem();
        if (productoSeleccionado == null) {
            mostrarError("Seleccione un producto");
            return null;
        }

        int cantidad;
        double precio;

        try {
            cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) {
                mostrarError("La cantidad debe ser mayor a 0");
                return null;
            }
        } catch (NumberFormatException e) {
            mostrarError("La cantidad debe ser un número entero válido");
            return null;
        }

        try {
            precio = Double.parseDouble(txtPrecio.getText());
            if (precio <= 0) {
                mostrarError("El precio debe ser mayor a 0");
                return null;
            }
        } catch (NumberFormatException e) {
            mostrarError("El precio debe ser un número válido");
            return null;
        }

        String categoria = (String) cmbCategoria.getSelectedItem();
        String observaciones = txtObservaciones.getText();

        return new Pedido(
                txtClienteNombre.getText(),
                txtClienteTelefono.getText(),
                txtClienteDireccion.getText(),
                productoSeleccionado,
                categoria,
                cantidad,
                precio,
                observaciones
        );
    }

    private void limpiarFormulario() {
        txtClienteNombre.setText("Ej: Juan Pérez");
        txtClienteNombre.setForeground(Color.GRAY);

        txtClienteTelefono.setText("Ej: 987654321");
        txtClienteTelefono.setForeground(Color.GRAY);

        txtClienteDireccion.setText("Ej: Av. Las Flores 123 - Miraflores");
        txtClienteDireccion.setForeground(Color.GRAY);

        txtCantidad.setText("1");
        txtCantidad.setForeground(COLOR_TEXTO);

        txtPrecio.setText("0.00");
        txtPrecio.setForeground(COLOR_TEXTO);

        txtObservaciones.setText("");

        cmbCategoria.setSelectedIndex(0);
        actualizarProductosPorCategoria();

        tabla.clearSelection();
    }

    public void mostrarPedidos(List<Pedido> lista) {
        modelo.setRowCount(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

        for (Pedido p : lista) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getClienteNombre(),
                    p.getClienteTelefono(),
                    p.getProducto(),
                    p.getCategoria(),
                    p.getCantidad(),
                    String.format("S/. %.2f", p.getTotal()),
                    p.getEstado(),
                    p.getFechaPedido().format(formatter)
            });
        }
    }

    public void actualizarEstadisticas(double ingresosTotales, long pedidosRecibidos,
                                       long pedidosPreparacion, long pedidosCamino,
                                       long pedidosEntregados, int totalPedidos) {
        lblIngresos.setText(String.format("💰 Ingresos: S/. %.2f", ingresosTotales));
        lblTotalPedidos.setText("📦 Total Pedidos: " + totalPedidos);
        lblRecibidos.setText("⏳ Recibidos: " + pedidosRecibidos);
        lblPreparacion.setText("👨‍🍳 En Preparación: " + pedidosPreparacion);
        lblCamino.setText("🚗 En Camino: " + pedidosCamino);
        lblEntregados.setText("✅ Entregados: " + pedidosEntregados);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "✅ Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "❌ Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new FrmPedidos().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Error al iniciar: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}