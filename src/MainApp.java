package dk.au.chartcraft;

import scala.None;
import scala.Option;

import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {
    private JTextArea textArea;
    private GraphicsPanel canvasPanel;

    public MainApp() {
        super("DrawIDE");
        initializeUI();
    }

    private void initializeUI() {
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the split pane to divide the screen into two parts
        JSplitPane splitPane = new JSplitPane();

        // Left pane for drawing (Canvas)
        canvasPanel = new GraphicsPanel();
        canvasPanel.setPreferredSize(new Dimension(400, 400));
        canvasPanel.setBackground(Color.WHITE);

        // Right pane for command input (Text Area)
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 400));

        // Adding components to the split pane
        splitPane.setLeftComponent(canvasPanel);
        splitPane.setRightComponent(scrollPane);
        splitPane.setDividerLocation(400);

        // Add the split pane to the main frame
        add(splitPane, BorderLayout.CENTER);

        // Setup text area changes to trigger updates in the canvas
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                redrawCanvas();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                redrawCanvas();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                redrawCanvas();
            }
        });
    }

    private void redrawCanvas() {
        canvasPanel.clearDrawables();

        // Parse the new text and update the canvas
        String[] commands = textArea.getText().split("\n");
        for (String command : commands) {
            Option<Drawable> result = CommandParser.parseCommand(command, canvasPanel);
            if (result.isDefined()) {
                canvasPanel.addDrawable(result.get());
            }
        }

        canvasPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApp frame = new MainApp();
            frame.setVisible(true);
        });
    }
}
