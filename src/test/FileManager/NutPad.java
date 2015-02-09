/// File   : editor/NutPad.java -- A very simple text editor
// Purpose: Illustrates use of AbstractActions for menus.
//          It only uses a few Action features.  Many more are available.
//          This program uses the obscure "read" and "write"
//               text component methods.
// Author : Fred Swartz - 2006-12-14 - Placed in public domain.

package test.FileManager;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;

///////////////////////////////////////////////////////////////////////// NutPad
@SuppressWarnings("serial")
public class NutPad extends JFrame {
	private FileManagerForm pForm;
	//... Components 
    private JTextArea    _editArea;
    private JFileChooser _fileChooser = new JFileChooser();
    
    //... Create actions for menu items, buttons, ...
    private Action _saveAction = new SaveAction();
    private Action _exitAction = new ExitAction(); 
    
    //... File for opening
    private File workFile;
    




	//============================================================== constructor
    public NutPad()
    {
    	//... Create scrollable text area.
        _editArea = new JTextArea(15, 80);
        _editArea.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        _editArea.setFont(new Font("monospaced", Font.PLAIN, 14));
        JScrollPane scrollingText = new JScrollPane(_editArea);
        
        //-- Create a content pane, set layout, add component.
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.add(scrollingText, BorderLayout.CENTER);
        
        //... Create menubar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = menuBar.add(new JMenu("File"));
        fileMenu.setMnemonic('F');
        fileMenu.add(_saveAction);
        fileMenu.addSeparator(); 
        fileMenu.add(_exitAction);
        
        //... Set window content and menu.
        setContentPane(content);
        setJMenuBar(menuBar);
      //... Set other window characteristics.
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setTitle("NutPad");
        pack();
        setLocationRelativeTo(null);

    }
    
	public File getWorkFile() {
		return workFile;
	}
	
    public void setWorkFile(File workFile) {
		this.workFile = workFile;
	}
    
    public void setProcessForm(FileManagerForm form)
    {
    	pForm = form;
    }
        public void openWorkFile(boolean isEdit) {
                                  
        	_editArea.setEditable(isEdit);
        	
        	if (workFile == null)
        	{
        		workFile = pForm.getSelectedFile();
        	}
        	
            _fileChooser.setSelectedFile(this.workFile);

                try {
                    FileReader reader = new FileReader(this.workFile);
                    _editArea.read(reader, "");  // Use TextComponent read
                } catch (IOException ioex) {
                    System.out.println("can't open file "+workFile.getAbsolutePath()+" for viewing/editing");
                    System.exit(1);
                }
                setVisible(true);
    }

    
    //////////////////////////////////////////////////// inner class SaveAction
    class SaveAction extends AbstractAction {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		//============================================= constructor
        SaveAction() {
            super("Save...");
            putValue(MNEMONIC_KEY, new Integer('S'));
        }
        
        //========================================= actionPerformed
        public void actionPerformed(ActionEvent e) {
        	 File ourFile = workFile;
         	_fileChooser.setSelectedFile(ourFile);
         	
                try {
                    FileWriter writer = new FileWriter(ourFile);
                    _editArea.write(writer);  // Use TextComponent write
                } catch (IOException ioex) {
                    JOptionPane.showMessageDialog(NutPad.this, ioex);
                    System.exit(1);
                }
            }
        }
   
    ///////////////////////////////////////////////////// inner class ExitAction
    class ExitAction extends AbstractAction {
        
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		//============================================= constructor
        public ExitAction() {
            super("Exit");
            putValue(MNEMONIC_KEY, new Integer('X'));
        }
        
        //========================================= actionPerformed
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
