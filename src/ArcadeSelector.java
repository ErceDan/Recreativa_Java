
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArcadeSelector extends JFrame {

    private static final String ROMS_DIR = "src" + File.separator + "mame" + File.separator + "roms";
    private static final String MAME_EXE = "src" + File.separator + "mame" + File.separator + "mame.exe";

    private List<Game> allGames;
    private int currentPage = 0;
    private JPanel contentPanel;

    private int selectedIndex = 0; // índice del juego seleccionado dentro de la página
    private GamePanel currentGamePanel;

    public ArcadeSelector() {
        setTitle("Arcade Selector");
        /*        setSize(1920, 1080); */
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);

        // Maximiza la ventana
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        allGames = loadGames();

        if (allGames.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron ROMs en la carpeta: " + ROMS_DIR);
        }

        contentPanel = new JPanel(new BorderLayout());
        setContentPane(contentPanel);

        showPage(currentPage);

        setupKeyBindings();

        setVisible(true);
    }

    private List<Game> loadGames() {
        List<Game> list = new ArrayList<>();
        File romDir = new File(ROMS_DIR);

        if (!romDir.exists()) {
            JOptionPane.showMessageDialog(this, "La carpeta de ROMs no existe: " + romDir.getAbsolutePath());
            return list;
        }

        String[] roms = romDir.list((dir, name) -> name.toLowerCase().endsWith(".zip"));
        if (roms == null || roms.length == 0) {
            JOptionPane.showMessageDialog(this, "No se encontraron ROMs en la carpeta: " + romDir.getAbsolutePath());
            return list;
        }

        for (String r : roms) {
            list.add(new Game(r));
        }
        return list;
    }

    private void showPage(int page) {
        contentPanel.removeAll();
        int start = page * 9;
        int end = Math.min(start + 9, allGames.size());
        List<Game> subList = allGames.subList(start, end);

        currentGamePanel = new GamePanel(subList);
        contentPanel.add(currentGamePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();

        selectedIndex = Math.min(selectedIndex, currentGamePanel.getButtonCount() - 1);
        currentGamePanel.highlightButton(selectedIndex);
        currentGamePanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100)); // top,left,bottom,right

    }

    private void setupKeyBindings() {
        InputMap im = contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = contentPanel.getActionMap();

        im.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        am.put("moveRight", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                moveRight();
            }
        });

        im.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        am.put("moveLeft", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                moveLeft();
            }
        });

        im.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        am.put("moveUp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                moveUp();
            }
        });

        im.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        am.put("moveDown", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                moveDown();
            }
        });

        im.put(KeyStroke.getKeyStroke("ENTER"), "launchGame");
        am.put("launchGame", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                launchSelectedGame();
            }
        });
    }

    private void moveRight() {
        if (selectedIndex < currentGamePanel.getButtonCount() - 1) {
            selectedIndex++;
            currentGamePanel.highlightButton(selectedIndex);
        } else if ((currentPage + 1) * 9 < allGames.size()) {
            currentPage++;
            selectedIndex = 0;
            showPage(currentPage);
        }
    }

    private void moveLeft() {
        if (selectedIndex > 0) {
            selectedIndex--;
            currentGamePanel.highlightButton(selectedIndex);
        } else if (currentPage > 0) {
            currentPage--;
            selectedIndex = Math.min(8, currentGamePanel.getButtonCount() - 1);
            showPage(currentPage);
        }
    }

    private void moveDown() {
        if (selectedIndex + 3 < currentGamePanel.getButtonCount()) {
            selectedIndex += 3;
            currentGamePanel.highlightButton(selectedIndex);
        } else if ((currentPage + 1) * 9 < allGames.size()) {
            currentPage++;
            showPage(currentPage);
            selectedIndex = selectedIndex % 3; // misma columna, primera fila
            currentGamePanel.highlightButton(selectedIndex);
        }
    }

    private void moveUp() {
        if (selectedIndex - 3 >= 0) {
            selectedIndex -= 3;
            currentGamePanel.highlightButton(selectedIndex);
        } else if (currentPage > 0) {
            currentPage--;
            showPage(currentPage);
            int lastRow = (currentGamePanel.getButtonCount() - 1) / 3;
            selectedIndex = lastRow * 3 + (selectedIndex % 3);
            if (selectedIndex >= currentGamePanel.getButtonCount()) {
                selectedIndex = currentGamePanel.getButtonCount() - 1;
            }
            currentGamePanel.highlightButton(selectedIndex);
        }
    }

    private void launchSelectedGame() {
        int gameIndex = currentPage * 9 + selectedIndex;
        if (gameIndex < allGames.size()) {
            Game game = allGames.get(gameIndex);
            String romName = game.getFileName().replace(".zip", "");

            try {
                ProcessBuilder pb = new ProcessBuilder(
                        MAME_EXE,
                        romName,
                        "-rompath", ROMS_DIR
                );

                // No establecemos directory(), se ejecuta desde la carpeta donde se lanza Java
                pb.start();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "No se pudo ejecutar el juego: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ArcadeSelector());
    }
}
