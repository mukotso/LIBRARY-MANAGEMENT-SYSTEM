package LMS;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FileChooser {
   private Dialog mainFrame;
  
   private String filePath;   
   private final String[] allowedFileExtensions = new String[] {"jpg", "png", "jpeg"};
  
   public FileChooser(){
          prepareGUI();
   }

 private void prepareGUI(){

	 mainFrame = new Dialog(null, "Cover Page Picture", Dialog.ModalityType.APPLICATION_MODAL);
	 mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(ADDBOOKS.class.getResource("/images/images (7).jpeg")));
	 mainFrame.setBounds(1000, 200, 100, 50);
	 mainFrame.setBackground(new Color(0, 153, 153));
	 mainFrame.setSize(200,200);
	 mainFrame.setLayout(new GridLayout(3, 1));
     
	 mainFrame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent windowEvent) {
    	  mainFrame.dispose();
      }
  });


 }


  public void showFileDialogDemo(){
     final FileDialog fileDialog = new FileDialog(mainFrame,"Select Picture");
      Button showFileDialogButton = new Button("Select Picture");
      showFileDialogButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
          FileDialog fileDialog = new FileDialog(mainFrame, "Select Cover Photo", FileDialog.LOAD);
          fileDialog.setDirectory("C:\\Users\\user\\pictures\\coverpics");
          fileDialog.setFile("*.png;*.jpeg;*.jpg;");
          
          fileDialog.setVisible(true);
          if(acceptFile(fileDialog.getDirectory() + fileDialog.getFile())==true) {
        	  setFilePath(fileDialog.getDirectory() + fileDialog.getFile());
          }
          else if(acceptFile(fileDialog.getDirectory() + fileDialog.getFile())==false) {
        	  setFilePath("exception");
          }
          
          // This is to make sure the code resumes where it was blocked
           mainFrame.setVisible(false);
             }
          });
      mainFrame.add(showFileDialogButton);
      mainFrame.setVisible(true);

  }

   public void setFilePath(String file) {
       this.filePath = file;
   }

   public String getFilePath() {
       return filePath;
   }
   
   public boolean acceptFile(String pathname) {
   	// TODO Auto-generated method stub
   	for (String extension : allowedFileExtensions)
   	{
   		if(pathname.toLowerCase().endsWith(extension))
   		{
   			return true;
   		}
   	}
   	return false;
   }

}