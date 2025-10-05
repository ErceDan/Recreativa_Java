import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class GamePanel extends JPanel {

    private final Image background;
    private JButton[] buttons;

    public GamePanel(List<Game> games) {

        // Margen y separación entre botones
        setLayout(new GridLayout(3, 3, 100, 100));
        setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));
        setOpaque(false);

        // Cargar imagen de fondo
        background = new ImageIcon("src/images/fondo2.jpg").getImage();

        buttons = new JButton[games.size()];
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            JButton btn = new JButton();

            // Nombre base (sin extensión .zip)
            String baseName = game.getFileName().replace(".zip", "");

            // Ruta del logo
            File logoFile = new File("src/logos/" + baseName + ".png");

            if (logoFile.exists()) {
                // Cargar y escalar el logo
                ImageIcon icon = new ImageIcon(logoFile.getPath());
                Image scaled = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(scaled));
            } else {
                // Si no hay logo, mostrar el nombre del juego
                btn.setText(game.getDisplayName());
                btn.setFont(new Font("Arial", Font.BOLD, 16));
                btn.setForeground(Color.WHITE);
            }

            // Eliminar bordes y fondos para que se vea el logo limpio
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(false);
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setOpaque(false);

            buttons[i] = btn;
            add(btn);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void highlightButton(int index) {
        for (int i = 0; i < buttons.length; i++) {
            if (i == index) {
                buttons[i].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
            } else {
                buttons[i].setBorder(BorderFactory.createEmptyBorder());
            }
        }
    }

    public JButton getButton(int index) {
        if (index >= 0 && index < buttons.length) {
            return buttons[index];
        }
        return null;
    }

    public int getButtonCount() {
        return buttons.length;
    }

    public JButton[] getButtons() {
        return buttons;
    }

    public void setButtons(JButton[] buttons) {
        this.buttons = buttons;
    }
}
