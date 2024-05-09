import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TextEditor extends JFrame implements ActionListener, DocumentListener {

    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu helpMenu;
    private JMenuItem newFileItem;
    private JMenuItem newWindowItem;
    private JMenuItem openMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem saveAsMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem copyMenuItem;
    private JMenuItem pasteMenuItem;
    private JMenuItem cutMenuItem;
    private JCheckBoxMenuItem wordWrapItem;
    private JMenuItem fontSettigsItem;
    private JMenuItem aboutMenuItem;

    private JFileChooser fileChooser;
    private javax.swing.filechooser.FileFilter fileFilter;
    private File file;
    private BufferedReader reader;
    private BufferedWriter writer;

    public static Font font = new Font("Roboto", Font.PLAIN, 14);

    private boolean unsavedChanges = false;

    private final String frameLabel = "Text Editor | Lemoncho";

    public TextEditor(){
        this.setTitle(frameLabel);
        this.setSize(1000, 600);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                if (unsavedChanges)
                {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    int v = showUnsavedChangesDialog();
                    if (v ==  JOptionPane.YES_OPTION)
                    {
                        unsavedChanges = false;
                        saveFile();
                        dispose();
                    } else if (v == JOptionPane.NO_OPTION)
                    {
                        unsavedChanges = false;
                        dispose();
                    }
                }
                else
                    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });

        textArea = new JTextArea();
        textArea.setEditable(true);
        textArea.setLineWrap(false);
        textArea.setFont(font);
        textArea.setSize(this.getSize());
        textArea.getDocument().addDocumentListener(this);

        scrollPane = new JScrollPane(textArea);

        fileFilter = new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".txt");
            }

            public String getDescription() {
                return "Text Files";
            }
        };

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(fileFilter);

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        helpMenu = new JMenu("Help");

        newFileItem = new JMenuItem("New");
        newFileItem.addActionListener(this);
        newWindowItem = new JMenuItem("New Window");
        newWindowItem.addActionListener(this);
        openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(this);
        saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(this);
        saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(this);
        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(this);
        copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.addActionListener(this);
        pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.addActionListener(this);
        cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.addActionListener(this);
        wordWrapItem = new JCheckBoxMenuItem("Word Wrap");
        wordWrapItem.addActionListener(this);
        fontSettigsItem = new JMenuItem("Font Settings");
        fontSettigsItem.addActionListener(this);
        aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(this);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        fileMenu.add(newFileItem);
        fileMenu.add(newWindowItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);
        editMenu.add(cutMenuItem);
        editMenu.addSeparator();
        editMenu.add(wordWrapItem);
        editMenu.add(fontSettigsItem);

        helpMenu.add(aboutMenuItem);

        this.setContentPane(scrollPane);
        //this.getContentPane().add(scrollPane);
        this.setJMenuBar(menuBar);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newFileItem)
            newFile();

        if (e.getSource() == newWindowItem)
            new TextEditor();

        if(e.getSource() == openMenuItem)
            openFile();

        if(e.getSource() == saveMenuItem)
            saveFile();

        if(e.getSource() == saveAsMenuItem)
            saveAsFile();

        if(e.getSource() == exitMenuItem)
            System.exit(0);

        if (e.getSource() == copyMenuItem)
            copy();

        if (e.getSource() == cutMenuItem)
            cut();

        if (e.getSource() == pasteMenuItem)
            paste();

        if (e.getSource() == aboutMenuItem)
        {
            JOptionPane.showMessageDialog(null, "This is a text editor that includes features like \nchanging the font and is made in java", "About",JOptionPane.INFORMATION_MESSAGE);
        }

        if (e.getSource() == wordWrapItem)
            textArea.setLineWrap(!textArea.getLineWrap());

        if (e.getSource() == fontSettigsItem)
        {
            new FontChooser(this, true);
            textArea.setFont(font);
        }
    }

    private void newFile()
    {
        int v = showUnsavedChangesDialog();
        if (v == JOptionPane.YES_OPTION)
        {
            saveFile();
            textArea.setText("");
            unsavedChanges = false;
            setTitle(frameLabel);
        } else if (v == JOptionPane.NO_OPTION)
        {
            textArea.setText("");
            unsavedChanges = false;
            setTitle(frameLabel);
        }
    }

    private void openFile(){
        try {
            if (unsavedChanges) {
                int answer = showUnsavedChangesDialog();
                if (answer == JOptionPane.YES_OPTION) {
                    saveFile();

                    int v = fileChooser.showOpenDialog(null);
                    if (v == JFileChooser.APPROVE_OPTION) {
                        file = fileChooser.getSelectedFile();
                        setTextToFile();
                        unsavedChanges = false;
                        this.setTitle(frameLabel);
                    }
                } else if (answer == JOptionPane.NO_OPTION) {

                    int v = fileChooser.showOpenDialog(null);
                    if (v == JFileChooser.APPROVE_OPTION) {
                        file = fileChooser.getSelectedFile();
                        setTextToFile();
                        unsavedChanges = false;
                        this.setTitle(frameLabel);
                    }
                }
            }else {
                int v = fileChooser.showOpenDialog(null);
                if (v == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    setTextToFile();
                    unsavedChanges = false;
                    this.setTitle(frameLabel);
                }
            }

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Failed to open file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void saveFile(){
        try {
            if (file != null)
            {
                saveFileToDisk();
                unsavedChanges = false;
                this.setTitle(frameLabel);
            }
            else {
                int v = fileChooser.showSaveDialog(null);
                if (v == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    saveFileToDisk();
                    unsavedChanges = false;
                    this.setTitle(frameLabel);
                }
            }
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Failed to save file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAsFile()
    {
        try {
            int v = fileChooser.showSaveDialog(null);
            if (v == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                saveFileToDisk();
                unsavedChanges = false;
                this.setTitle(frameLabel);
            }
        }catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Failed to save file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveFileToDisk()
    {
        try {
            writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
            writer.write(textArea.getText().toString());
            writer.close();
        }catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void setTextToFile()
    {
        try {
            reader = new BufferedReader(new FileReader(file));

            textArea.setText("");

            String line;
            while ((line = reader.readLine()) != null)
            {
                textArea.append(line + "\n");
            }

            reader.close();
        }catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void copy()
    {
        try {
            Robot robot = new Robot();

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_C);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_C);
        }catch (AWTException ex)
        {
            ex.printStackTrace();
        }
    }

    private void cut()
    {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_X);
            robot.keyRelease(KeyEvent.VK_X);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        }catch (AWTException ex)
        {
            ex.printStackTrace();
        }
    }

    private void paste()
    {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        }catch (AWTException ex)
        {
            ex.printStackTrace();
        }
    }

    private int showUnsavedChangesDialog()
    {
        return JOptionPane.showConfirmDialog(null, "You have unsaved changes! \nDo you want to save them?", "Unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        unsavedChanges = true;
        this.setTitle(frameLabel + "*");
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        unsavedChanges = true;
        this.setTitle(frameLabel + "*");
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        unsavedChanges = true;
        this.setTitle(frameLabel + "*");
    }

}
