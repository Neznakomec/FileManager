package test.FileManager;

import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreePath;

public class Listeners {
	
	private FileManagerForm pForm;
	private NutPad nutpad;
	private JFrame frame; //makedir/makefile dialog
	private JButton button; //makedir/makefile dialog textfield
	private JTextField field;//makedir/makefile dialog button
	private String transferFrom, transferTo;
	private TreePath currSelectedPath;

	Listeners()
	{
	pForm = null;	
	}
	
	// F2 function
	public void changeDrive(JTree tree) {
		
		JFrame choose = new JFrame();
		choose.setVisible(true);
		choose.setTitle("Choose drive");
		choose.setSize(tree.getSize().width / 2, tree.getSize().height / 2);
		choose.setLocation(tree.getLocation().x + tree.getSize().width / 4,
				tree.getLocation().y + tree.getSize().height / 4);

		File[] roots = File.listRoots();
		choose.setLayout(new GridLayout(roots.length, 1)); // 1 column, many
															// rows
		for (int i = 0; i < roots.length; i++) {
			Icon icon = FileSystemView.getFileSystemView().getSystemIcon(
					roots[i]);
			String s = roots[i].getAbsolutePath();
			JLabel l = new JLabel(s, icon, SwingConstants.LEFT);
			
			l.setBorder(BorderFactory.createEtchedBorder());
			l.addMouseListener(this.forDriveChoose);
			choose.add(l);
		}
	}

	// F3, F4 function
	public void openFile(String pathString) {
		Desktop desktop = null;
		if (Desktop.isDesktopSupported()) {
			desktop = Desktop.getDesktop();
		}

		try {
			desktop.open(new File(pathString));
		} catch (IOException ioe) {
			System.out.println("Can't open " + getFileExtension(pathString)
					+ " type of files");
		}

	}
	
	public void openFile() {
		JTree tree = pForm.getCurrentTree();
		String pathString = getTreePath(tree.getSelectionPath(), File.pathSeparator);
		Desktop desktop = null;
		if (Desktop.isDesktopSupported()) {
			desktop = Desktop.getDesktop();
		}

		try {
			desktop.open(new File(pathString));
		} catch (IOException ioe) {
			System.out.println("Can't open " + getFileExtension(pathString)
					+ " type of files");
		}

	}
//F5 function
	public void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
				System.out.println("Directory copied from " + src + "  to "
						+ dest);
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it
			Files.copy(src.toPath(), dest.toPath(),
					StandardCopyOption.REPLACE_EXISTING);

			System.out.println("File " + src.getName() + " copied from "
					+ src.getAbsolutePath() + " to " + dest.getAbsolutePath());
		}
	}

	// F6 function
	public void moveFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
				System.out.println("Directory moved from " + src + "  to "
						+ dest);
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				moveFolder(srcFile, destFile);
			}
			src.delete(); // delete a directoty after all files moved
			System.out.println("Empty directory " + src.getAbsolutePath()
					+ " deleted");

		} else {
			// if file, then move it
			
			Files.move(src.toPath(), dest.toPath());
			
			System.out.println("File " + src.getName() + " moved from "
					+ src.getAbsolutePath() + " to " + dest.getAbsolutePath());
		}
	}

	// F7 Function
	public void makeDir() {
		frame = new JFrame("Enter catalog name");
		frame.setSize(400, 200);
		frame.setLayout(new GridLayout(2, 1));
		
		this.field = new JTextField();
		this.button = new JButton("Create Directory");
		field.setHorizontalAlignment(JTextField.CENTER);
		
		frame.add(field);
		frame.add(button);
		frame.setVisible(true);

		button.addActionListener(newDir);
	}
	
	public void makeFile() {
		frame = new JFrame("Enter file name");
		frame.setSize(400, 200);
		frame.setLayout(new GridLayout(2, 1));
		
		field = new JTextField();
		button = new JButton("Create File");
		field.setHorizontalAlignment(JTextField.CENTER);
		
		frame.add(field);
		frame.add(button);
		frame.setVisible(true);

		button.addActionListener(newFile);
		field.addKeyListener(newFileKey);
	}

	// F8 function
	public void delDir(File src) throws IOException {
		if (src.isDirectory()) {

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src file structure
				File srcFile = new File(src, file);
				// recursive delete directory content
				delDir(srcFile);
			}
			src.delete(); // delete a directoty after all files moved
			System.out.println("Empty directory " + src.getAbsolutePath()
					+ " deleted");

		} else {
			// if file, then delete it

			Files.delete(src.toPath());
			System.out.println("File " + src.getAbsolutePath()
					+ " was deleted ");
		}
	}
	
	//additional functions
	public String getTreePath(TreePath treePath, String separator) {
		StringBuffer treePathStrBuff = new StringBuffer();
		String treePathStr = null;
		Object[] objs = treePath.getPath();
		if (objs != null) {
			treePathStrBuff.append(objs[objs.length - 1].toString());
			treePathStr = treePathStrBuff.toString();
		}

		return treePathStr;
	}

	private String getFileExtension(String pathString) {
		String delims = "[.]";
		if (!pathString.contains(".")) {
			return pathString;
		} else {
			String[] tokens = pathString.split(delims);
			return "*." + tokens[tokens.length - 1];
		}
	}

	public void selectDrive(String drivePath, JTree tree) {
		File tempFile = new File(drivePath);
		FileTreeModel tempFileTree = new FileTreeModel(tempFile);
		tree.setModel(tempFileTree);
		//FileSystemModel tempFileTree = new FileSystemModel(tempFile);
		//tree.setModel(tempFileTree);

	}

	protected boolean isBinary(String url){

		 boolean isbin=false;
		 java.io.InputStream in=null;


		 try{
		 URL bin_url = new URL(url);

		 in = bin_url.openStream();
		 BufferedReader r = new BufferedReader(new InputStreamReader(in));

		 char [] cc= new char[255]; //do a peek
		 r.read(cc,0,255);

		 double prob_bin=0;

		 for(int i=0; i<cc.length; i++){
		 int j = (int)cc[i];

		 if(j<32 || j>127){ //with chinese and other type languages it might
		 //flag them as binary - need another check ideaaly
		 prob_bin++;
		 }

		 }

		 double pb = prob_bin/255;
		 if(pb>0.5){
		 // System.out.println("probably binary at "+pb);
		 isbin= true;
		 }

		 in.close();

		 }catch(Exception ee){
		 System.out.println("WARN! Couldn't find isBinary() content-"+url);
		 isbin= false; //error - likely broken link - so return false
		 }

		 try{
		 in.close();
		 }catch(Exception E){}

		 System.out.println("url isBinary():"+url+":"+isbin);
		 return isbin;

		 }
	
	public void CatchCurrPath(int x, int y)
	{
		JTree tree = pForm.getCurrentTree();
		TreePath tempPath = null;
		for (int i = 0; i<tree.getSize().getWidth(); i++)
		{
			tempPath = tree.getPathForLocation(i, y);
			if (tempPath != null) break;
		}
		this.currSelectedPath = tempPath; 
	}
	
	public String buildInfoLabel() {
		// builds a string for infoLabel component,
		// uses information from transferTo and
		// transferFrom strings

		String info = "";
		
		getCopyPaths();
		String pathFrom = transferFrom;
		String pathTo = transferTo;
		String add;
		if (new File(pathFrom).isFile()) {
			add = "<br>view(F3), edit(F4) or delete(F8) " + pathFrom;
		}

		else {
			add = "<br>delete(F8) "+pathFrom;
		}
		info = "<html>copy(F5) or move(F6) from " + pathFrom + " to	"	+ pathTo+add;
		return info;
	}

	public void updateTrees() {
		JTree leftTree = pForm.getLeftTree();
		JTree rightTree = pForm.getRightTree();
		leftTree.updateUI();
		rightTree.updateUI();
	}

	//
	//
	//getters and setters section
	//
	//
	
	public void getCopyPaths() {
		String pathFrom = new String("");
		String pathTo = new String("");
		JTree currTree = pForm.getCurrentTree();		//last selected tree
		JTree leftTree = pForm.getLeftTree(); 			//left tree
		JTree rightTree = pForm.getRightTree();			//right tree
	
		TreePath tpFrom = null, tpTo = null;
	
		if (currTree == leftTree) // you copy from leftTree to rightTree
		{
			tpFrom = leftTree.getSelectionPath();
			tpTo = rightTree.getSelectionPath();
	
			if (tpTo == null)
				pathTo = ((File) (rightTree.getModel().getRoot()))
						.getAbsolutePath();
		} else if (currTree == rightTree) // you copy from tree2 to tree1
		{
			tpFrom = rightTree.getSelectionPath();
			tpTo = leftTree.getSelectionPath();
	
			if (tpTo == null)
				pathTo = ((File) (leftTree.getModel().getRoot()))
						.getAbsolutePath();
		}
	
		if (tpFrom != null)
			pathFrom = getTreePath(tpFrom, File.pathSeparator);
		if (tpTo != null)
			pathTo = getTreePath(tpTo, File.pathSeparator);
	
		File fFrom = new File(pathFrom);
		File fTo = new File(pathTo);
	
		if (fTo.isFile()) {
			fTo = new File(fTo.getParent());
		}
	
		if (fFrom.isFile()) {
			fTo = new File(fTo.getAbsolutePath() + "/" + fFrom.getName());
		}
		if (fFrom.isDirectory()) {
			fTo = new File(fTo.getAbsolutePath() + "/" + fFrom.getName());
		}
	
		pathFrom = fFrom.getAbsolutePath();
		pathTo = fTo.getAbsolutePath();
	
		this.transferFrom = pathFrom;
		this.transferTo = pathTo;
	
	}

	public TreePath getSelPathParentOrRoot(JTree tree) {
		TreePath tp = tree.getSelectionPath();
	
		if ((tp == null) || (tp.getParentPath() == null)) {
			File tf = (File) tree.getModel().getRoot();
			tp = new TreePath(tf.toPath());
		} else {
			tp = tp.getParentPath();
		}
		return tp;
	}

	public TreePath getSelPathOrRoot(JTree tree) {
		TreePath tp = tree.getSelectionPath();
	
		if (tp == null) {
			File tf = (File) tree.getModel().getRoot();
			tp = new TreePath( tf.toPath() );
		}
		
		return tp;
	}

	public String getTransferFrom() {
		return transferFrom;
	}

	public String getTransferTo() {
		return transferTo;
	}

	public void setEditor(NutPad editor)
	{
		if (pForm != null) nutpad = editor;
	}

	public void setProcessForm(FileManagerForm form)
	{
		pForm = form;
	}

	public MouseListener currentTreeListener = new MouseListener()
	{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			JTree tree = (JTree) e.getSource();
			if (pForm.getLeftTree() == tree)
			{
				pForm.setCurrentTree(pForm.getLeftTree());
			}
			else if (pForm.getRightTree() == tree)
			{
				pForm.setCurrentTree(pForm.getRightTree());
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	public  ActionListener newDir = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			// TODO Auto-generated method stub
			JTree currTree = pForm.getCurrentTree();
			if (e.getSource() == button) {
				//
				TreePath tp = getSelPathOrRoot(currTree);
				File temp = new File(getTreePath(tp, File.pathSeparator));
				if ( temp.isFile() ) tp = getSelPathParentOrRoot(currTree);
				
				temp = new File(getTreePath(tp, File.pathSeparator) + "/"+ field.getText());
				
				if (temp.mkdir()) {
					System.out.println("Directory " + temp.getAbsolutePath()
							+ " created");
				} else {
					System.out.println("Please select a folder to create directory");
				}
			}
			updateTrees();
		}


	};
	
	public void doFile()
	{
		JTree tree = pForm.getCurrentTree();
		TreePath tp = getSelPathOrRoot(tree);
		File temp = new File(getTreePath(tp, File.pathSeparator) + "/" + field.getText());
		try {
			temp.createNewFile();
			System.out.println("File " + temp.getAbsolutePath()
					+ " created");
		} catch (IOException ioe) {
			System.out
					.println("Please select a folder to create a file");
		}
		frame.setVisible(false);
	}
	public ActionListener newFile = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
		doFile();	
		}
	};
	
	public KeyListener newFileKey = new KeyListener() {
		
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				doFile();
			}
			
		}
	};


	public KeyListener hotKeys = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent key) {
			// TODO Auto-generated method stub
			JTree currTree = pForm.getCurrentTree();
			JTree leftTree = pForm.getLeftTree();
			JTree rightTree = pForm.getRightTree();
			
			getCopyPaths();
			pForm.setInfoLabelText(buildInfoLabel());
			if (currTree == leftTree) 
			{
				/*leftTree.setBorder( pForm.getSelectBorder() );
				rightTree.setBorder( pForm.getNullBorder() );*/
				pForm.getScrollpane1().setBorder( pForm.getSelectBorder() );
				pForm.getScrollpane2().setBorder( pForm.getNullBorder() );
				
			} else if (currTree == rightTree) {
				/*leftTree.setBorder( pForm.getNullBorder() );
				rightTree.setBorder( pForm.getSelectBorder() );*/
				pForm.getScrollpane1().setBorder( pForm.getNullBorder() );
				pForm.getScrollpane2().setBorder( pForm.getSelectBorder() );
			}

			if (key.getKeyCode() == KeyEvent.VK_F2)
				changeDrive(currTree);
			else if (key.getKeyCode() == KeyEvent.VK_F3) {
				TreePath tp = currTree.getSelectionPath();
				File tf = new File( getTreePath(tp, File.pathSeparator));
				if ((tp != null) && ( tf.isFile() ) )
				{
					setEditor(pForm.getNutPad());
					nutpad.setWorkFile(tf);
					String url = tf.getAbsolutePath();
					if ( !isBinary(url) )
						{
						nutpad.openWorkFile(false);
						}
				}
			} else if (key.getKeyCode() == KeyEvent.VK_F4) {
				TreePath tp = currTree.getSelectionPath();
				File tf = new File( getTreePath(tp, File.pathSeparator));
				if ((tp != null) && ( tf.isFile() ) )
				{
					setEditor(pForm.getNutPad());
					nutpad.setWorkFile(tf);
					String url = tf.getAbsolutePath();
					if ( !isBinary(url) )
						{
						nutpad.openWorkFile(true);
						}
				}
					
			} else if (key.getKeyCode() == KeyEvent.VK_F5) {

				getCopyPaths();

				String pathFrom = transferFrom;
				String pathTo = transferTo;
				File dirFrom = new File(pathFrom);
				File dirTo = new File(pathTo);

				JLabel label = pForm.getInfoLabel();

				label.setText("copying from " + pathFrom + " to " + pathTo);

				try {
					copyFolder(dirFrom, dirTo);
					System.out.println("all files successfully copied");
					

				} catch (IOException e) {
					// TODO Auto-generated catch block
				}

			} else if (key.getKeyCode() == KeyEvent.VK_F6) {
				getCopyPaths();

				String pathFrom = transferFrom;
				String pathTo = transferTo;
				File dirFrom = new File(pathFrom);
				File dirTo = new File(pathTo);

				JLabel label = pForm.getInfoLabel();

				label.setText("moving from " + pathFrom + " to " + pathTo);

				try {
					TreePath tp = getSelPathParentOrRoot(currTree);
					
					moveFolder(dirFrom, dirTo);
					System.out.println("all files successfully moved");
					
					currTree.setSelectionPath(tp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			} else if (key.getKeyCode() == KeyEvent.VK_F7) {
				makeDir();
			} else if (key.getKeyCode() == KeyEvent.VK_F8) {
				File source = new File(getTreePath(currTree.getSelectionPath(),
						File.pathSeparator));
				try {
					delDir(source);
				} catch (IOException e) {
					System.out.println("can't delete " + source);
				}
			}

			updateTrees();
		}

		@Override
		public void keyReleased(KeyEvent key) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent key) {
			// TODO Auto-generated method stub

		}
	};

	public MouseListener mouseRightMenu = new MouseListener() {
		public void mouseClicked(MouseEvent e) {
			JTree currTree = pForm.getCurrentTree();
			JTree leftTree = pForm.getLeftTree();
			JTree rightTree = pForm.getRightTree();
			CatchCurrPath(e.getX(), e.getY());
			currTree.setSelectionPath(currSelectedPath);
			
			if ((leftTree.getSelectionPath() == null) || (rightTree.getSelectionPath() == null))
			{
				JPopupMenu menu = pForm.getFileMenu();
				JMenuItem ji = (JMenuItem) menu.getSubElements()[1];
				ji.setEnabled(false);
			}
			else
			{
				JPopupMenu menu = pForm.getFileMenu();
				JMenuItem ji = (JMenuItem) menu.getSubElements()[1];
				ji.setEnabled(true);
			}
			
			getCopyPaths();
			JLabel label = pForm.getInfoLabel();
			label.setText(buildInfoLabel());

			//
			//process double left click
			//
			if (e.getClickCount() == 2 && !e.isConsumed() && e.getButton() == MouseEvent.BUTTON1) {
				e.consume();
				 //handle double click.
				TreePath tp = currTree.getSelectionPath();
				if (tp != null)
				{
					File tf = new File(getTreePath(tp, File.pathSeparator));
					openFile(tf.getAbsolutePath());
					System.out.println("double left click");
				}
			}
			//
			//process double right click
			//			
			if (e.getClickCount() == 2 && !e.isConsumed() && e.getButton() == MouseEvent.BUTTON3) {
				e.consume();
				 //handle double click.
				makeFile();
				
			}
			
			// General mouse event, highlight tree
if (currTree == leftTree) {
				/*leftTree.setBorder(pForm.getSelectBorder());
				rightTree.setBorder(pForm.getNullBorder());*/
				pForm.getScrollpane1().setBorder(pForm.getSelectBorder());
				pForm.getScrollpane2().setBorder(pForm.getNullBorder());
			} else if (currTree == rightTree) {
				pForm.getScrollpane1().setBorder(pForm.getNullBorder());
				pForm.getScrollpane2().setBorder(pForm.getSelectBorder());
			}
			// process 1 right click(file menu/standard menu)
			if (e.getButton() == MouseEvent.BUTTON3) {

				System.out.println("Right click");

				TreePath tp = currTree.getPathForLocation(e.getX(), e.getY());

				if (tp != null) {
					currTree.setSelectionPath(tp);
					System.out.println("file menu");
					JPopupMenu menu = pForm.getFileMenu();
					menu.show(currTree, e.getX(), e.getY());
				} else {
					currTree.setSelectionPath(null);
					System.out.println("std menu");
					JPopupMenu menu = pForm.getStandardMenu();
					CatchCurrPath(e.getX(), e.getY());
					currTree.setSelectionPath(currSelectedPath);
					menu.show(currTree, e.getX(), e.getY());
				}
			}
			//process 1 left click, select clicked tree node
			if (e.getButton() == MouseEvent.BUTTON1) {
				System.out.println("Left click");

				TreePath tp = currTree.getPathForLocation(e.getX(), e.getY());
				if (tp == null) {
					currTree.setSelectionPath(null);
				}
			}

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	};

	public MouseListener forDriveChoose = new MouseListener() {
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
			if (e.getButton() == MouseEvent.BUTTON1) {
				//JFrame tempFrame = (JFrame) e.getSource();
				//JLabel chosenDrive = (JLabel) tempFrame.getContentPane()
				//		.getComponentAt(e.getX(), e.getY());
				JLabel chosenDrive = (JLabel) e.getSource();
				if (chosenDrive != null) {
					System.out.println("You choose " + chosenDrive.getText());
					selectDrive(chosenDrive.getText(), pForm.getCurrentTree());
				}

			}

		}
	};

}