import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class FontChooser extends JFrame implements ListSelectionListener {

    private JList<String> fontNames;
    private JList<String> fontStyles;
    private JList<Integer> fontSize;

    String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    String[] styles = {"normal", "italic", "bold"};
    Integer[] size = {4,6,8,10,12,14,16,18,20,24,28,30,32,36,40,42,46,50,54,58,60,64,68,70,74,78,80,82,86,90,94,98,100,102,106,110};

    FontChooser()
    {
        this.setTitle("Font Chooser");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fontNames = new JList<>(fonts);
        fontNames.addListSelectionListener(this);
        fontStyles = new JList<>(styles);
        fontStyles.addListSelectionListener(this);
        fontSize = new JList<>(size);
        fontSize.addListSelectionListener(this);

        this.add(fontNames);
        this.add(fontStyles);
        this.add(fontSize);
        this.setLayout(null);
        this.setVisible(true);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }
}
