package test.FileManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class MouseMenus {
public static JPopupMenu fileMenu = new JPopupMenu();
public static JPopupMenu standardMenu = new JPopupMenu();

static ActionListener actionListener = new ActionListener() {
    public void actionPerformed(ActionEvent actionEvent) {
      System.out.println("Selected: "
          + actionEvent.getActionCommand());
    }
};

	    
	    public static void formFileMenu(JPopupMenu menu)
	    {
	    	menu.addPopupMenuListener(popupMenuListener);
	    	
	    	JMenuItem cutMenuItem = new JMenuItem("Cut");
	        cutMenuItem.addActionListener(actionListener);
	        menu.add(cutMenuItem);

	        JMenuItem copyMenuItem = new JMenuItem("Copy");
	        copyMenuItem.addActionListener(actionListener);
	        menu.add(copyMenuItem);

	        JMenuItem pasteMenuItem = new JMenuItem("Paste");
	        pasteMenuItem.addActionListener(actionListener);
	        pasteMenuItem.setEnabled(false);
	        menu.add(pasteMenuItem);

	        menu.addSeparator();

	        JMenuItem findMenuItem = new JMenuItem("Find");
	        findMenuItem.addActionListener(actionListener);
	        menu.add(findMenuItem);

	        MouseListener mouseListener = new JPopupMenuShower(menu);
	        JTree tree1 = (JTree)Listeners.shared[1];
	        JTree tree2 = (JTree)Listeners.shared[2];
	        
	        tree1.addMouseListener(mouseListener);
	        tree2.addMouseListener(mouseListener);

	    }
	    
	    public static void formStandardMenu(JPopupMenu menu)
	    {
	    	JMenuItem cutMenuItem = new JMenuItem("Ct");
	        cutMenuItem.addActionListener(actionListener);
	        menu.add(cutMenuItem);

	        JMenuItem copyMenuItem = new JMenuItem("Cpy");
	        copyMenuItem.addActionListener(actionListener);
	        menu.add(copyMenuItem);

	        JMenuItem pasteMenuItem = new JMenuItem("Pste");
	        pasteMenuItem.addActionListener(actionListener);
	        pasteMenuItem.setEnabled(false);
	        menu.add(pasteMenuItem);

	        menu.addSeparator();

	        JMenuItem findMenuItem = new JMenuItem("Fnd");
	        findMenuItem.addActionListener(actionListener);
	        menu.add(findMenuItem);

	        MouseListener mouseListener = new JPopupMenuShower(menu);
	        JTree tree1 = (JTree)Listeners.shared[1];
	        JTree tree2 = (JTree)Listeners.shared[2];
	        
	        tree1.addMouseListener(mouseListener);
	        tree2.addMouseListener(mouseListener);

	    }

	    static PopupMenuListener popupMenuListener = new PopupMenuListener() {
	      public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
	        System.out.println("Canceled");
	      }

	      public void popupMenuWillBecomeInvisible(
	          PopupMenuEvent popupMenuEvent) {
	        System.out.println("Becoming Invisible");
	      }

	      public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
	        System.out.println("Becoming Visible");
	      }
	    };
/*
	    JFrame frame = new JFrame("Popup Example");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JPopupMenu popupMenu = new JPopupMenu();
	    popupMenu.addPopupMenuListener(popupMenuListener);

	    JMenuItem cutMenuItem = new JMenuItem("Cut");
	    cutMenuItem.addActionListener(actionListener);
	    popupMenu.add(cutMenuItem);

	    JMenuItem copyMenuItem = new JMenuItem("Copy");
	    copyMenuItem.addActionListener(actionListener);
	    popupMenu.add(copyMenuItem);

	    JMenuItem pasteMenuItem = new JMenuItem("Paste");
	    pasteMenuItem.addActionListener(actionListener);
	    pasteMenuItem.setEnabled(false);
	    popupMenu.add(pasteMenuItem);

	    popupMenu.addSeparator();

	    JMenuItem findMenuItem = new JMenuItem("Find");
	    findMenuItem.addActionListener(actionListener);
	    popupMenu.add(findMenuItem);

	    MouseListener mouseListener = new JPopupMenuShower(popupMenu);
	    frame.addMouseListener(mouseListener);

	    frame.setSize(350, 250);
	    frame.setVisible(true);
	  }*/
	}

	class JPopupMenuShower extends MouseAdapter {

	  private JPopupMenu popup;

	  public JPopupMenuShower(JPopupMenu popup) {
	    this.popup = popup;
	  }

	  private void showIfPopupTrigger(MouseEvent mouseEvent) {
	    if (popup.isPopupTrigger(mouseEvent)) {
	      popup.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent
	          .getY());
	    }
	  }

	  public void mousePressed(MouseEvent mouseEvent) {
	    showIfPopupTrigger(mouseEvent);
	  }

	  public void mouseReleased(MouseEvent mouseEvent) {
	    showIfPopupTrigger(mouseEvent);
	  }
	}