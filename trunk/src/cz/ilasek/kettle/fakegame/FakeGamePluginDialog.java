/*
 * Created on 18-mei-2003
 *
 */

package cz.ilasek.kettle.fakegame;

import game.classifiers.Classifiers;
import game.configuration.ClassWithConfigBean;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.row.ValueMetaAndData;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.errorhandling.StreamInterface;
import org.pentaho.di.ui.core.dialog.EnterValueDialog;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

import configuration.MainConfigurationTree;
import configuration.MainConfigurationTreeDialog;

public class FakeGamePluginDialog extends BaseStepDialog implements StepDialogInterface
{
	private FakeGamePluginMeta input;
	private ValueMetaAndData value;

	private Label        wlValName;
	private Text         wValName;
	private FormData     fdlValName, fdValName;

	private Label        wlValue;
	private Button       wbValue;
	private Text         wValue;
	private FormData     fdlValue, fdbValue, fdValue;
	
	private Button       wShowDialog;
	private Listener     lsShowDialog;
	
	public FakeGamePluginDialog(Shell parent, Object in, TransMeta transMeta, String sname)
	{
		super(parent, (BaseStepMeta)in, transMeta, sname);
		input=(FakeGamePluginMeta)in;
//		value = input.getValue();
	}

	public String open()
	{
	    Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
	    
		// Dialog box pro zobrazeni input fieldu
		Shell parent = getParent();
		Display display = parent.getDisplay();
		
		// Nastavime zobrazeni dialogu
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
		props.setLook( shell );
        // input je DummyPluginMeta, na jeho zaklade se zjistit id pluginu 
		// a nahraje se prislusny obrazek v levem hornim rohu
		setShellImage(shell, input);
	

		// listener poslouchajici nad vsemi tremi policky formulare
		ModifyListener lsMod = new ModifyListener() 
		{
			public void modifyText(ModifyEvent e) 
			{
				input.setChanged();
			}
		};
		changed = input.hasChanged();

		// Nastaveni zobrazeni fomrulare
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth  = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;

		// predame nastaveni formulare do dialogu
		shell.setLayout(formLayout);
		// nastavime titulek
		shell.setText(Messages.getString("DummyPluginDialog.Shell.Title")); //$NON-NLS-1$
		
		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;

		// Stepname line
		// vytvorime popisek k step name policku
		wlStepname=new Label(shell, SWT.RIGHT);
		wlStepname.setText(Messages.getString("DummyPluginDialog.StepName.Label")); //$NON-NLS-1$
        props.setLook( wlStepname );
		
        // samotne policko
        fdlStepname=new FormData();
		fdlStepname.left = new FormAttachment(0, 0);
		fdlStepname.right= new FormAttachment(middle, -margin);
		fdlStepname.top  = new FormAttachment(0, margin);
		wlStepname.setLayoutData(fdlStepname);
		
		// text pro step name
		wStepname=new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		wStepname.setText(stepname);
        props.setLook( wStepname );
        
        // pridame listener na text ve formulari
		wStepname.addModifyListener(lsMod);
		
		// nastavime vzhled textu ve formulari - odsazeni
		fdStepname=new FormData();
		fdStepname.left = new FormAttachment(middle, 0);
		fdStepname.top  = new FormAttachment(0, margin);
		fdStepname.right= new FormAttachment(100, 0);
		wStepname.setLayoutData(fdStepname);
		
		// ValName line
//		wlValName=new Label(shell, SWT.RIGHT);
//		wlValName.setText(Messages.getString("DummyPluginDialog.ValueName.Label")); //$NON-NLS-1$
//        props.setLook( wlValName );
//		fdlValName=new FormData();
//		fdlValName.left = new FormAttachment(0, 0);
//		fdlValName.right= new FormAttachment(middle, -margin);
//		fdlValName.top  = new FormAttachment(wStepname, margin);
//		wlValName.setLayoutData(fdlValName);
		
//		wValName=new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
//        props.setLook( wValName );
//		wValName.addModifyListener(lsMod);
//		fdValName=new FormData();
//		fdValName.left = new FormAttachment(middle, 0);
//		fdValName.right= new FormAttachment(100, 0);
//		fdValName.top  = new FormAttachment(wStepname, margin);
//		wValName.setLayoutData(fdValName);
		/////////
		
		// Value line
		wlValue=new Label(shell, SWT.RIGHT);
		wlValue.setText("KECO CANC"); //$NON-NLS-1$
        props.setLook( wlValue );
		fdlValue=new FormData();
		fdlValue.left = new FormAttachment(0, 0);
		fdlValue.right= new FormAttachment(middle, -margin);
//		fdlValue.top  = new FormAttachment(wValName, margin);
		fdlValue.top = new FormAttachment(wStepname, margin);
		wlValue.setLayoutData(fdlValue);

		wbValue=new Button(shell, SWT.PUSH| SWT.CENTER);
        props.setLook( wbValue );
		wbValue.setText(Messages.getString("System.Button.Edit")); //$NON-NLS-1$
		fdbValue=new FormData();
		fdbValue.right= new FormAttachment(100, 0);
//		fdbValue.top  = new FormAttachment(wValName, margin);
		fdbValue.top = new FormAttachment(wStepname, margin);
		wbValue.setLayoutData(fdbValue);

		wValue=new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        props.setLook( wValue );
		wValue.addModifyListener(lsMod);
		fdValue=new FormData();
		fdValue.left = new FormAttachment(middle, 0);
		fdValue.right= new FormAttachment(wbValue, -margin);
//		fdValue.top  = new FormAttachment(wValName, margin);
		fdValue.top = new FormAttachment(wStepname, margin);
		wValue.setLayoutData(fdValue);

		wbValue.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent arg0)
			{
				ValueMetaAndData v = (ValueMetaAndData) value.clone();
				EnterValueDialog evd = new EnterValueDialog(shell, SWT.NONE, v.getValueMeta(), v.getValueData());
				ValueMetaAndData newval = evd.open();
				if (newval!=null)
				{
					value = newval;
					getData();
				}
			}
		});

		// Some buttons
		wOK=new Button(shell, SWT.PUSH);
		wOK.setText(Messages.getString("System.Button.OK")); //$NON-NLS-1$
		wCancel=new Button(shell, SWT.PUSH);
		wCancel.setText(Messages.getString("System.Button.Cancel")); //$NON-NLS-1$
		wShowDialog = new Button(shell, SWT.PUSH);
		wShowDialog.setText("Zobrazit dialog 1234");


        BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel, wShowDialog}, margin, wValue);
        
		// Add listeners
		lsCancel   = new Listener() { public void handleEvent(Event e) { cancel(); } };
		lsOK       = new Listener() { public void handleEvent(Event e) { ok();     } };
		lsShowDialog = new Listener() { public void handleEvent(Event e) { showDialog();     } };
		
		wCancel.addListener(SWT.Selection, lsCancel);
		wOK.addListener    (SWT.Selection, lsOK    );
		wShowDialog.addListener(SWT.Selection, lsShowDialog);
		
		
		
		
		lsDef=new SelectionAdapter() { public void widgetDefaultSelected(SelectionEvent e) { ok(); } };
		wStepname.addSelectionListener( lsDef );
//		wValName.addSelectionListener( lsDef );
		// Detect X or ALT-F4 or something that kills this window...
		shell.addShellListener(	new ShellAdapter() { public void shellClosed(ShellEvent e) { cancel(); } } );

		// Set the shell size, based upon previous time...
		setSize();
		getData();
		input.setChanged(changed);
		shell.open();
		while (!shell.isDisposed())
		{
		    if (!display.readAndDispatch()) display.sleep();
		}
		return stepname;
	}
	
	// Read data from input (TextFileInputInfo)
	/**
	 * Vlozi data z promenne value do prislusneho formularoveho policka.
	 */
	public void getData()
	{
		wStepname.selectAll();
		if (value!=null)
		{
//			wValName.setText(value.getValueMeta().getName());
			wValue.setText(value.toString()+" ("+value.toStringMeta()+")"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	private void cancel()
	{
		stepname=null;
		input.setChanged(changed);
		dispose();
	}
	
	private void ok()
	{
		stepname = wStepname.getText(); // return value
        ClassWithConfigBean conf = (ClassWithConfigBean) MainConfigurationTree.getInstance().getRootModule();
        conf.getCfgBean().toString();
        Classifiers.getInstance().createNewClassifier(conf); 
        Classifiers.getInstance().getClassifier(0).getName();		
		dispose();
	}
	
	private void showDialog()
	{
        MainConfigurationTreeDialog.showConfigDialog();	
	}
	
	/**
	 * Nastavi nazvy dvou kroku, ktere posilaji vstupni stream.
	 * 
	 * @param meta
	 */
	private void setStepMeta(FakeGamePluginMeta meta)
	{
        List<StreamInterface> infoStreams = meta.getStepIOMeta().getInfoStreams();

        String [] stepNames = wValue.getText().split(";");
        
        infoStreams.get(0).setStepMeta( transMeta.findStep( stepNames[0] ) );
        infoStreams.get(1).setStepMeta( transMeta.findStep( stepNames[1] ) );	    
	}
}
;