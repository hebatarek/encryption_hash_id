import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.wb.swt.SWTResourceManager;
import org.hashids.Hashids;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Button;

public class MainWindow {

	protected Shell shell;
	private Text playerID;
	private Text encryptedID;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION_TEXT));
		shell.setSize(450, 300);

		playerID = new Text(shell, SWT.BORDER);
		playerID.setBounds(189, 46, 163, 19);

		Label playerIDLabel = new Label(shell, SWT.NONE);
		playerIDLabel.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.BOLD));
		playerIDLabel.setAlignment(SWT.CENTER);
		playerIDLabel.setBounds(44, 46, 92, 19);
		playerIDLabel.setText("Player ID");
		playerIDLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		Label encryptedIDLabel = new Label(shell, SWT.NONE);
		encryptedIDLabel.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.BOLD));
		encryptedIDLabel.setAlignment(SWT.CENTER);
		encryptedIDLabel.setBounds(44, 139, 106, 19);
		encryptedIDLabel.setText("Encrypted ID");
		encryptedIDLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		encryptedID = new Text(shell, SWT.BORDER);
		encryptedID.setBounds(189, 139, 163, 19);
		

		Button btnEncrypt = new Button(shell, SWT.NONE);
		btnEncrypt.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.BOLD));
		btnEncrypt.setBounds(159, 83, 94, 34);
		btnEncrypt.setText("Encrypt");
		btnEncrypt.setEnabled(false);

		Button btnDecrypt = new Button(shell, SWT.NONE);
		btnDecrypt.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.BOLD));
		btnDecrypt.setBounds(159, 173, 94, 34);
		btnDecrypt.setText("Decrypt");
		btnDecrypt.setEnabled(false);
		
		Label result = new Label(shell, SWT.NONE);
		result.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.BOLD));
		result.setEnabled(false);
		result.setAlignment(SWT.CENTER);
		result.setBounds(123, 216, 163, 34);
		result.setText("The Result");
		result.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		playerID.addListener(SWT.FocusIn, new Listener() {
			public void handleEvent(Event event) {
				encryptedID.setText("");
				btnDecrypt.setEnabled(false);
				String plid = playerID.getText();
				if (plid != null && !plid.isEmpty()) {
					btnEncrypt.setEnabled(true);
					
				}
			}
		});
		
		encryptedID.addListener(SWT.FocusIn, new Listener() {
			public void handleEvent(Event event) {
				playerID.setText("");
				btnEncrypt.setEnabled(false);
				String encid = encryptedID.getText();
				if (encid != null && !encid.isEmpty()) {
					btnDecrypt.setEnabled(true);
					
				}
			}
		});
		
		playerID.addModifyListener(new ModifyListener() {
			@Override
		    public void modifyText(ModifyEvent e) {
				btnDecrypt.setEnabled(false);
				String plid = playerID.getText();
				if (plid != null && !plid.isEmpty()) {
					btnEncrypt.setEnabled(true);
					
				}
		    }
		});

		
		
		encryptedID.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				btnEncrypt.setEnabled(false);
				String encid = encryptedID.getText();
				if (encid != null && !encid.isEmpty()) {
					btnDecrypt.setEnabled(true);
					
				}
			}
		});

		btnEncrypt.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String plid = playerID.getText();
				String errorMsg = null;
				encryptedID.setText("");
				if (plid == null || plid.isEmpty()) {

					MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
					messageBox.setText("Alert");
					errorMsg = "Please enter Player ID";
					messageBox.setMessage(errorMsg);
					messageBox.open();
					result.setText("The Result");
					
				}else {
					Hashids hashids=new Hashids("this is code day");
					String encrypted=hashids.encode(Long.parseLong(plid));
					result.setText(encrypted);
				}
			}
		});

		btnDecrypt.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String encid = encryptedID.getText();
				String errorMsg = null;
				playerID.setText("");
				if (encid == null || encid.isEmpty()) {

					MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
					messageBox.setText("Alert");
					errorMsg = "Please enter Encrypted ID";
					messageBox.setMessage(errorMsg);
					messageBox.open();
					result.setText("The Result");

				}else {
					Hashids hashids=new Hashids("this is code day");
					long[] decrypted=hashids.decode(encid);
					if(decrypted.length <= 0) {
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
						messageBox.setText("Alert");
						errorMsg = "Encrypted ID has no matches";
						messageBox.setMessage(errorMsg);
						messageBox.open();
						result.setText("The Result");
					}else {
						result.setText(decrypted[0]+"");
					}
				}
			}
		});
		
		
	}
}
