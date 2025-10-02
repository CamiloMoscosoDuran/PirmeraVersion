package vallegrande.edu.pe;

import vallegrande.edu.pe.view.FrmPedidos;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class AppLauncher {
    public static void main(String[] args) {
        // Establecer el look and feel del sistema para mejor apariencia
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("Error al configurar el look and feel: " + ex.getMessage());
            }
        }

        SwingUtilities.invokeLater(() -> {
            try {
                FrmPedidos frame = new FrmPedidos();
                frame.setVisible(true);

                // Mostrar mensaje de bienvenida
                javax.swing.JOptionPane.showMessageDialog(frame,
                        "¡Bienvenido a Restaurante Sabores! 🍗🍕🍝\n\n" +
                                "Sistema de Gestión de Pedidos Delivery\n" +
                                "Versión 1.0 - Desarrollado para tu negocio",
                        "🚀 Restaurante Sabores - Delivery",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                System.err.println("❌ Error al iniciar la aplicación: " + e.getMessage());
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(null,
                        "Error al iniciar la aplicación: " + e.getMessage(),
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}