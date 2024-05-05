import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class TextEditor extends JFrame implements ActionListener {

    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu helpMenu;
    private JMenuItem openMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem saveAsMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem copyMenuItem;
    private JMenuItem pasteMenuItem;
    private JMenuItem cutMenuItem;
    private JMenuItem fontSettigsItem;
    private JMenuItem aboutMenuItem;

    private JFileChooser fileChooser;
    private javax.swing.filechooser.FileFilter fileFilter;
    private File file;
    private BufferedReader reader;
    private BufferedWriter writer;


    public static Font font = new Font("Roboto", Font.PLAIN, 14);

    public TextEditor(){
        this.setTitle("Text Editor | Lemoncho");
        this.setSize(1000, 600);

        textArea = new JTextArea();
        textArea.setEditable(true);
        textArea.setLineWrap(false);
        textArea.setFont(font);
        textArea.setSize(this.getSize());

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
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setFileFilter(fileFilter);

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        helpMenu = new JMenu("Help");

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
        fontSettigsItem = new JMenuItem("Font Settings");
        fontSettigsItem.addActionListener(this);
        aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(this);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(exitMenuItem);

        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);
        editMenu.add(cutMenuItem);
        editMenu.add(fontSettigsItem);

        helpMenu.add(aboutMenuItem);


        this.setContentPane(scrollPane);
        //this.getContentPane().add(scrollPane);
        this.setJMenuBar(menuBar);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == openMenuItem){
            openFile();
        }
        if(e.getSource() == saveMenuItem){
            saveFile();
        }
        if(e.getSource() == saveAsMenuItem){
            saveAsFile();
        }
        if(e.getSource() == exitMenuItem){
            System.exit(0);
        }

        if (e.getSource() == fontSettigsItem)
        {
            new FontChooser(this, true);
            textArea.setFont(font);
        }
    }

    private void openFile(){
        try {
            int v = fileChooser.showOpenDialog(null);
            if (v == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                setTextToFile();
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
            }
            else {
                int v = fileChooser.showSaveDialog(null);
                if (v == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    saveFileToDisk();
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

}
