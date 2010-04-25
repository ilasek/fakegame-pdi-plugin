/*
 * Created on 18-mei-2003
 *
 */

package cz.ilasek.kettle.fakegame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Props;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

public class FakeGamePluginDialog extends BaseStepDialog implements StepDialogInterface
{
	private FakeGamePluginMeta currentMeta;
	private FakeGamePluginMeta originalMeta;

	private CTabFolder wTabFolder;
	private FormData fdTabFolder;
	
	private CTabItem wFileTab;
	private CTabItem wModelTab;
	private CTabItem wMappingsTab;
	
	private Label wlFilename;
	private FormData fdlFilename;
	private Button wbFilename;
	private FormData fdbFilename;
	private TextVar wFilename;
	private FormData fdFilename;
	
	private Label wOutputProbsLab;
	private Button wOutputProbs;
	private FormData fdlOutputProbs;
	private FormData fdOutputProbs;
	
	private FormData fdFileComp;
	
	private Text wModelText;
	private FormData fdModelText;
	private FormData fdModelComp;
	
    private Text wMappingsText;
    private FormData fdMappingsText;
    private FormData fdMappingsComp;	
	
	public FakeGamePluginDialog(Shell parent, Object in, TransMeta transMeta, String sname)
	{
		super(parent, (BaseStepMeta) in, transMeta, sname);
		currentMeta = (FakeGamePluginMeta) in;
		originalMeta = (FakeGamePluginMeta) currentMeta.clone();
	}

	public String open()
	{
		// Dialog box pro zobrazeni currentMeta fieldu
		Shell parent = getParent();
		Display display = parent.getDisplay();
		
		// Nastavime zobrazeni dialogu
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
		props.setLook( shell );
        // currentMeta je DummyPluginMeta, na jeho zaklade se zjistit id pluginu 
		// a nahraje se prislusny obrazek v levem hornim rohu
		setShellImage(shell, currentMeta);
	

		// listener poslouchajici nad vsemi tremi policky formulare
		ModifyListener lsMod = new ModifyListener() 
		{
			public void modifyText(ModifyEvent e) 
			{
				currentMeta.setChanged();
			}
		};
		changed = currentMeta.hasChanged();

		// Nastaveni zobrazeni fomrulare
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth  = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;

		// predame nastaveni formulare do dialogu
		shell.setLayout(formLayout);
		// nastavime titulek
		shell.setText(Messages.getString("FakeGamePluginDialog.Shell.Title"));
		
		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;

		// Stepname line
		// vytvorime popisek k step name policku
		wlStepname=new Label(shell, SWT.RIGHT);
		wlStepname.setText(Messages.getString("FakeGamePluginDialog.StepName.Label"));
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
		
		// TabFolder
	    wTabFolder = new CTabFolder(shell, SWT.BORDER);
	    props.setLook(wTabFolder, Props.WIDGET_STYLE_TAB);
	    wTabFolder.setSimple(false);	    

        // Start FileTab
        wFileTab = new CTabItem(wTabFolder, SWT.NONE);
        wFileTab.setText(Messages.getString("FakeGamePluginDialog.FileTab.TabTitle"));
        
        Composite wFileComp = new Composite(wTabFolder, SWT.NONE);
        props.setLook(wFileComp);
        
        FormLayout fileLayout = new FormLayout();
        fileLayout.marginWidth  = 3;
        fileLayout.marginHeight = 3;
        wFileComp.setLayout(fileLayout);	    
		
        // Filename line
        wlFilename = new Label(wFileComp, SWT.RIGHT);
        wlFilename.
          setText(Messages.getString("FakeGamePluginDialog.Filename.Label"));
        props.setLook(wlFilename);
        fdlFilename = new FormData();
        fdlFilename.left = new FormAttachment(0, 0);
        fdlFilename.top = new FormAttachment(0, margin);
        fdlFilename.right = new FormAttachment(middle, -margin);
        wlFilename.setLayoutData(fdlFilename);  
        

        wbFilename=new Button(wFileComp, SWT.PUSH| SWT.CENTER);
        props.setLook(wbFilename);
        wbFilename.setText(Messages.getString("System.Button.Browse"));
        fdbFilename=new FormData();
        fdbFilename.right = new FormAttachment(100, 0);
        fdbFilename.top = new FormAttachment(0, 0);
        wbFilename.setLayoutData(fdbFilename);
     
        // combined text field and env variable widget
        wFilename = new TextVar(transMeta, wFileComp, 
                                  SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        props.setLook(wFilename);
        wFilename.addModifyListener(lsMod);
        fdFilename=new FormData();
        fdFilename.left = new FormAttachment(middle, 0);
        fdFilename.top = new FormAttachment(0, margin);
        fdFilename.right = new FormAttachment(wbFilename, -margin);
        wFilename.setLayoutData(fdFilename);    
        
        wOutputProbsLab = new Label(wFileComp, SWT.RIGHT);
        wOutputProbsLab.setText(Messages.getString("FakeGamePluginDialog.OutputProbs.Label"));
        props.setLook(wOutputProbsLab);
        fdlOutputProbs = new FormData();
        fdlOutputProbs.left = new FormAttachment(0, 0);
        fdlOutputProbs.top = new FormAttachment(wFilename, margin);
        fdlOutputProbs.right = new FormAttachment(middle, -margin);
        wOutputProbsLab.setLayoutData(fdlOutputProbs);
        wOutputProbs = new Button(wFileComp, SWT.CHECK);
        props.setLook(wOutputProbs);
        fdOutputProbs = new FormData();
        fdOutputProbs.left = new FormAttachment(middle, 0);
        fdOutputProbs.top = new FormAttachment(wFilename, margin);
        fdOutputProbs.right = new FormAttachment(100, 0);
        wOutputProbs.setLayoutData(fdOutputProbs);
        
        
        fdFileComp = new FormData();
        fdFileComp.left = new FormAttachment(0, 0);
        fdFileComp.top = new FormAttachment(0, 0);
        fdFileComp.right = new FormAttachment(100, 0);
        fdFileComp.bottom = new FormAttachment(100, 0);
        wFileComp.setLayoutData(fdFileComp);
        
        wFileComp.layout();
        wFileTab.setControl(wFileComp);        

        
        // Model Tab
        wModelTab = new CTabItem(wTabFolder, SWT.NONE);
        wModelTab.setText(Messages.getString("FakeGamePluginDialog.ModelTab.TabTitle"));
        
        Composite wModelComp = new Composite(wTabFolder, SWT.NONE);
        props.setLook(wModelComp);
        
        FormLayout modelLayout = new FormLayout();
        modelLayout.marginWidth  = 3;
        modelLayout.marginHeight = 3;
        wModelComp.setLayout(modelLayout);        
        
        
        wModelComp.layout();
        wModelTab.setControl(wModelComp);
        
        wModelText = new Text(wModelComp, 
                SWT.MULTI | 
                SWT.BORDER |
                SWT.V_SCROLL |
                SWT.H_SCROLL);
        wModelText.setEditable(false);
        FontData fd = new FontData("Courier New", 10, SWT.NORMAL);
        wModelText.setFont(new Font(getParent().getDisplay(), fd));
        
        //    m_wModelText.setText(stepname);
        props.setLook(wModelText);
        // format the model text area
        fdModelText = new FormData();
        fdModelText.left = new FormAttachment(0, 0);
        fdModelText.top = new FormAttachment(0, margin);
        fdModelText.right = new FormAttachment(100, 0);
        fdModelText.bottom = new FormAttachment(100, 0);
        wModelText.setLayoutData(fdModelText);
        
        
        fdModelComp = new FormData();
        fdModelComp.left = new FormAttachment(0, 0);
        fdModelComp.top = new FormAttachment(0, 0);
        fdModelComp.right = new FormAttachment(100, 0);
        fdModelComp.bottom = new FormAttachment(100, 0);
        wModelComp.setLayoutData(fdModelComp);
        
        wModelComp.layout();
        wModelTab.setControl(wModelComp);
        
        // Mappings Tab
        wMappingsTab = new CTabItem(wTabFolder, SWT.NONE);
        wMappingsTab.setText(Messages.getString("FakeGamePluginDialog.MappingsTab.TabTitle"));
        
        Composite wMappingsComp = new Composite(wTabFolder, SWT.NONE);
        props.setLook(wMappingsComp);
        
        FormLayout mappingsLayout = new FormLayout();
        mappingsLayout.marginWidth  = 3;
        mappingsLayout.marginHeight = 3;
        wMappingsComp.setLayout(mappingsLayout);        
        
        
        wMappingsComp.layout();
        wMappingsTab.setControl(wMappingsComp);
        
        wMappingsText = new Text(wMappingsComp, 
                SWT.MULTI | 
                SWT.BORDER |
                SWT.V_SCROLL |
                SWT.H_SCROLL);
        wMappingsText.setEditable(false);
        wMappingsText.setFont(new Font(getParent().getDisplay(), fd));
        
        //    m_wMappingsText.setText(stepname);
        props.setLook(wMappingsText);
        // format the mappings text area
        fdMappingsText = new FormData();
        fdMappingsText.left = new FormAttachment(0, 0);
        fdMappingsText.top = new FormAttachment(0, margin);
        fdMappingsText.right = new FormAttachment(100, 0);
        fdMappingsText.bottom = new FormAttachment(100, 0);
        wMappingsText.setLayoutData(fdMappingsText);
        
        
        fdMappingsComp = new FormData();
        fdMappingsComp.left = new FormAttachment(0, 0);
        fdMappingsComp.top = new FormAttachment(0, 0);
        fdMappingsComp.right = new FormAttachment(100, 0);
        fdMappingsComp.bottom = new FormAttachment(100, 0);
        wMappingsComp.setLayoutData(fdMappingsComp);
        
        wMappingsComp.layout();
        wMappingsTab.setControl(wMappingsComp);        

        // Put whole TabFolder and wStepname in FormData
        fdTabFolder = new FormData();
        fdTabFolder.left  = new FormAttachment(0, 0);
        fdTabFolder.top   = new FormAttachment(wStepname, margin);
        fdTabFolder.right = new FormAttachment(100, 0);
        fdTabFolder.bottom= new FormAttachment(100, -50);
        wTabFolder.setLayoutData(fdTabFolder);        
        
        // bottom buttons
        
		// Some buttons
		wOK = new Button(shell, SWT.PUSH);
		wOK.setText(Messages.getString("System.Button.OK")); //$NON-NLS-1$
		wCancel = new Button(shell, SWT.PUSH);
		wCancel.setText(Messages.getString("System.Button.Cancel")); //$NON-NLS-1$

        BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel}, margin, wTabFolder);
        
		// Add listeners
		lsCancel = new Listener() { public void handleEvent(Event e) { cancel(); } };
		lsOK = new Listener() { public void handleEvent(Event e) { ok();     } };
		
		wCancel.addListener(SWT.Selection, lsCancel);
		wOK.addListener    (SWT.Selection, lsOK    );
		
		lsDef = new SelectionAdapter() { public void widgetDefaultSelected(SelectionEvent e) { ok(); } };
		wStepname.addSelectionListener( lsDef );

		// Detect X or ALT-F4 or something that kills this window...
		shell.addShellListener(	new ShellAdapter() { public void shellClosed(ShellEvent e) { cancel(); } } );

        wFilename.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                wFilename.setToolTipText(transMeta
                        .environmentSubstitute(wFilename.getText()));
            }
        });

        // listen to the file name text box and try to load a model
        // if the user presses enter
        wFilename.addSelectionListener(new SelectionAdapter() {
            public void widgetDefaultSelected(SelectionEvent e) {
                loadModel(wFilename.getText());
                visualizeMappings();
            }
        });

        wbFilename.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(shell, SWT.OPEN);
                String[] extensions = null;
                String[] filterNames = null;
                extensions = new String[2];
                filterNames = new String[2];
                extensions[0] = "*.net";
                filterNames[0] = Messages
                        .getString("FakeGamePluginDialog.FileType.PropertiesModelFile");
                extensions[1] = "*";
                filterNames[1] = Messages
                        .getString("System.FileType.AllFiles");
                dialog.setFilterExtensions(extensions);
                
                if (wFilename.getText() != null) {
                    dialog.setFileName(transMeta.environmentSubstitute(wFilename.getText()));
                }
                dialog.setFilterNames(filterNames);

                if (dialog.open() != null) {
                    wFilename.setText(dialog.getFilterPath()
                            + System.getProperty("file.separator")
                            + dialog.getFileName());
                    
                    loadModel(wFilename.getText());
                    visualizeMappings();
                }
            }
        });
        
        wTabFolder.setSelection(0);
        
		// Set the shell size, based upon previous time...
		setSize();
		getData();
		shell.open();
		
		while (!shell.isDisposed())
		{
		    if (!display.readAndDispatch()) 
		        display.sleep();
		}
		
		return stepname;
	}
	
	private boolean loadModel(String modelsFileName)
	{
        try {
            currentMeta.setModelsFileName(modelsFileName, transMeta);
            String serializedModels = currentMeta.getSerializedModels();
            if (serializedModels != null)
            {
                wModelText.setText(serializedModels);
            
                return true;
            }
        } catch (KettleStepException e1) {
            log.logError(Messages.getString("FakeGamePluginDialog.Log.FileLoadingError") + ": " + e1.getMessage());
        }
        return false;
	}
	
	private void visualizeMappings()
	{
        ModelsFacade models = currentMeta.getModels();
        

        try {
            RowMetaInterface inputRowMeta = getInputRowMeta();
            models.generateMappings(inputRowMeta.getValueMetaList());
            StringBuilder mappingStr = new StringBuilder();
            mappingStr.append(Messages.getString("FakeGamePluginDialog.Mapping.Title") + "\n");
            mappingStr.append("--------------------------------------------\n");
            
            int inputIndex = 0;
            for (String input : models.getInputs())
            {
                mappingStr.append(input);
                mappingStr.append(" => ");

                Integer rowIndex = models.mapInputToRow(inputIndex);
                if (rowIndex != null)
                    mappingStr.append("[" + inputRowMeta.getValueMeta(rowIndex).getName() + "]\n");
                else
                    mappingStr.append(Messages.getString("FakeGamePluginDialog.Mapping.NoMapping") + "\n");
                
                inputIndex++;
            }
            
            wMappingsText.setText(mappingStr.toString());
        } catch (KettleStepException e) {
            log.logError(
                    "[FakeGamePluginDialog]",
                    Messages.getString("FakeGamePluginDialog.Log.MappingsGenerationError"));
        }	    
    }
	
	private RowMetaInterface getInputRowMeta() throws KettleStepException
	{
	    StepMeta stepMeta = transMeta.findStep(stepname);
        return transMeta.getPrevStepFields(stepMeta);
	}
		
	// Read data from currentMeta (TextFileInputInfo)
	public void getData()
	{
	    wOutputProbs.setSelection(currentMeta.isShowOutputProbabilities());
	    if (currentMeta.getModelFileName() != null) {
	        wFilename.setText(currentMeta.getModelFileName());
	        wModelText.setText(currentMeta.getSerializedModels());
	        loadModel(wFilename.getText());
	        visualizeMappings();
	    }
	}
	
	private void cancel()
	{
		stepname=null;
		currentMeta.setChanged(changed);
		loadModel(originalMeta.getModelFileName());

		dispose();
	}
	
	private void ok()
	{
		currentMeta.setShowOutputProbabilities(wOutputProbs.getSelection());
		
		if (!Const.isEmpty(wFilename.getText())) {
		    loadModel(wFilename.getText());
		    visualizeMappings();
		} else {
		    loadModel(null);
		}
		
		stepname = wStepname.getText();
		dispose();
	}
}