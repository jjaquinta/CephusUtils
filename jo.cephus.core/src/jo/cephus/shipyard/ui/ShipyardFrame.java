package jo.cephus.shipyard.ui;

import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.RuntimeLogic;

@SuppressWarnings("serial")
public class ShipyardFrame extends JFrame
{
    private final static RuntimeBean mRuntime = RuntimeLogic.getInstance();

    private JMenuBar          mMenuBar;
    private JMenu             mFile;
    private JMenuItem         mOpen;
    private JMenuItem         mSave;
    private JMenuItem         mSaveAs;
    private JMenuItem         mExit;
    private ShipyardPanel     mClient;

    public ShipyardFrame()
    {
        super("Cephus Engine - Shipyard");
        initInstantiate();
        initLayout();
        initLink();
    }

    private void initInstantiate()
    {
        UIManager.getDefaults().put("TitledBorder.font", new javax.swing.plaf.FontUIResource( new Font( "Arial", Font.BOLD, 16 ) ) ) ;
        
        mClient = new ShipyardPanel();
        
        mMenuBar = new JMenuBar();
        mFile = new JMenu("File");
        mOpen = new JMenuItem("Open...");
        mSave = new JMenuItem("Save");
        mSaveAs = new JMenuItem("Save As...");
        mExit = new JMenuItem("Exit");
    }

    private void initLayout()
    {
        getContentPane().add("Center", mClient);
        mMenuBar.add(mFile);
        mFile.add(mOpen);
        mFile.add(mSave);
        mFile.add(mSaveAs);
        mFile.add(mExit);
        setJMenuBar(mMenuBar);
    }

    private void initLink()
    {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                if (mRuntime.isAnyChanges())
                    if (JOptionPane.showConfirmDialog(ShipyardFrame.this, "You have unsaved changes. Do you want to save first?") == JOptionPane.YES_OPTION)
                        doSave();
                super.windowClosed(e);
                RuntimeLogic.shutdown();
                System.exit(0);
            }
        });
        mOpen.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doOpen();
            }
        });
        mSave.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doSave();
            }
        });
        mSaveAs.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doSaveAs();
            }
        });
        mExit.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doExit();
            }
        });
    }
    
    // actions
    
    private void doOpen()
    {
        RuntimeBean mRuntime = RuntimeLogic.getInstance();
        if (mRuntime.isAnyChanges())
            if (JOptionPane.showConfirmDialog(ShipyardFrame.this, "You have unsaved changes. Do you want to save first?") == JOptionPane.YES_OPTION)
                doSave();        
        FileDialog fd = new FileDialog(this, "Open Narrative", FileDialog.LOAD);
        if (mRuntime.getFile() != null)
            fd.setDirectory(mRuntime.getFile().getParent());
        else
            fd.setDirectory(System.getProperty("user.home"));
        if (mRuntime.getFile() != null)
            fd.setFile(mRuntime.getFile().getName());
        else
            fd.setFile("shipyard.json");
        fd.setVisible(true);
        if (fd.getDirectory() == null)
            return;
        String fileName = fd.getDirectory()+System.getProperty("file.separator")+fd.getFile();
        try
        {
            RuntimeLogic.loadAs(new File(fileName));
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error writing "+fileName, JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void doSave()
    {
        if (mRuntime.getFile() == null)
            doSaveAs();
        else
            try
            {
                RuntimeLogic.save();
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Error writing "+mRuntime.getFile(), JOptionPane.ERROR_MESSAGE);
            }
    }
    
    private void doSaveAs()
    {
        FileDialog fd = new FileDialog(this, "Save Narrative", FileDialog.SAVE);
        if (mRuntime.getFile() != null)
            fd.setDirectory(mRuntime.getFile().getParent());
        else
            fd.setDirectory(System.getProperty("user.home"));
        if (mRuntime.getFile() != null)
            fd.setFile(mRuntime.getFile().getName());
        else
            fd.setFile("shipyard.json");
        fd.setVisible(true);
        if (fd.getDirectory() == null)
            return;
        String fileName = fd.getDirectory()+System.getProperty("file.separator")+fd.getFile();
        try
        {
            RuntimeLogic.saveAs(new File(fileName));
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, e.toString(), "Error writing "+fileName, JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void doExit()
    {
        if (mRuntime.isAnyChanges())
            if (JOptionPane.showConfirmDialog(ShipyardFrame.this, "You have unsaved changes. Do you want to save first?") == JOptionPane.YES_OPTION)
                doSave();
        RuntimeLogic.shutdown();
        System.exit(0);
    }
}
