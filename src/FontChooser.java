import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class FontChooser extends JDialog implements ActionListener {

    private JComboBox<String> fontNames;
    private JComboBox<String> fontStyles;
    private JComboBox<Integer> fontSize;
    private JTextField textField;
    private JButton okB;
    private JButton cancelB;

    private JLabel fontLabel;
    private JLabel fontStyleLabel;
    private JLabel fontSizeLabel;

    private Font font;

    String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    String[] styles = {"Normal","Bold", "Italic" };
    Integer[] size = {4,6,8,10,12,14,16,18,20,24,28,30,32,36,40,42,46,50,54,58,60,64,68,70,74,78,80,82,86,90,94,98,100,102,106,110};

    FontChooser(JFrame parent, boolean modal)
    {
        super(parent, modal);

        this.setTitle("Font Chooser");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(600,400);
        this.setResizable(false);

        textField = new JTextField("Example@123");
        textField.setBounds(40, 90, 200,30);
        textField.setFont(font);
        textField.setEditable(false);
        textField.setFocusable(false);

        fontNames = new JComboBox<>(fonts);
        fontNames.setBounds(40,30, 150,30);
        fontStyles = new JComboBox<>(styles);
        fontStyles.setBounds(210,30, 150,30);
        fontSize = new JComboBox<>(size);
        fontSize.setBounds(380,30, 150,30);

        font = TextEditor.font;
        fontNames.setSelectedItem(font.getName());
        switch (font.getStyle())
        {
            case Font.PLAIN -> fontStyles.setSelectedItem("Normal");
            case Font.BOLD -> fontStyles.setSelectedItem("Bold");
            case Font.ITALIC -> fontStyles.setSelectedItem("Italic");
        }
        fontSize.setSelectedItem(font.getSize());

        textField.setFont(font);

        fontNames.addActionListener(this);
        fontStyles.addActionListener(this);
        fontSize.addActionListener(this);

        fontLabel = new JLabel("Font:");
        fontLabel.setBounds(40, 5, 150, 30);

        fontStyleLabel = new JLabel("Style:");
        fontStyleLabel.setBounds(210, 5, 150, 30);

        fontSizeLabel = new JLabel("Size:");
        fontSizeLabel.setBounds(380, 5, 150, 30);

        okB = new JButton("OK");
        okB.setBounds(390,320, 70,30);
        okB.addActionListener(this);

        cancelB = new JButton("Cancel");
        cancelB.setBounds(475,320, 85,30);
        cancelB.addActionListener(this);

        this.add(fontNames);
        this.add(fontStyles);
        this.add(fontSize);
        this.add(textField);
        this.add(fontLabel);
        this.add(fontStyleLabel);
        this.add(fontSizeLabel);
        this.add(okB);
        this.add(cancelB);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okB)
            onOkPressed();
        if (e.getSource() == cancelB)
            onCancelPressed();

        if (e.getSource() == fontNames || e.getSource() == fontStyles || e.getSource() == fontSize)
        {
            font = createNewFont();
            textField.setFont(font);
        }
    }

    private void onOkPressed()
    {
        font = createNewFont();
        TextEditor.font = font;
        this.dispose();
    }

    private Font createNewFont()
    {
        return new Font(fontNames.getSelectedItem().toString(), fontStyles.getSelectedIndex(),Integer.parseInt(fontSize.getSelectedItem().toString()));
    }

    private void onCancelPressed()
    {
        this.dispose();
    }
}
