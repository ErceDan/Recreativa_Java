import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GamePanel extends JPanel {
    private final Image background;
    private JButton[] buttons;

    public GamePanel(List<Game> games) {
        setLayout(new GridLayout(3, 3, 5, 5));
        setOpaque(false);

        // Cargar imagen de fondo
        background = new ImageIcon("src/images/fondo.png").getImage();

        buttons = new JButton[games.size()];
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            JButton btn = new JButton(game.getDisplayName());
            btn.setFont(new Font("Arial", Font.BOLD,16));
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
                buttons[i].setBackground(Color.YELLOW);
            } else {
                buttons[i].setBackground(null);
            }
        }
    }

    public JButton getButton(int index) {
        if (index >= 0 && index < buttons.length) return buttons[index];
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
