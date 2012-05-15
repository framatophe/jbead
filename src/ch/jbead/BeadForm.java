/** jbead - http://www.brunoldsoftware.ch
    Copyright (C) 2001-2012  Damian Brunold

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.jbead;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

/**
 * 
 */
public class BeadForm extends JFrame {

    private static final long serialVersionUID = 1L;

    private BeadUndo undo = new BeadUndo();
    private BeadField field = new BeadField();
    private Color colors[] = new Color[10];
    private byte colorIndex;
    private int begin_i;
    private int begin_j;
    private int end_i;
    private int end_j;
    private int sel_i1, sel_i2, sel_j1, sel_j2;
    private boolean selection;
    private BeadField sel_buff = new BeadField();
    private boolean dragging;
    private int grid;
    private int zoomtable[] = new int[5];
    private int zoomIndex;
    private int scroll;
    private int shift;
    private boolean saved;
    private boolean modified;
    private boolean repeatDirty;
    private int repeat;
    private int colorRepeat;
    private String mru[] = new String[6];

    private File file = new File(Texts.text("unnamed", "unbenannt"));

    private ButtonGroup languageGroup = new ButtonGroup();
    private JRadioButtonMenuItem languageEnglish = new JRadioButtonMenuItem("English");
    private JRadioButtonMenuItem languageGerman = new JRadioButtonMenuItem("German");

    private JToolBar toolbar = new JToolBar();
    private JButton sbColor0 = new JButton();
    private JButton sbColor1 = new JButton();
    private JButton sbColor2 = new JButton();
    private JButton sbColor3 = new JButton();
    private JButton sbColor4 = new JButton();
    private JButton sbColor5 = new JButton();
    private JButton sbColor6 = new JButton();
    private JButton sbColor7 = new JButton();
    private JButton sbColor8 = new JButton();
    private JButton sbColor9 = new JButton();

    private JScrollBar scrollbar = new JScrollBar(JScrollBar.VERTICAL);

    private DraftPanel draft = new DraftPanel(field, colors, grid, scroll);
    private NormalPanel normal = new NormalPanel(field, colors, grid, scroll);
    private SimulationPanel simulation = new SimulationPanel(field, colors, grid, scroll, shift);
    private ReportPanel report = new ReportPanel(field, colors, colorRepeat, file);

    private JLabel laDraft = new JLabel("draft");
    private JLabel laNormal = new JLabel("normal");
    private JLabel laSimulation = new JLabel("simulation");
    private JLabel laReport = new JLabel("report");

    private JMenu menuFile = new JMenu("file");
    private JMenuItem fileNew = new JMenuItem("new");
    private JMenuItem fileOpen = new JMenuItem("open");
    private JMenuItem fileSave = new JMenuItem("save");
    private JMenuItem fileSaveas = new JMenuItem("save as");
    private JMenuItem filePrint = new JMenuItem("print");
    private JMenuItem filePrintersetup = new JMenuItem("printer setup");
    private JMenuItem fileExit = new JMenuItem("exit");

    private JMenu menuEdit = new JMenu("edit");
    private JMenuItem editUndo = new JMenuItem("undo");
    private JMenuItem editRedo = new JMenuItem("redo");
    private JMenuItem editCopy = new JMenuItem("arrange");
    private JMenuItem editLine = new JMenuItem("empty line");
    private JMenuItem editInsertline = new JMenuItem("insert");
    private JMenuItem editDeleteline = new JMenuItem("delete");

    private JMenuItem toolTool = new JMenuItem("tool");
    private JMenuItem toolPoint = new JMenuItem("pencil");
    private JMenuItem toolSelect = new JMenuItem("select");
    private JMenuItem toolFill = new JMenuItem("fill");
    private JMenuItem toolSniff = new JMenuItem("pipette");

    private JMenu menuView = new JMenu("view");
    private JMenuItem viewZoomin = new JMenuItem("zoom in");
    private JMenuItem viewZoomout = new JMenuItem("zoom out");
    private JMenuItem viewZoomnormal = new JMenuItem("normal");
    private JMenu viewLanguage = new JMenu("language");

    private JCheckBoxMenuItem viewDraft = new JCheckBoxMenuItem("draft");
    private JCheckBoxMenuItem viewNormal = new JCheckBoxMenuItem("normal");
    private JCheckBoxMenuItem viewSimulation = new JCheckBoxMenuItem("simulation");
    private JCheckBoxMenuItem viewReport = new JCheckBoxMenuItem("report");

    private JMenuItem fileMRU1 = new JMenuItem();
    private JMenuItem fileMRU2 = new JMenuItem();
    private JMenuItem fileMRU3 = new JMenuItem();
    private JMenuItem fileMRU4 = new JMenuItem();
    private JMenuItem fileMRU5 = new JMenuItem();
    private JMenuItem fileMRU6 = new JMenuItem();

    private JPopupMenu.Separator fileMRUSeparator = new JPopupMenu.Separator();

    private JMenu menuPattern = new JMenu("pattern");
    private JMenuItem patternWidth = new JMenuItem("width");

    private JMenu menuInfo = new JMenu("?");
    private JMenuItem infoAbout = new JMenuItem("about jbead");

    private JButton sbNew = new JButton("new");
    private JButton sbOpen = new JButton("open");
    private JButton sbSave = new JButton("save");
    private JButton sbPrint = new JButton("print");
    private JButton sbUndo = new JButton("undo");
    private JButton sbRedo = new JButton("redo");
    private JButton sbRotateleft = new JButton("left");
    private JButton sbRotateright = new JButton("right");
    private JButton sbCopy = new JButton("arrange");
    private JToggleButton sbToolSelect = new JToggleButton("select");
    private JToggleButton sbToolPoint = new JToggleButton("pencil");
    private JToggleButton sbToolFill = new JToggleButton("fill");
    private JToggleButton sbToolSniff = new JToggleButton("pipette");

    private PageFormat pageFormat;

    public BeadForm() {
        super("jbead");
        saved = false;
        modified = false;
        repeatDirty = false;
        selection = false;
        updateTitle();
        field.clear();
        field.setWidth(15);
        colorIndex = 1;
        defaultColors();
        setGlyphColors();
        scroll = 0;
        zoomIndex = 2;
        zoomtable[0] = 6;
        zoomtable[1] = 8;
        zoomtable[2] = 10;
        zoomtable[3] = 12;
        zoomtable[4] = 14;
        grid = zoomtable[zoomIndex];
        loadMRU();
        updateMRU();
        updateScrollbar();

        // init button group
        languageGroup.add(languageEnglish);
        languageGroup.add(languageGerman);

        // init color buttons
        // TODO handle sbColor0 with transparent color and x lines
        sbColor1.setIcon(new ColorIcon(colors[1]));
        sbColor2.setIcon(new ColorIcon(colors[2]));
        sbColor3.setIcon(new ColorIcon(colors[3]));
        sbColor4.setIcon(new ColorIcon(colors[4]));
        sbColor5.setIcon(new ColorIcon(colors[5]));
        sbColor6.setIcon(new ColorIcon(colors[6]));
        sbColor7.setIcon(new ColorIcon(colors[7]));
        sbColor8.setIcon(new ColorIcon(colors[8]));
        sbColor9.setIcon(new ColorIcon(colors[9]));

        Settings settings = new Settings();
        settings.SetCategory("Environment");
        Language language;
        int lang = settings.LoadInt("Language", -1);
        if (lang == -1) { // Windows-Spracheinstellung abfragen
            Locale locale = Locale.getDefault();
            if (locale.getLanguage().equals("de")) {
                lang = 1;
            } else {
                lang = 0;
            }
        }
        language = lang == 0 ? Language.EN : Language.GE;
        Texts.forceLanguage(language,  this);
        if (Texts.active_language == Language.EN) {
            languageEnglish.setSelected(true);
        } else {
            languageGerman.setSelected(true);
        }

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (formCloseQuery()) {
                    System.exit(0);
                }
            }
        });
        
        // TODO persist the pageFormat in Settings?
        pageFormat = PrinterJob.getPrinterJob().defaultPage();
        pageFormat.setOrientation(PageFormat.LANDSCAPE);
    }

    void defaultColors() {
        colors[0] = Color.LIGHT_GRAY; // was clBtnFace
        colors[1] = new Color(128, 0, 0); // maroon
        colors[2] = new Color(0, 0, 128); // navy
        colors[3] = Color.GREEN;
        colors[4] = Color.YELLOW;
        colors[5] = Color.RED;
        colors[6] = Color.BLUE;
        colors[7] = new Color(128, 0, 128); // purple
        colors[8] = Color.BLACK;
        colors[9] = Color.WHITE;
    }

    void setGlyphColors() {
        sbColor1.setIcon(new ColorIcon(colors[1]));
        sbColor2.setIcon(new ColorIcon(colors[2]));
        sbColor3.setIcon(new ColorIcon(colors[3]));
        sbColor4.setIcon(new ColorIcon(colors[4]));
        sbColor5.setIcon(new ColorIcon(colors[5]));
        sbColor6.setIcon(new ColorIcon(colors[6]));
        sbColor7.setIcon(new ColorIcon(colors[7]));
        sbColor8.setIcon(new ColorIcon(colors[8]));
        sbColor9.setIcon(new ColorIcon(colors[9]));
    }

    void formResize() {
        int cheight = getContentPane().getHeight() - toolbar.getHeight();
        int cwidth = getContentPane().getWidth() - scrollbar.getWidth();
        int top = toolbar.getHeight() + 6;

        int nr = 0;
        if (viewDraft.isSelected()) nr++;
        if (viewNormal.isSelected()) nr++;
        if (viewSimulation.isSelected()) nr++;
        if (viewReport.isSelected()) nr += 2;
        if (nr == 0) {
            viewDraft.setSelected(true);
            draft.setVisible(true);
            laDraft.setVisible(true);
            nr = 1;
        }

        int m = 6;

        if (viewDraft.isSelected()) {
            draft.setBounds(m, top, field.getWidth() * grid + 35, cheight - 6 - laDraft.getHeight() - 3);
            laDraft.setLocation(m + (draft.getWidth() - laDraft.getWidth()) / 2, draft.getY() + draft.getHeight() + 2);
            m += draft.getWidth() + 12;
        }

        if (viewNormal.isSelected()) {
            normal.setBounds(m, top, (field.getWidth() + 1) * grid + 10, cheight - 6 - laNormal.getHeight() - 3);
            laNormal.setLocation(m + (normal.getWidth() - laNormal.getWidth()) / 2, normal.getY() + normal.getHeight() + 2);
            m += normal.getWidth() + 12;
        }

        if (viewSimulation.isSelected()) {
            simulation.setBounds(m, top, (field.getWidth() + 2) * grid / 2 + 10, cheight - 6 - laSimulation.getHeight() - 3);
            laSimulation.setLocation(m + (simulation.getWidth() - laSimulation.getWidth()) / 2, simulation.getY() + simulation.getHeight() + 2);
            m += simulation.getWidth() + 12;
        }

        if (viewReport.isSelected()) {
            report.setBounds(m, top, cwidth - m - 6, cheight - 6 - laReport.getHeight() - 3);
            laReport.setLocation(m + 5, report.getY() + report.getHeight() + 2);
        }

        scrollbar.setBounds(getContentPane().getWidth() - scrollbar.getWidth(), top, scrollbar.getWidth(), cheight - 6 - laDraft.getHeight() - 3);

        updateScrollbar();
    }

    void updateScrollbar() {
        int h = draft.getHeight() / grid;
        assert (h < field.getHeight());
        scrollbar.setMinimum(0);
        scrollbar.setMaximum(field.getHeight() - h);
        if (scrollbar.getMaximum() < 0) scrollbar.setMaximum(0);
        scrollbar.setUnitIncrement(h);
        scrollbar.setBlockIncrement(h);
        scrollbar.setValue(scrollbar.getMaximum() - scrollbar.getBlockIncrement() - scroll);
    }

    int correctCoordinatesX(int i, int j) {
        int idx = i + (j + scroll) * field.getWidth();
        int m1 = field.getWidth();
        int m2 = field.getWidth() + 1;
        int k = 0;
        int m = (k % 2 == 0) ? m1 : m2;
        while (idx >= m) {
            idx -= m;
            k++;
            m = (k % 2 == 0) ? m1 : m2;
        }
        i = idx;
        j = k - scroll;
        return i;
    }

    int correctCoordinatesY(int i, int j) {
        int idx = i + (j + scroll) * field.getWidth();
        int m1 = field.getWidth();
        int m2 = field.getWidth() + 1;
        int k = 0;
        int m = (k % 2 == 0) ? m1 : m2;
        while (idx >= m) {
            idx -= m;
            k++;
            m = (k % 2 == 0) ? m1 : m2;
        }
        i = idx;
        j = k - scroll;
        return j;
    }

    void updateBead(int i, int j) {
        // use observer pattern to remove this explicit dependency
        draft.redraw(i, j);
        normal.updateBead(i, j);
        simulation.updateBead(i, j);
    }

    void fileNewClick() {
        // ask whether to save modified document
        if (modified) {
            int answer = JOptionPane.showConfirmDialog(this,
                    Texts.text("Do you want to save your changes?", "Sollen die Änderungen gespeichert werden?"));
            if (answer == JOptionPane.CANCEL_OPTION) return;
            if (answer == JOptionPane.YES_OPTION) {
                fileSaveClick();
            }
        }

        // delete all
        undo.clear();
        field.clear();
        repeat = 0;
        colorRepeat = 0;
        invalidate();
        colorIndex = 1;
        sbColor1.setSelected(true);
        defaultColors();
        setGlyphColors();
        scroll = 0;
        updateScrollbar();
        selection = false;
        sbToolPoint.setSelected(true);
        toolPoint.setSelected(true);
        file = new File(Texts.text("unnamed", "unbenannt"));
        saved = false;
        modified = false;
        updateTitle();
    }

    void loadFile(File file, boolean addtomru) {
        // ask whether to save modified document
        if (modified) {
            int answer = JOptionPane.showConfirmDialog(this,
                    Texts.text("Do you want to save your changes?", "Sollen die Änderungen gespeichert werden?"));
            if (answer == JOptionPane.CANCEL_OPTION) return;
            if (answer == JOptionPane.YES_OPTION) {
                fileSaveClick();
            }
        }

        // Datei laden
        try {
            JBeadInputStream in = new JBeadInputStream(new FileInputStream(file));
            try {
                String strid = in.read(13);
                if (!strid.equals("DB-BEAD/01:\r\n")) {
                    JOptionPane.showMessageDialog(this, Texts.text("The file is not a jbead pattern file. It cannot be loaded.",
                            "Die Datei ist keine jbead Musterdatei. Sie kann nicht geladen werden."));
                    return;
                }
                undo.clear();
                field.clear();
                repeat = 0;
                colorRepeat = 0;
                field.load(in);
                for (int i = 0; i < colors.length; i++) {
                    colors[i] = in.readColor();
                }
                colorIndex = in.read();
                zoomIndex = in.readInt();
                shift = in.readInt();
                scroll = in.readInt();
                viewDraft.setSelected(in.readBool());
                viewNormal.setSelected(in.readBool());
                viewSimulation.setSelected(in.readBool());
                switch (colorIndex) {
                case 0:
                    sbColor0.setSelected(true);
                    break;
                case 1:
                    sbColor1.setSelected(true);
                    break;
                case 2:
                    sbColor2.setSelected(true);
                    break;
                case 3:
                    sbColor3.setSelected(true);
                    break;
                case 4:
                    sbColor4.setSelected(true);
                    break;
                case 5:
                    sbColor5.setSelected(true);
                    break;
                case 6:
                    sbColor6.setSelected(true);
                    break;
                case 7:
                    sbColor7.setSelected(true);
                    break;
                case 8:
                    sbColor8.setSelected(true);
                    break;
                case 9:
                    sbColor9.setSelected(true);
                    break;
                default:
                    assert (false);
                    break;
                }
                setGlyphColors();
                updateScrollbar();
            } finally {
                in.close();
            }
        } catch (IOException e) {
            // xxx
            undo.clear();
            field.clear();
            repeat = 0;
            colorRepeat = 0;
        }
        saved = true;
        modified = false;
        repeatDirty = true;
        this.file = file;
        updateTitle();
        formResize();
        invalidate();
        if (addtomru) addToMRU(file);
    }

    void fileOpenClick() {
        JFileChooser dialog = new JFileChooser();
        dialog.setCurrentDirectory(file.getParentFile());
        if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            loadFile(dialog.getSelectedFile(), true);
        }
    }

    void fileSaveClick() {
        if (saved) {
            // Einfach abspeichern...
            try {
                JBeadOutputStream out = new JBeadOutputStream(new FileOutputStream(file));
                try {
                    out.write("DB-BEAD/01:\r\n");
                    field.save(out);
                    for (Color color : colors) {
                        out.writeColor(color);
                    }
                    out.writeInt(colorIndex);
                    out.writeInt(zoomIndex);
                    out.writeInt(shift);
                    out.writeInt(scroll);
                    out.writeBool(viewDraft.isSelected());
                    out.writeBool(viewNormal.isSelected());
                    out.writeBool(viewSimulation.isSelected());
                    modified = false;
                    updateTitle();
                } finally {
                    out.close();
                }
            } catch (IOException e) {
                // xxx
            }
        } else {
            fileSaveasClick();
        }
    }

    void fileSaveasClick() {
        JFileChooser dialog = new JFileChooser();
        if (dialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (dialog.getSelectedFile().exists()) {
                String msg = Texts.text("The file ", "Die Datei ") + dialog.getSelectedFile().getName()
                        + Texts.text(" already exists. Do you want to overwrite it?", " existiert bereits. Soll sie überschrieben werden?");
                if (JOptionPane.showConfirmDialog(this, msg, "Overwrite", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            file = dialog.getSelectedFile();
            saved = true;
            fileSaveClick();
            addToMRU(file);
        }
    }

    void filePrintClick(ActionEvent event) {
        try {
            Object Sender = event.getSource();
            if (Sender != sbPrint) {
                PrinterJob pj = PrinterJob.getPrinterJob();
                if (pj.printDialog()) {
                    pj.setPrintable(new Printable() {
                        @Override
                        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                            if (pageIndex == 0) {
                                printAll(graphics, pageFormat, pageIndex);
                                return PAGE_EXISTS;
                            } else {
                                return NO_SUCH_PAGE;
                            }
                        }
                    }, pageFormat);
                    pj.print();
                }
            } else {
                PrinterJob pj = PrinterJob.getPrinterJob();
                pj.setPrintable(new Printable() {
                    @Override
                    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                        if (pageIndex == 0) {
                            printAll(graphics, pageFormat, pageIndex);
                            return PAGE_EXISTS;
                        } else {
                            return NO_SUCH_PAGE;
                        }
                    }
                }, pageFormat);
                pj.print();
            }
        } catch (PrinterException e) {
            // TODO show error dialog
        }
    }

    void filePrintersetupClick() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pageFormat = pj.pageDialog(pj.defaultPage());
    }

    void fileExitClick() {
        if (modified) {
            int r = JOptionPane.showConfirmDialog(this,
                    Texts.text("Do you want to save your changes?", "Sollen die Änderungen gespeichert werden?"));
            if (r == JOptionPane.CANCEL_OPTION) return;
            if (r == JOptionPane.OK_OPTION) fileSaveClick();
        }
        // TODO maybe need to save settings?
        System.exit(0);
    }

    void patternWidthClick() {
        int old = field.getWidth();
        PatternWidthForm form = new PatternWidthForm();
        form.setWidth(field.getWidth());
        form.formShow();
        if (form.isOK()) {
            undo.snapshot(field, modified);
            field.setWidth(form.getWidth());
            formResize();
            invalidate();
            if (!modified) {
                modified = (old != field.getWidth());
            }
            updateTitle();
            repeatDirty = true;
        }
    }

    int calcLineCoordX(int _i1, int _j1, int _i2, int _j2) {
        int dx = Math.abs(_i2 - _i1);
        int dy = Math.abs(_j2 - _j1);
        if (2 * dy < dx) {
            _j2 = _j1;
        } else if (2 * dx < dy) {
            _i2 = _i1;
        } else {
            int d = Math.min(dx, dy);
            if (_i2 - _i1 > d)
                _i2 = _i1 + d;
            else if (_i1 - _i2 > d) _i2 = _i1 - d;
            if (_j2 - _j1 > d)
                _j2 = _j1 + d;
            else if (_j1 - _j2 > d) _j2 = _j1 - d;
        }
        return _i2;
    }

    int calcLineCoordY(int _i1, int _j1, int _i2, int _j2) {
        int dx = Math.abs(_i2 - _i1);
        int dy = Math.abs(_j2 - _j1);
        if (2 * dy < dx) {
            _j2 = _j1;
        } else if (2 * dx < dy) {
            _i2 = _i1;
        } else {
            int d = Math.min(dx, dy);
            if (_i2 - _i1 > d)
                _i2 = _i1 + d;
            else if (_i1 - _i2 > d) _i2 = _i1 - d;
            if (_j2 - _j1 > d)
                _j2 = _j1 + d;
            else if (_j1 - _j2 > d) _j2 = _j1 - d;
        }
        return _j2;
    }

    void draftLinePreview() {
        if (!sbToolPoint.isSelected()) return;
        if (begin_i == end_i && begin_j == end_j) return;

        int ei = end_i;
        int ej = end_j;
        ei = calcLineCoordX(begin_i, begin_j, ei, ej);
        ej = calcLineCoordY(begin_i, begin_j, ei, ej);

        draft.linePreview(new Point(begin_i, begin_j), new Point(ei, ej));
    }

    void draftSelectPreview(boolean _draw, boolean _doit) {
        if (!sbToolSelect.isSelected() && !_doit) return;
        if (begin_i == end_i && begin_j == end_j) return;

        int i1 = Math.min(begin_i, end_i);
        int i2 = Math.max(begin_i, end_i);
        int j1 = Math.min(begin_j, end_j);
        int j2 = Math.max(begin_j, end_j);

        draft.selectPreview(_draw, new Point(i1, j1), new Point(i2, j2));
    }

    void draftSelectDraw() {
        if (!selection) return;
        begin_i = sel_i1;
        begin_j = sel_j1;
        end_i = sel_i2;
        end_j = sel_j2;
        draftSelectPreview(true, true);
    }

    void draftSelectClear() {
        if (!selection) return;
        begin_i = sel_i1;
        begin_j = sel_j1;
        end_i = sel_i2;
        end_j = sel_j2;
        draftSelectPreview(false, true);
        selection = false;
    }

    void draftMouseDown(MouseEvent event, int X, int Y) {
        if (dragging) return;
        Point pt = new Point(event.getX(), event.getY());
        if (event.getButton() == MouseEvent.BUTTON1 && draft.mouseToField(pt)) {
            draftSelectClear();
            dragging = true;
            begin_i = pt.getX();
            begin_j = pt.getY();
            end_i = pt.getX();
            end_j = pt.getY();
            // Prepress
            if (sbToolPoint.isSelected()) {
                draft.drawPrepress(new Point(begin_i, begin_j));
            }
            draftLinePreview();
            draftSelectPreview(true, false);
        }
    }

    void draftMouseMove(MouseEvent event) {
        Point pt = new Point(event.getX(), event.getY());
        if (dragging && draft.mouseToField(pt)) {
            draftSelectPreview(false, false);
            draftLinePreview();
            end_i = pt.getX();
            end_j = pt.getY();
            draftLinePreview();
            draftSelectPreview(true, false);
        }
    }

    void draftMouseUp(MouseEvent event) {
        Point pt = new Point(event.getX(), event.getY());
        if (dragging && draft.mouseToField(pt)) {
            draftLinePreview();
            end_i = pt.getX();
            end_j = pt.getY();
            dragging = false;

            if (sbToolPoint.isSelected()) {
                if (begin_i == end_i && begin_j == end_j) {
                    setPoint(begin_i, begin_j);
                } else {
                    end_i = calcLineCoordX(begin_i, begin_j, end_i, end_j);
                    end_j = calcLineCoordY(begin_i, begin_j, end_i, end_j);
                    if (Math.abs(end_i - begin_i) == Math.abs(end_j - begin_j)) {
                        // 45 grad Linie
                        undo.snapshot(field, modified);
                        int jj;
                        if (begin_i > end_i) {
                            int tmp = begin_i;
                            begin_i = end_i;
                            end_i = tmp;
                            tmp = begin_j;
                            begin_j = end_j;
                            end_j = tmp;
                        }
                        for (int i = begin_i; i <= end_i; i++) {
                            if (begin_j < end_j)
                                jj = begin_j + (i - begin_i);
                            else
                                jj = begin_j - (i - begin_i);
                            field.set(i, jj + scroll, colorIndex);
                            updateBead(i, jj);
                        }
                        repeatDirty = true;
                        modified = true;
                        updateTitle();
                    } else if (end_i == begin_i) {
                        // Senkrechte Linie
                        undo.snapshot(field, modified);
                        int j1 = Math.min(end_j, begin_j);
                        int j2 = Math.max(end_j, begin_j);
                        for (int jj = j1; jj <= j2; jj++) {
                            field.set(begin_i, jj + scroll, colorIndex);
                            updateBead(begin_i, jj);
                        }
                        modified = true;
                        repeatDirty = true;
                        updateTitle();
                    } else if (end_j == begin_j) {
                        // Waagrechte Linie ziehen
                        undo.snapshot(field, modified);
                        int i1 = Math.min(end_i, begin_i);
                        int i2 = Math.max(end_i, begin_i);
                        for (int i = i1; i <= i2; i++) {
                            field.set(i, begin_j + scroll, colorIndex);
                            updateBead(i, begin_j);
                        }
                        modified = true;
                        repeatDirty = true;
                        updateTitle();
                    }
                }
            } else if (sbToolFill.isSelected()) {
                undo.snapshot(field, modified);
                fillLine(end_i, end_j);
                modified = true;
                updateTitle();
                repeatDirty = true;
                report.invalidate();
            } else if (sbToolSniff.isSelected()) {
                colorIndex = field.get(begin_i, begin_j + scroll);
                assert (colorIndex >= 0 && colorIndex < 10);
                switch (colorIndex) {
                case 0:
                    sbColor0.setSelected(true);
                    break;
                case 1:
                    sbColor1.setSelected(true);
                    break;
                case 2:
                    sbColor2.setSelected(true);
                    break;
                case 3:
                    sbColor3.setSelected(true);
                    break;
                case 4:
                    sbColor4.setSelected(true);
                    break;
                case 5:
                    sbColor5.setSelected(true);
                    break;
                case 6:
                    sbColor6.setSelected(true);
                    break;
                case 7:
                    sbColor7.setSelected(true);
                    break;
                case 8:
                    sbColor8.setSelected(true);
                    break;
                case 9:
                    sbColor9.setSelected(true);
                    break;
                default:
                    assert (false);
                    break;
                }
            } else if (sbToolSelect.isSelected()) {
                draftSelectPreview(false, false);
                if (begin_i != end_i || begin_j != end_j) {
                    selection = true;
                    sel_i1 = begin_i;
                    sel_j1 = begin_j;
                    sel_i2 = end_i;
                    sel_j2 = end_j;
                    draftSelectDraw();
                }
            }
        }
    }

    void fillLine(int _i, int _j) {
        // xxx experimentell nach links und rechts
        byte bk = field.get(_i, _j + scroll);
        int i = _i;
        while (i >= 0 && field.get(i, _j + scroll) == bk) {
            field.set(i, _j + scroll, colorIndex);
            // TODO make draft an observer of field!
            updateBead(i, _j);
            i--;
        }
        i = begin_i + 1;
        while (i < field.getWidth() && field.get(i, _j + scroll) == bk) {
            field.set(i, _j + scroll, colorIndex);
            // TODO make draft an observer of field!
            updateBead(i, _j);
            i++;
        }
    }

    void setPoint(int _i, int _j) {
        undo.snapshot(field, modified);
        byte s = field.get(_i, _j + scroll);
        if (s == colorIndex) {
            field.set(_i, _j + scroll, (byte) 0);
        } else {
            field.set(_i, _j + scroll, colorIndex);
        }
        updateBead(_i, _j);
        modified = true;
        repeatDirty = true;
        updateTitle();
    }

    void editUndoClick() {
        undo.undo(field);
        modified = undo.isModified();
        updateTitle();
        invalidate();
        repeatDirty = true;
    }

    void editRedoClick() {
        undo.redo(field);
        modified = undo.isModified();
        updateTitle();
        invalidate();
        repeatDirty = true;
    }

    void viewZoominClick() {
        if (zoomIndex < 4) zoomIndex++;
        grid = zoomtable[zoomIndex];
        formResize();
        invalidate();
        updateScrollbar();
    }

    void viewZoomnormalClick() {
        if (zoomIndex == 1) return;
        zoomIndex = 2;
        grid = zoomtable[zoomIndex];
        formResize();
        invalidate();
        updateScrollbar();
    }

    void viewZoomoutClick() {
        if (zoomIndex > 0) zoomIndex--;
        grid = zoomtable[zoomIndex];
        formResize();
        invalidate();
        updateScrollbar();
    }

    void viewDraftClick() {
        viewDraft.setSelected(!viewDraft.isSelected());
        draft.setVisible(viewDraft.isSelected());
        laDraft.setVisible(draft.isVisible());
        formResize();
    }

    void viewNormalClick() {
        viewNormal.setSelected(!viewNormal.isSelected());
        normal.setVisible(viewNormal.isSelected());
        laNormal.setVisible(normal.isVisible());
        formResize();
    }

    void viewSimulationClick() {
        viewSimulation.setSelected(!viewSimulation.isSelected());
        simulation.setVisible(viewSimulation.isSelected());
        laSimulation.setVisible(simulation.isVisible());
        formResize();
    }

    void viewReportClick() {
        viewReport.setSelected(!viewReport.isSelected());
        report.setVisible(viewReport.isSelected());
        laReport.setVisible(report.isVisible());
        formResize();
    }

    void formKeyUp(KeyEvent event) {
        int Key = event.getKeyCode();
        if (Key == KeyEvent.VK_F5)
            invalidate();
        else if (Key == KeyEvent.VK_1 && event.isControlDown() && !event.isAltDown()) {
            sbToolPoint.setSelected(true);
            toolPoint.setSelected(true);
        } else if (Key == KeyEvent.VK_2 && event.isControlDown() && !event.isAltDown()) {
            sbToolSelect.setSelected(true);
            toolSelect.setSelected(true);
        } else if (Key == KeyEvent.VK_3 && event.isControlDown() && !event.isAltDown()) {
            sbToolFill.setSelected(true);
            toolFill.setSelected(true);
        } else if (Key == KeyEvent.VK_4 && event.isControlDown() && !event.isAltDown()) {
            sbToolSniff.setSelected(true);
            toolSniff.setSelected(true);
        } else if (event.getKeyChar() >= '0' && event.getKeyChar() <= '9') {
            colorIndex = (byte) (event.getKeyChar() - '0');
            switch (colorIndex) {
            case 0:
                sbColor0.setSelected(true);
                break;
            case 1:
                sbColor1.setSelected(true);
                break;
            case 2:
                sbColor2.setSelected(true);
                break;
            case 3:
                sbColor3.setSelected(true);
                break;
            case 4:
                sbColor4.setSelected(true);
                break;
            case 5:
                sbColor5.setSelected(true);
                break;
            case 6:
                sbColor6.setSelected(true);
                break;
            case 7:
                sbColor7.setSelected(true);
                break;
            case 8:
                sbColor8.setSelected(true);
                break;
            case 9:
                sbColor9.setSelected(true);
                break;
            default:
                assert (false);
                break;
            }
        } else if (Key == KeyEvent.VK_SPACE) {
            sbToolPoint.setSelected(true);
            toolPoint.setSelected(true);
        } else if (Key == KeyEvent.VK_ESCAPE) {
            // righttimer.Enabled = false;
            // lefttimer.Enabled = false;
        }
    }

    void rotateLeft() {
        shift = (shift - 1 + field.getWidth()) % field.getWidth();
        modified = true;
        updateTitle();
        simulation.invalidate();
    }

    void rotateRight() {
        shift = (shift + 1) % field.getWidth();
        modified = true;
        updateTitle();
        simulation.invalidate();
    }

    // TODO split this for every color toolbar button
    void colorClick(ActionEvent event) {
        Object Sender = event.getSource();
        if (Sender == sbColor0)
            colorIndex = 0;
        else if (Sender == sbColor1)
            colorIndex = 1;
        else if (Sender == sbColor2)
            colorIndex = 2;
        else if (Sender == sbColor3)
            colorIndex = 3;
        else if (Sender == sbColor4)
            colorIndex = 4;
        else if (Sender == sbColor5)
            colorIndex = 5;
        else if (Sender == sbColor6)
            colorIndex = 6;
        else if (Sender == sbColor7)
            colorIndex = 7;
        else if (Sender == sbColor8)
            colorIndex = 8;
        else if (Sender == sbColor9) colorIndex = 9;
    }

    // TODO split this for every color toolbar button
    void colorDblClick(ActionEvent event) {
        Object Sender = event.getSource();
        int c = 0;
        if (Sender == sbColor0)
            c = 0;
        else if (Sender == sbColor1)
            c = 1;
        else if (Sender == sbColor2)
            c = 2;
        else if (Sender == sbColor3)
            c = 3;
        else if (Sender == sbColor4)
            c = 4;
        else if (Sender == sbColor5)
            c = 5;
        else if (Sender == sbColor6)
            c = 6;
        else if (Sender == sbColor7)
            c = 7;
        else if (Sender == sbColor8)
            c = 8;
        else if (Sender == sbColor9) c = 9;
        if (c == 0) return;
        Color color = JColorChooser.showDialog(this, "choose color", colors[c]);
        if (color == null) return;
        undo.snapshot(field, modified);
        colors[c] = color;
        // TODO propagate change to all dependants (or better use observer
        // pattern)
        modified = true;
        updateTitle();
        invalidate();
        setGlyphColors();
    }

    // TODO handle out parameter
    void scrollbarScroll(AdjustmentEvent event) {
        int oldscroll = scroll;
        // if (ScrollPos > scrollbar.Max - scrollbar.PageSize) ScrollPos =
        // scrollbar.Max - scrollbar.PageSize;
        scroll = scrollbar.getMaximum() - scrollbar.getBlockIncrement() - scrollbar.getValue();
        if (oldscroll != scroll) invalidate();
    }

    void idleHandler() {
        // Menü- und Toolbar enablen/disablen
        editCopy.setEnabled(selection);
        sbCopy.setEnabled(selection);
        editUndo.setEnabled(undo.canUndo());
        editRedo.setEnabled(undo.canRedo());
        sbUndo.setEnabled(undo.canUndo());
        sbRedo.setEnabled(undo.canRedo());

        // FIXME is this whole rapport stuff needed? all drawing code was
        // commented out and thus removed...

        // Rapport berechnen und zeichnen
        if (repeatDirty) {

            // Musterrapport neu berechnen
            int last = -1;
            for (int j = 0; j < field.getHeight(); j++) {
                for (int i = 0; i < field.getWidth(); i++) {
                    int c = field.get(i, j);
                    if (c > 0) {
                        last = j;
                        break;
                    }
                }
            }
            if (last == -1) {
                repeat = 0;
                colorRepeat = 0;
                repeatDirty = false;
                report.invalidate();
                return;
            }
            repeat = last + 1;
            for (int j = 1; j <= last; j++) {
                if (equalRows(0, j)) {
                    boolean ok = true;
                    for (int k = j + 1; k <= last; k++) {
                        if (!equalRows((k - j) % j, k)) {
                            ok = false;
                            break;
                        }
                    }
                    if (ok) {
                        repeat = j;
                        break;
                    }
                }
            }

            // Farbrapport neu berechnen
            colorRepeat = repeat * field.getWidth();
            for (int i = 1; i <= repeat * field.getWidth(); i++) {
                if (field.get(i) == field.get(0)) {
                    boolean ok = true;
                    for (int k = i + 1; k <= repeat * field.getWidth(); k++) {
                        if (field.get((k - i) % i) != field.get(k)) {
                            ok = false;
                            break;
                        }
                    }
                    if (ok) {
                        colorRepeat = i;
                        break;
                    }
                }
            }

            report.invalidate();
            repeatDirty = false;
        }

        // Vorsorgliches Undo
        undo.prepareSnapshot(field, modified);
    }

    boolean equalRows(int j, int k) {
        for (int i = 0; i < field.getWidth(); i++) {
            if (field.get(i, j) != field.get(i, k)) return false;
        }
        return true;
    }

    void toolPointClick() {
        toolPoint.setSelected(true);
        sbToolPoint.setSelected(true);
        draftSelectClear();
    }

    void toolSelectClick() {
        toolSelect.setSelected(true);
        sbToolSelect.setSelected(true);
    }

    void toolFillClick() {
        toolFill.setSelected(true);
        sbToolFill.setSelected(true);
        draftSelectClear();
    }

    void toolSniffClick() {
        toolSniff.setSelected(true);
        sbToolSniff.setSelected(true);
        draftSelectClear();
    }

    void sbToolPointClick() {
        toolPoint.setSelected(true);
        draftSelectClear();
    }

    void sbToolFillClick() {
        toolFill.setSelected(true);
        draftSelectClear();
    }

    void sbToolSniffClick() {
        toolSniff.setSelected(true);
        draftSelectClear();
    }

    void sbToolSelectClick() {
        toolSelect.setSelected(true);
    }

    void normalMouseUp(MouseEvent event) {
        // TODO move this to the NormalPanel
        Point pt = new Point(event.getX(), event.getY());
        if (event.getButton() == MouseEvent.BUTTON1 && normal.mouseToField(pt)) {
            // Lineare Koordinaten berechnen
            int idx = 0;
            int m1 = field.getWidth();
            int m2 = m1 + 1;
            for (int j = 0; j < pt.getY() + scroll; j++) {
                if (j % 2 == 0)
                    idx += m1;
                else
                    idx += m2;
            }
            idx += pt.getX();

            // Feld setzen und Darstellung nachf�hren
            int j = idx / field.getWidth();
            int i = idx % field.getWidth();
            setPoint(i, j - scroll);
        }
    }

    void infoAboutClick() {
        new AboutBox().setVisible(true);
    }

    void lefttimerTimer() {
        rotateLeft();
        // Application.ProcessMessages(); // FIXME maybe just remove it?
    }

    void righttimerTimer() {
        rotateRight();
        // Application.ProcessMessages(); // FIXME maybe just remove it?
    }

    void sbRotaterightMouseDown(MouseEvent event) {
        rotateRight();
        // Application.ProcessMessages();
        // righttimer.Enabled = true;
    }

    void sbRotaterightMouseUp(MouseEvent event) {
        // righttimer.Enabled = false;
    }

    void sbRotateleftMouseDown(MouseEvent event) {
        rotateLeft();
        // Application.ProcessMessages();
        // lefttimer.Enabled = true;
    }

    void sbRotateleftMouseUp(MouseEvent event) {
        // lefttimer.Enabled = false;
    }

    void formKeyDown(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
            rotateRight();
        } else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
            rotateLeft();
        }
    }

    boolean formCloseQuery() {
        if (modified) {
            int r = JOptionPane.showConfirmDialog(this,
                    Texts.text("Do you want to save your changes?", "Sollen die �nderungen gespeichert werden?"));
            if (r == JOptionPane.CANCEL_OPTION) {
                return false;
            }
            if (r == JOptionPane.OK_OPTION) fileSaveClick();
        }
        return true;
    }

    void editCopyClick() {
        CopyForm copyform = new CopyForm();
        copyform.setVisible(true);
        if (copyform.isOK()) {
            undo.snapshot(field, modified);
            // Aktuelle Daten in Buffer kopieren
            sel_buff.copyFrom(field);
            // Daten vervielf�ltigen
            if (sel_i1 > sel_i2) {
                int temp = sel_i1;
                sel_i1 = sel_i2;
                sel_i2 = temp;
            }
            if (sel_j1 > sel_j2) {
                int temp = sel_j1;
                sel_j1 = sel_j2;
                sel_j2 = temp;
            }
            for (int i = sel_i1; i <= sel_i2; i++) {
                for (int j = sel_j1; j <= sel_j2; j++) {
                    byte c = sel_buff.get(i, j);
                    if (c == 0) continue;
                    int idx = getIndex(i, j);
                    // Diesen Punkt x-mal vervielf�ltigen
                    for (int k = 0; k < copyform.getCopies(); k++) {
                        idx += getCopyOffset(copyform);
                        if (field.isValidIndex(idx)) field.set(idx, c);
                    }
                }
            }
            repeatDirty = true;
            modified = true;
            updateTitle();
            invalidate();
        }
    }

    int getCopyOffset(CopyForm form) {
        return form.getVertOffset() * field.getWidth() + form.getHorzOffset();
    }

    int getIndex(int i, int j) {
        return j * field.getWidth() + i;
    }

    void editInsertlineClick() {
        undo.snapshot(field, modified);
        field.insertLine();
        repeatDirty = true;
        modified = true;
        updateTitle();
        invalidate();
    }

    void editDeletelineClick() {
        undo.snapshot(field, modified);
        field.deleteLine();
        repeatDirty = true;
        modified = true;
        updateTitle();
        invalidate();
    }

    void setAppTitle() {
        updateTitle();
    }

    void updateTitle() {
        String c = "jbead"; // APP_TITLE;
        c += " - ";
        if (saved) {
            c += file.getName();
        } else {
            c += "unnamed"; // DATEI_UNBENANNT;
        }
        if (modified) {
            c += "*";
        }
        setTitle(c);
    }

    void languageEnglishClick() {
        Texts.setLanguage(Language.EN, this);
        languageEnglish.setSelected(true);
        Settings settings = new Settings();
        settings.SetCategory("Environment");
        settings.SaveInt("Language", 0);
    }

    void languageGermanClick() {
        Texts.setLanguage(Language.GE, this);
        languageGerman.setSelected(true);
        Settings settings = new Settings();
        settings.SetCategory("Environment");
        settings.SaveInt("Language", 1);
    }

    int mm2px(int x, int sx) {
        return x * sx / 254;
    }

    int mm2py(int y, int sy) {
        return y * sy / 254;
    }

    void printAll(Graphics g, PageFormat pageFormat, int pageIndex) {
//        String title = "jbead"; // APP_TITLE;
//        title += " - " + savedialog.getSelectedFile().getName();
        // TODO print headers and footers?

        int sx = 72; // 72 dpi
        int sy = 72; // 72 dpi

        int gx = (15 + zoomIndex * 5) * sx / 254;
        int gy = (15 + zoomIndex * 5) * sy / 254;

        int draftleft = 0;
        int normalleft = 0;
        int simulationleft = 0;
        int reportleft = 0;
        int reportcols = 0;

        int m = mm2px(10, sx);
        if (draft.isVisible()) {
            draftleft = m;
            m += mm2px(13, sx) + field.getWidth() * gx + mm2px(7, sx);
        }

        if (normal.isVisible()) {
            normalleft = m;
            m += mm2px(7, sx) + (field.getWidth() + 1) * gx;
        }

        if (simulation.isVisible()) {
            simulationleft = m;
            m += mm2px(7, sx) + (field.getWidth() / 2 + 1) * gx;
        }

        if (report.isVisible()) {
            reportleft = m;
            reportcols = ((int) pageFormat.getWidth() - m - 10) / (mm2px(5, sx) + mm2px(8, sx));
        }

        int h = (int) pageFormat.getHeight() - mm2py(10, sy);

        // //////////////////////////////////////
        //
        // Draft
        //
        // //////////////////////////////////////

        // Grid
        g.setColor(Color.BLACK);
        int left = draftleft + mm2px(13, sx);
        if (left < 0) left = 0;
        int maxj = Math.min(field.getHeight(), (h - mm2py(10, sy)) / gy);
        for (int i = 0; i < field.getWidth() + 1; i++) {
            g.drawLine(left + i * gx, h - (maxj) * gy, left + i * gx, h - 1);
        }
        for (int j = 0; j <= maxj; j++) {
            g.drawLine(left, h - 1 - j * gy, left + field.getWidth() * gx, h - 1 - j * gy);
        }

        // Daten
        for (int i = 0; i < field.getWidth(); i++) {
            for (int j = 0; j < maxj; j++) {
                byte c = field.get(i, j);
                assert (c >= 0 && c <= 9);
                if (c > 0) {
                    g.setColor(colors[c]);
                    g.fillRect(left + i * gx + 1, h - (j + 1) * gy, gx, gy);
                }
            }
        }

        // Zehnermarkierungen
        g.setColor(Color.BLACK);
        for (int j = 0; j < maxj; j++) {
            if ((j % 10) == 0) {
                g.drawLine(draftleft, h - j * gy - 1, left - mm2px(3, sx), h - j * gy - 1);
                g.drawString(Integer.toString(j), draftleft, h - j * gy + mm2py(1, sy));
            }
        }

        // //////////////////////////////////////
        //
        // Korrigiert (normal)
        //
        // //////////////////////////////////////

        // Grid
        g.setColor(Color.BLACK);
        left = normalleft + gx / 2;
        if (left < 0) left = gx / 2;
        maxj = Math.min(field.getHeight(), (h - mm2py(10, sy)) / gy);
        for (int i = 0; i < field.getWidth() + 1; i++) {
            for (int jj = 0; jj < maxj; jj += 2) {
                g.drawLine(left + i * gx, h - (jj + 1) * gy, left + i * gx, h - jj * gy);
            }
        }
        for (int i = 0; i <= field.getWidth() + 1; i++) {
            for (int jj = 1; jj < maxj; jj += 2) {
                g.drawLine(left + i * gx - gx / 2, h - (jj + 1) * gy, left + i * gx - gx / 2, h - jj * gy);
            }
        }
        g.drawLine(left, h - 1, left + field.getWidth() * gx + 1, h - 1);
        for (int jj = 1; jj <= maxj; jj++) {
            g.drawLine(left - gx / 2, h - 1 - jj * gy, left + field.getWidth() * gx + gx / 2 + 1, h - 1 - jj * gy);
        }

        // Daten
        for (int i = 0; i < field.getWidth(); i++) {
            for (int jj = 0; jj < maxj; jj++) {
                byte c = field.get(i, jj + scroll);
                assert (c >= 0 && c <= 9);
                if (c == 0) continue;
                g.setColor(colors[c]);
                int ii = i;
                int j1 = jj;
                ii = correctCoordinatesX(ii, j1);
                j1 = correctCoordinatesY(ii, j1);
                if (j1 % 2 == 0) {
                    g.fillRect(left + ii * gx + 1, h - (j1 + 1) * gy, gx, gy);
                } else {
                    g.fillRect(left - gx / 2 + ii * gx + 1, h - (j1 + 1) * gy, gx, gy);
                }
            }
        }

        // //////////////////////////////////////
        //
        // Simulation
        //
        // //////////////////////////////////////

        // Grid
        g.setColor(Color.BLACK);
        left = simulationleft + gx / 2;
        if (left < 0) left = gx / 2;
        maxj = Math.min(field.getHeight(), (h - mm2py(10, sy)) / gy);
        int w = field.getWidth() / 2;
        for (int j = 0; j < maxj; j += 2) {
            for (int i = 0; i < w + 1; i++) {
                g.drawLine(left + i * gx, h - (j + 1) * gy, left + i * gx, h - j * gy);
            }
            if (j > 0 || scroll > 0) {
                g.drawLine(left - gx / 2, h - (j + 1) * gy, left - gx / 2, h - j * gy);
            }
        }
        for (int j = 1; j < maxj; j += 2) {
            for (int i = 0; i < w + 1; i++) {
                g.drawLine(left + i * gx - gx / 2, h - (j + 1) * gy, left + i * gx - gx / 2, h - j * gy);
            }
            g.drawLine(left + w * gx, h - (j + 1) * gy, left + w * gx, h - j * gy);
        }
        g.drawLine(left, h - 1, left + w * gx + 1, h - 1);
        for (int j = 1; j <= maxj; j++) {
            g.drawLine(left - gx / 2, h - 1 - j * gy, left + w * gx + 1, h - 1 - j * gy);
        }

        // Daten
        for (int i = 0; i < field.getWidth(); i++) {
            for (int j = 0; j < maxj; j++) {
                byte c = field.get(i, j + scroll);
                assert (c >= 0 && c <= 9);
                if (c == 0) continue;
                g.setColor(colors[c]);
                int ii = i;
                int jj = j;
                ii = correctCoordinatesX(ii, jj);
                jj = correctCoordinatesY(ii, jj);
                if (ii > w && ii != field.getWidth()) continue;
                if (jj % 2 == 0) {
                    if (ii == w) continue;
                    g.fillRect(left + ii * gx + 1, h - (jj + 1) * gy, gx, gy);
                } else {
                    if (ii != field.getWidth() && ii != w) {
                        g.fillRect(left - gx / 2 + ii * gx + 1, h - (jj + 1) * gy, gx, gy);
                    } else if (ii == w) {
                        g.fillRect(left - gx / 2 + ii * gx + 1, h - (jj + 1) * gy, gx / 2, gy);
                    } else {
                        g.fillRect(left - gx / 2 + 1, h - (jj + 2) * gy, gx / 2, gy);
                    }
                }
            }
        }

        // //////////////////////////////////////
        //
        // Auswertung
        //
        // //////////////////////////////////////

        int x1 = reportleft;
        int x2 = reportleft + mm2px(30, sx);
        int y = mm2py(10, sy);
        int dy = mm2py(5, sy);
        int dx = mm2px(5, sx);

        // Mustername
        g.setColor(Color.BLACK);
        g.drawString(Texts.text("Pattern:", "Muster:"), x1, y);
        g.drawString(file.getName(), x2, y);
        y += dy;
        // Umfang
        g.drawString(Texts.text("Circumference:", "Umfang:"), x1, y);
        g.drawString(Integer.toString(field.getWidth()), x2, y);
        y += dy;
        // Farbrapport
        g.drawString(Texts.text("Repeat of colors:", "Farbrapport:"), x1, y);
        g.drawString(Integer.toString(colorRepeat) + Texts.text(" beads", " Perlen"), x2, y);
        y += dy;
        // Faedelliste...
        if (colorRepeat > 0) {
            int page = 1;
            int column = 0;
            g.drawString(Texts.text("List of beads", "Fädelliste"), x1, y);
            y += dy;
            int ystart = y;
            byte col = field.get(colorRepeat - 1);
            int count = 1;
            for (int i = colorRepeat - 2; i >= 0; i--) {
                if (field.get(i) == col) {
                    count++;
                } else {
                    if (col != 0) {
                        g.setColor(colors[col]);
                        g.fillRect(x1, y, dx - mm2px(1, sx), dy - mm2py(1, sy));
                        g.setColor(Color.WHITE);
                        g.drawRect(x1, y, dx - mm2px(1, sx), dy - mm2py(1, sy));
                    } else {
                        g.setColor(Color.WHITE);
                        g.fillRect(x1, y, dx - mm2px(1, sx), dy - mm2py(1, sy));
                        g.setColor(Color.BLACK);
                        g.drawRect(x1, y, dx - mm2px(1, sx), dy - mm2py(1, sy));
                    }
                    g.setColor(Color.BLACK);
                    g.drawString(Integer.toString(count), x1 + dx + 3, y);
                    y += dy;
                    col = field.get(i);
                    count = 1;
                }
                if (y >= (int) pageFormat.getHeight() - mm2py(10, sy)) {
                    x1 += dx + mm2px(8, sx);
                    y = ystart;
                    column++;
                    if (column >= reportcols) { // neue Seite und weiter...
                        // TODO handle multipage output, sigh...
                        break;
                        // Printer().NewPage();
                        // x1 = draftleft;
                        // x2 = draftleft + MM2PRx(30, sx);
                        // y = MM2PRy(10, sy);
                        // reportcols = (Printer().PageWidth - draftleft - 10) /
                        // (MM2PRx(5, sx) + MM2PRx(8, sx));
                        // column = 0;
                        // page++;
                        // canvas.Pen.Color = clBlack;
                        // canvas.TextOut (x1, y,
                        // String(Language.STR("Pattern ",
                        // "Muster "))+savedialog.getSelectedFile().getName() +
                        // " - " + Language.STR("page ", "Seite ") +
                        // IntToStr(page));
                        // y += dy;
                        // ystart = y;
                    }
                }
            }
            if (y < (int) pageFormat.getHeight() - mm2py(10, sy)) {
                if (col != 0) {
                    g.setColor(colors[col]);
                    g.fillRect(x1, y, dx - mm2px(1, sx), dy - mm2py(1, sy));
                    g.setColor(Color.WHITE);
                    g.drawRect(x1, y, dx - mm2px(1, sx), dy - mm2py(1, sy));
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x1, y, dx - mm2px(1, sx), dy - mm2py(1, sy));
                    g.setColor(Color.BLACK);
                    g.drawRect(x1, y, dx - mm2px(1, sx), dy - mm2py(1, sy));
                }
                g.setColor(Color.BLACK);
                g.drawString(Integer.toString(count), x1 + dx + 3, y);
            }
        }
    }

    void reloadLanguage() {
        // Menüs
        // Menu Datei
        Texts.update(menuFile, Language.EN, "&File");
        Texts.update(menuFile, Language.GE, "&Datei");
        Texts.update(fileNew, Language.EN, "&New", "Creates a new pattern");
        Texts.update(fileNew, Language.GE, "&Neu", "Erstellt ein neues Muster");
        Texts.update(fileOpen, Language.EN, "&Open...", "Opens a pattern");
        Texts.update(fileOpen, Language.GE, "�&ffnen...", "�ffnet ein Muster");
        Texts.update(fileSave, Language.EN, "&Save", "Saves the pattern");
        Texts.update(fileSave, Language.GE, "&Speichern", "Speichert das Muster");
        Texts.update(fileSaveas, Language.EN, "Save &as...", "Saves the pattern to a new file");
        Texts.update(fileSaveas, Language.GE, "Speichern &unter...", "Speichert das Muster unter einem neuen Namen");
        Texts.update(filePrint, Language.EN, "&Print...", "Prints the pattern");
        Texts.update(filePrint, Language.GE, "&Drucken...", "Druckt das Muster");
        Texts.update(filePrintersetup, Language.EN, "Printer set&up...", "Configures the printer");
        Texts.update(filePrintersetup, Language.GE, "D&ruckereinstellung...", "Konfiguriert den Drucker");
        Texts.update(fileExit, Language.EN, "E&xit", "Exits the program");
        Texts.update(fileExit, Language.GE, "&Beenden", "Beendet das Programm");

        // Menu Bearbeiten
        Texts.update(menuEdit, Language.EN, "&Edit", "");
        Texts.update(menuEdit, Language.GE, "&Bearbeiten", "");
        Texts.update(editUndo, Language.EN, "&Undo", "Undoes the last action");
        Texts.update(editUndo, Language.GE, "&R�ckg�ngig", "Macht die letzte �nderung r�ckg�ngig");
        Texts.update(editRedo, Language.EN, "&Redo", "Redoes the last undone action");
        Texts.update(editRedo, Language.GE, "&Wiederholen", "F�hrt die letzte r�ckg�ngig gemachte �nderung durch");
        Texts.update(editCopy, Language.EN, "&Arrange", "");
        Texts.update(editCopy, Language.GE, "&Anordnen", "");
        Texts.update(editLine, Language.EN, "&Empty Line", "");
        Texts.update(editLine, Language.GE, "&Leerzeile", "");
        Texts.update(editInsertline, Language.EN, "&Insert", "");
        Texts.update(editInsertline, Language.GE, "&Einf�gen", "");
        Texts.update(editDeleteline, Language.EN, "&Delete", "");
        Texts.update(editDeleteline, Language.GE, "E&ntfernen", "");

        // Menu Werkzeug
        Texts.update(toolTool, Language.EN, "&Tool", "");
        Texts.update(toolTool, Language.GE, "&Werkzeug", "");
        Texts.update(toolPoint, Language.EN, "&Pencil", "");
        Texts.update(toolPoint, Language.GE, "&Eingabe", "");
        Texts.update(toolSelect, Language.EN, "&Select", "");
        Texts.update(toolSelect, Language.GE, "&Auswahl", "");
        Texts.update(toolFill, Language.EN, "&Fill", "");
        Texts.update(toolFill, Language.GE, "&F�llen", "");
        Texts.update(toolSniff, Language.EN, "P&ipette", "");
        Texts.update(toolSniff, Language.GE, "&Pipette", "");

        // Menu Ansicht
        Texts.update(menuView, Language.EN, "&View", "");
        Texts.update(menuView, Language.GE, "&Ansicht", "");
        Texts.update(viewDraft, Language.EN, "&Design", "");
        Texts.update(viewDraft, Language.GE, "&Entwurf", "");
        Texts.update(viewNormal, Language.EN, "&Corrected", "");
        Texts.update(viewNormal, Language.GE, "&Korrigiert", "");
        Texts.update(viewSimulation, Language.EN, "&Simulation", "");
        Texts.update(viewSimulation, Language.GE, "&Simulation", "");
        Texts.update(viewReport, Language.EN, "&Report", "");
        Texts.update(viewReport, Language.GE, "&Auswertung", "");
        Texts.update(viewZoomin, Language.EN, "&Zoom in", "Zoom in");
        Texts.update(viewZoomin, Language.GE, "&Vergr�ssern", "Vergr�ssert die Ansicht");
        Texts.update(viewZoomnormal, Language.EN, "&Normal", "Sets magnification to default value");
        Texts.update(viewZoomnormal, Language.GE, "&Normal", "Stellt die Standardgr�sse ein");
        Texts.update(viewZoomout, Language.EN, "Zoo&m out", "Zoom out");
        Texts.update(viewZoomout, Language.GE, "Ver&kleinern", "Verkleinert die Ansicht");
        Texts.update(viewLanguage, Language.EN, "&Language", "");
        Texts.update(viewLanguage, Language.GE, "&Sprache", "");
        Texts.update(languageEnglish, Language.EN, "&English", "");
        Texts.update(languageEnglish, Language.GE, "&Englisch", "");
        Texts.update(languageGerman, Language.EN, "&German", "");
        Texts.update(languageGerman, Language.GE, "&Deutsch", "");

        // Menu Muster
        Texts.update(menuPattern, Language.EN, "&Pattern", "");
        Texts.update(menuPattern, Language.GE, "&Muster", "");
        Texts.update(patternWidth, Language.EN, "&Width...", "");
        Texts.update(patternWidth, Language.GE, "&Breite...", "");

        // Menu ?
        Texts.update(menuInfo, Language.EN, "&?", "");
        Texts.update(menuInfo, Language.GE, "&?", "");
        Texts.update(infoAbout, Language.EN, "About &DB-BEAD...", "Displays information about DB-BEAD");
        Texts.update(infoAbout, Language.GE, "�ber &DB-BEAD...", "Zeigt Informationen �ber DB-BEAD an");

        // Toolbar
        Texts.update(sbNew, Language.EN, "", "New|Creates a new pattern");
        Texts.update(sbNew, Language.GE, "", "Neu|Erstellt ein neues Muster");
        Texts.update(sbOpen, Language.EN, "", "Open|Opens a pattern");
        Texts.update(sbOpen, Language.GE, "", "�ffnen|�ffnet ein Muster");
        Texts.update(sbSave, Language.EN, "", "Save|Saves the pattern");
        Texts.update(sbSave, Language.GE, "", "Speichern|Speichert das Muster");
        Texts.update(sbPrint, Language.EN, "", "Print|Prints the pattern");
        Texts.update(sbPrint, Language.GE, "", "Drucken|Druckt das Muster");
        Texts.update(sbUndo, Language.EN, "", "Undo|Undoes the last change");
        Texts.update(sbUndo, Language.GE, "", "R�ckg�ngig|Macht die letzte �nderung r�ckg�ngig");
        Texts.update(sbRedo, Language.EN, "", "Redo|Redoes the last undone change");
        Texts.update(sbRedo, Language.GE, "", "Wiederholen|Macht die letzte r�ckg�ngig gemachte �nderung");
        Texts.update(sbRotateleft, Language.EN, "", "Left|Rotates the pattern left");
        Texts.update(sbRotateleft, Language.GE, "", "Links|Rotiert das Muster nach links");
        Texts.update(sbRotateright, Language.EN, "", "Right|Rotates the pattern right");
        Texts.update(sbRotateright, Language.GE, "", "Rechts|Rotiert das Muster nach rechts");
        Texts.update(sbCopy, Language.EN, "", "Arrange");
        Texts.update(sbCopy, Language.GE, "", "Anordnen");
        Texts.update(sbColor0, Language.EN, "", "Color 0");
        Texts.update(sbColor0, Language.GE, "", "Farbe 0");
        Texts.update(sbColor1, Language.EN, "", "Color 1");
        Texts.update(sbColor1, Language.GE, "", "Farbe 1");
        Texts.update(sbColor2, Language.EN, "", "Color 2");
        Texts.update(sbColor2, Language.GE, "", "Farbe 2");
        Texts.update(sbColor3, Language.EN, "", "Color 3");
        Texts.update(sbColor3, Language.GE, "", "Farbe 3");
        Texts.update(sbColor4, Language.EN, "", "Color 4");
        Texts.update(sbColor4, Language.GE, "", "Farbe 4");
        Texts.update(sbColor5, Language.EN, "", "Color 5");
        Texts.update(sbColor5, Language.GE, "", "Farbe 5");
        Texts.update(sbColor6, Language.EN, "", "Color 6");
        Texts.update(sbColor6, Language.GE, "", "Farbe 6");
        Texts.update(sbColor7, Language.EN, "", "Color 7");
        Texts.update(sbColor7, Language.GE, "", "Farbe 7");
        Texts.update(sbColor8, Language.EN, "", "Color 8");
        Texts.update(sbColor8, Language.GE, "", "Farbe 8");
        Texts.update(sbColor9, Language.EN, "", "Color 9");
        Texts.update(sbColor9, Language.GE, "", "Farbe 9");
        Texts.update(sbToolSelect, Language.EN, "", "Select");
        Texts.update(sbToolSelect, Language.GE, "", "Auswahl");
        Texts.update(sbToolPoint, Language.EN, "", "Pencil");
        Texts.update(sbToolPoint, Language.GE, "", "Eingabe");
        Texts.update(sbToolFill, Language.EN, "", "Fill");
        Texts.update(sbToolFill, Language.GE, "", "F�llen");
        Texts.update(sbToolSniff, Language.EN, "", "Pipette");
        Texts.update(sbToolSniff, Language.GE, "", "Pipette");

        Texts.update(laDraft, Language.EN, "Draft");
        Texts.update(laDraft, Language.GE, "Entwurf");
        Texts.update(laNormal, Language.EN, "Corrected");
        Texts.update(laNormal, Language.GE, "Korrigiert");
        Texts.update(laSimulation, Language.EN, "Simulation");
        Texts.update(laSimulation, Language.GE, "Simulation");
        Texts.update(laReport, Language.EN, "Report");
        Texts.update(laReport, Language.GE, "Auswertung");

        invalidate();
    }

    void addToMRU(File file) {
        if (file.getPath() == "") return;

        // Wenn Datei schon in MRU: Eintrag nach oben schieben
        for (int i = 0; i < 6; i++) {
            if (mru[i] == file.getPath()) {
                if (i > 0) {
                    String temp = mru[i];
                    for (int j = i; j > 0; j--)
                        mru[j] = mru[j - 1];
                    mru[0] = temp;
                }
                updateMRU();
                saveMRU();
                return;
            }
        }

        // Ansonsten wird alles um einen Platz nach unten
        // geschoben und der Dateiname im ersten Eintrag
        // vermerkt.
        for (int i = 5; i > 0; i--)
            mru[i] = mru[i - 1];
        mru[0] = file.getPath();

        updateMRU();
        saveMRU();
    }

    void updateMRU() {
        updateMRUMenu(1, fileMRU1, mru[0]);
        updateMRUMenu(2, fileMRU2, mru[1]);
        updateMRUMenu(3, fileMRU3, mru[2]);
        updateMRUMenu(4, fileMRU4, mru[3]);
        updateMRUMenu(5, fileMRU5, mru[4]);
        updateMRUMenu(6, fileMRU6, mru[5]);
        fileMRUSeparator.setVisible(fileMRU1.isVisible() || fileMRU2.isVisible() || fileMRU3.isVisible() || fileMRU4.isVisible()
                || fileMRU5.isVisible() || fileMRU6.isVisible());
    }

    void updateMRUMenu(int index, JMenuItem menuitem, String filename) {
        menuitem.setVisible(filename != "");
        // xxx Eigene Dateien oder so?!
        // Bestimmen ob Datei im Daten-Verzeichnis ist, falls
        // nicht, ganzen Pfad anzeigen!
        String path = filename;
        String datapath = System.getProperty("user.dir");
        if (path == datapath) {
            menuitem.setText(new File(filename).getName());
        } else {
            menuitem.setText(filename);
        }
        menuitem.setAccelerator(KeyStroke.getKeyStroke(Integer.toString(index)));
    }

    void fileMRU1Click() {
        loadFile(new File(mru[0]), true);
    }

    void fileMRU2Click() {
        loadFile(new File(mru[1]), true);
    }

    void fileMRU3Click() {
        loadFile(new File(mru[2]), true);
    }

    void fileMRU4Click() {
        loadFile(new File(mru[3]), true);
    }

    void fileMRU5Click() {
        loadFile(new File(mru[4]), true);
    }

    void fileMRU6Click() {
        loadFile(new File(mru[5]), true);
    }

    void saveMRU() {
        Settings settings = new Settings();
        settings.SetCategory("mru");
        settings.SaveString("mru0", mru[0]);
        settings.SaveString("mru1", mru[1]);
        settings.SaveString("mru2", mru[2]);
        settings.SaveString("mru3", mru[3]);
        settings.SaveString("mru4", mru[4]);
        settings.SaveString("mru5", mru[5]);
    }

    void loadMRU() {
        Settings settings = new Settings();
        settings.SetCategory("mru");
        mru[0] = settings.LoadString("mru0");
        mru[1] = settings.LoadString("mru1");
        mru[2] = settings.LoadString("mru2");
        mru[3] = settings.LoadString("mru3");
        mru[4] = settings.LoadString("mru4");
        mru[5] = settings.LoadString("mru5");
    }

}