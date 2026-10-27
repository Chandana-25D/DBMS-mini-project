/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package event_registration_system;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Chandana D
 */
public class Registration_form extends JFrame{
     private JTextField usnField, nameField, emailField, dateField, searchField, idSearchField;
    private JComboBox<String> courseBox, semesterBox, eventBox;
    private JTable table;
    private DefaultTableModel model;
    private JLabel countLabel;
    private JScrollPane pageScroll;
    private JPanel formCardRef, tableCardRef, searchCardRef;
    private RoundButton registrationNav, participantsNav, searchNav, eventsNav;

    private static final String URL = "jdbc:mysql://localhost:3306/event_db";
    private static final String USER = "root";
    // Keep the password that works on your computer.
    private static final String PASSWORD = "12345678";

    // ===== COLOURS =====
    private static final Color BG_TOP = new Color(251, 249, 245);
    private static final Color BG_BOTTOM = new Color(226, 226, 246);
    private static final Color NAVY = new Color(17, 37, 78);
    private static final Color NAVY_LIGHT = new Color(35, 61, 112);
    private static final Color PURPLE = new Color(92, 101, 178);
    private static final Color LAVENDER = new Color(228, 227, 247);
    private static final Color GOLD = new Color(190, 145, 65);
    private static final Color FIELD = Color.WHITE;
    private static final Color LINE = new Color(205, 207, 224);
    private static final Color TEXT = new Color(25, 39, 73);
    private static final Color MUTED = new Color(150, 155, 175);
    private static final Color SIDE_TEXT = new Color(236, 237, 246);

    // ===== FONTS =====
    // Change HEADING_FONT / BODY_FONT to restyle the whole app (any font installed on the PC).
    private static final String HEADING_FONT = "Times New Roman";
    private static final String BODY_FONT = "Times New Roman";
    private static Font head(int style, int size) { return new Font(HEADING_FONT, style, size); }
    private static Font body(int style, int size) { return new Font(BODY_FONT, style, size); }

    public Registration_form() {
        setTitle("Event Registration System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setSize(1120, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);

        BgPanel main = new BgPanel();
        main.setLayout(new BorderLayout(0, 16));
        main.setBorder(new EmptyBorder(0, 18, 16, 18));
        main.add(buildHero(), BorderLayout.NORTH);

        JPanel content = new ScrollPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        formCardRef = buildFormCard();
        tableCardRef = buildTableCard();
        searchCardRef = buildSearchCard();
        content.add(formCardRef);
        content.add(Box.createVerticalStrut(16));
        content.add(tableCardRef);
        content.add(Box.createVerticalStrut(16));
        content.add(searchCardRef);

        pageScroll = new JScrollPane(content);
        pageScroll.setBorder(null);
        pageScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pageScroll.setOpaque(false);
        pageScroll.getViewport().setOpaque(false);
        pageScroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        pageScroll.getVerticalScrollBar().setUnitIncrement(18);
        main.add(pageScroll, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
        
        registrationNav.addActionListener(e -> {
    setActiveNav(registrationNav);
    scrollTo(formCardRef);
});

participantsNav.addActionListener(e -> {
    setActiveNav(participantsNav);
    scrollTo(tableCardRef);
});

searchNav.addActionListener(e -> {
    setActiveNav(searchNav);
    scrollTo(searchCardRef);
});

eventsNav.addActionListener(e -> {
    setActiveNav(eventsNav);
    scrollTo(formCardRef);
    eventBox.requestFocusInWindow();
});
        loadRegistrations();
    }

    private void showDatabaseError(SQLException ex) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // ================= CUSTOM COMPONENTS =================

    /** Page content that always matches the window width (no sideways scrolling). */
    static class ScrollPanel extends JPanel implements Scrollable {
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        public int getScrollableUnitIncrement(
                Rectangle r, int o, int d) {
            return 18;
        }

        public int getScrollableBlockIncrement(
                Rectangle r, int o, int d) {
            return r.height;
        }

        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    static class BgPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setPaint(new GradientPaint(
                    0, 0, BG_TOP,
                    w, h, BG_BOTTOM));

            g2.fillRect(0, 0, w, h);

            g2.setColor(new Color(150, 155, 215, 45));

            Path2D glow = new Path2D.Double();
            glow.moveTo(0, h * 0.85);
            glow.curveTo(
                    w * 0.3, h * 0.65,
                    w * 0.6, h * 1.05,
                    w, h * 0.75);
            glow.lineTo(w, h);
            glow.lineTo(0, h);
            glow.closePath();

            g2.fill(glow);
            g2.dispose();
        }
    }

    static class RoundPanel extends JPanel {
        private final int arc;

        RoundPanel(int arc) {
            this.arc = arc;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(new Color(40, 40, 90, 20));
            g2.fillRoundRect(3, 6, w - 7, h - 7, arc, arc);

            g2.setColor(new Color(255, 255, 255, 240));
            g2.fillRoundRect(0, 0, w - 5, h - 7, arc, arc);

            g2.setColor(new Color(226, 224, 241));
            g2.drawRoundRect(0, 0, w - 6, h - 8, arc, arc);

            g2.dispose();
        }
    }

    static class RoundButton extends JButton {
        private Color base;
        private final Color outline;
        private final String icon;
        private final String label;
        private boolean hover;
        boolean leftAlign;

        RoundButton(
                String text, Color bg, Color fg, Color outline) {
            this(null, text, bg, fg, outline);
        }

        RoundButton(
                String icon, String text,
                Color bg, Color fg, Color outline) {

            super(icon == null ? text : "");

            this.icon = icon;
            this.label = text;
            this.base = bg;
            this.outline = outline;

            setForeground(fg);
            setFont(head(Font.PLAIN, 16));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setBorder(new EmptyBorder(12, 14, 12, 14));
            setCursor(Cursor.getPredefinedCursor(
                    Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        public void setBaseColor(Color color) {
            this.base = color;
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            if (icon == null) {
                return super.getPreferredSize();
            }

            FontMetrics tfm = getFontMetrics(getFont());
            FontMetrics ifm = getFontMetrics(
                    new Font("Segoe UI Emoji", Font.PLAIN, 17));

            return new Dimension(
                    ifm.stringWidth(icon) + 12
                            + tfm.stringWidth(label) + 44,
                    Math.max(tfm.getHeight(), ifm.getHeight()) + 26);
        }

        private static Color mix(
                Color c, Color o, double t) {
            return new Color(
                    (int) (c.getRed()
                            + (o.getRed() - c.getRed()) * t),
                    (int) (c.getGreen()
                            + (o.getGreen() - c.getGreen()) * t),
                    (int) (c.getBlue()
                            + (o.getBlue() - c.getBlue()) * t));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (base.getAlpha() == 0) {
                if (hover) {
                    g2.setColor(new Color(255, 255, 255, 18));
                    g2.fillRoundRect(0, 0, w, h, 10, 10);
                }
            } else {
                Color top = mix(base, Color.WHITE, hover ? 0.30 : 0.15);
                g2.setPaint(new GradientPaint(
                        0, 0, top, 0, h, base));

                g2.fillRoundRect(0, 0, w, h - 3, 14, 14);
            }

            if (outline != null) {
                g2.setColor(outline);
                g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);
            }

            if (icon != null) {
                g2.setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                Font iconFont = new Font(
                        "Segoe UI Emoji", Font.PLAIN, 17);

                FontMetrics ifm = g2.getFontMetrics(iconFont);
                FontMetrics tfm = g2.getFontMetrics(getFont());

                int gap = 12;
                int iw = ifm.stringWidth(icon);
                int tw = tfm.stringWidth(label);

                int x = leftAlign
                        ? 16 : (w - (iw + gap + tw)) / 2;

                g2.setColor(getForeground());
                g2.setFont(iconFont);
                g2.drawString(
                        icon, x,
                        (h - ifm.getHeight()) / 2 + ifm.getAscent());

                g2.setFont(getFont());
                g2.drawString(
                        label, x + iw + gap,
                        (h - tfm.getHeight()) / 2 + tfm.getAscent());

                g2.dispose();
                return;
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundBorder extends AbstractBorder {
        private final Color c;

        RoundBorder(Color c) {
            this.c = c;
        }

        @Override
        public void paintBorder(
                Component comp, Graphics g,
                int x, int y, int w, int h) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(c);
            g2.drawRoundRect(x, y, w - 1, h - 1, 10, 10);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component comp) {
            return new Insets(8, 14, 8, 14);
        }
    }

    static class HintField extends JTextField {
        private final String hint;

        HintField(String hint) {
            this(hint, 0);
        }

        HintField(String hint, int cols) {
            super(cols);
            this.hint = hint;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(
                    0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

            g2.dispose();
            super.paintComponent(g);

            if (getText().isEmpty()) {
                Graphics2D h = (Graphics2D) g.create();

                h.setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                h.setColor(MUTED);
                h.setFont(getFont());

                Insets in = getInsets();

                h.drawString(
                        hint, in.left,
                        (getHeight()
                                + h.getFontMetrics().getAscent()
                                - h.getFontMetrics().getDescent()) / 2);

                h.dispose();
            }
        }
    }

    private static Image loadImage(String path) {
        java.io.File f = new java.io.File(path);
        return f.exists()
                ? new ImageIcon(path).getImage()
                : null;
    }

    private JLabel circle(
            String text, int size, Color fill,
            Color outline, Color fg, int fontSize) {

        JLabel l = new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(fill);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);

                if (outline != null) {
                    g2.setColor(outline);
                    g2.drawOval(
                            0, 0, getWidth() - 1, getHeight() - 1);
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };

        l.setOpaque(false);
        l.setForeground(fg);
        l.setFont(new Font(
                "Segoe UI Emoji", Font.PLAIN, fontSize));
        l.setPreferredSize(new Dimension(size, size));

        return l;
    }

    // ================= SIDEBAR =================

    private JPanel buildSidebar() {
        JPanel side = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setPaint(new GradientPaint(
                        0, 0, new Color(20, 42, 88),
                        0, getHeight(), new Color(11, 26, 62)));

                g2.fillRect(
                        0, 0, getWidth(), getHeight());

                g2.dispose();
            }
        };

        side.setPreferredSize(new Dimension(215, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(28, 12, 18, 12));

        JLabel crest;
        Image logo = loadImage("images/logo.png");

        if (logo != null) {
            crest = new JLabel(new ImageIcon(
                    logo.getScaledInstance(
                            90, -1, Image.SCALE_SMOOTH)));
        } else {
            crest = new JLabel("✧", SwingConstants.CENTER);
            crest.setFont(new Font("Serif", Font.PLAIN, 58));
            crest.setForeground(GOLD);
        }

        crest.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel university = new JLabel(
                "RV UNIVERSITY", SwingConstants.CENTER);
        university.setFont(body(Font.PLAIN, 18));
        university.setForeground(new Color(239, 211, 151));
        university.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel motto = new JLabel(
                "Go, change the world", SwingConstants.CENTER);
        motto.setFont(head(Font.ITALIC, 12));
        motto.setForeground(new Color(220, 218, 213));
        motto.setAlignmentX(Component.CENTER_ALIGNMENT);

        side.add(crest);
        side.add(Box.createVerticalStrut(6));
        side.add(university);
        side.add(Box.createVerticalStrut(6));
        side.add(motto);
        side.add(Box.createVerticalStrut(30));

        registrationNav = (RoundButton) sideNavButton(
                "👤", "Registration", true);
        participantsNav = (RoundButton) sideNavButton(
                "👥", "Participants", false);
        searchNav = (RoundButton) sideNavButton(
                "🔍", "Search", false);
        eventsNav = (RoundButton) sideNavButton(
                "🗓", "Events", false);

        side.add(registrationNav);
        side.add(Box.createVerticalStrut(8));
        side.add(participantsNav);
        side.add(Box.createVerticalStrut(8));
        side.add(searchNav);
        side.add(Box.createVerticalStrut(8));
        side.add(eventsNav);
        side.add(Box.createVerticalGlue());

        JLabel footer = new JLabel(
                "<html>LEARN<br>COLLABORATE<br>"
                + "INNOVATE<br>BELONG</html>");

        footer.setForeground(new Color(218, 222, 237));
        footer.setFont(body(Font.PLAIN, 12));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        side.add(footer);
        side.add(Box.createVerticalStrut(10));

        JLabel line = new JLabel("━━━");
        line.setForeground(GOLD);
        line.setAlignmentX(Component.CENTER_ALIGNMENT);
        side.add(line);

        return side;
    }

    private JButton sideNavButton(
            String icon, String text, boolean selected) {

        RoundButton b = new RoundButton(
                icon, text,
                selected
                        ? new Color(88, 108, 184)
                        : new Color(0, 0, 0, 0),
                SIDE_TEXT, null);

        b.setFont(body(Font.PLAIN, 16));
        b.leftAlign = true;
        b.setBorder(new EmptyBorder(14, 16, 14, 8));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));

        return b;
    }

    private void setActiveNav(RoundButton activeButton) {
        Color transparentColor = new Color(0, 0, 0, 0);

        registrationNav.setBaseColor(transparentColor);
        participantsNav.setBaseColor(transparentColor);
        searchNav.setBaseColor(transparentColor);
        eventsNav.setBaseColor(transparentColor);

        activeButton.setBaseColor(new Color(88, 108, 184));

        registrationNav.repaint();
        participantsNav.repaint();
        searchNav.repaint();
        eventsNav.repaint();
    }

    // ================= HEADER =================

    private JPanel buildHero() {
        Image campus = loadImage("images/campus.png");

        JPanel hero = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                int w = getWidth();
                int h = getHeight();

                if (campus != null) {
                    int iw = (int) (h * 1.7);
                    int x = w - iw;

                    g2.drawImage(campus, x, 0, iw, h, null);

                    Color bg = new Color(244, 243, 252);

                    g2.setPaint(new GradientPaint(
                            x, 0, bg,
                            x + iw * 0.5f, 0,
                            new Color(244, 243, 252, 0)));

                    g2.fillRect(x, 0, iw, h);
                }

                g2.dispose();
            }
        };

        hero.setOpaque(false);
        hero.setLayout(new BorderLayout());
        hero.setBorder(new EmptyBorder(18, 30, 20, 30));
        hero.setPreferredSize(new Dimension(700, 175));

        JPanel labels = new JPanel();
        labels.setOpaque(false);
        labels.setLayout(new BoxLayout(
                labels, BoxLayout.Y_AXIS));

        JPanel title = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 0, 0));

        title.setOpaque(false);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel t1 = new JLabel("Event Registration ");
        t1.setFont(head(Font.BOLD, 46));
        t1.setForeground(NAVY);

        JLabel t2 = new JLabel("System");
        t2.setFont(head(Font.BOLD, 46));
        t2.setForeground(GOLD);

        title.add(t1);
        title.add(t2);
        title.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 64));

        JLabel subtitle = new JLabel(
                "C O N N E C T  •  P A R T I C I P A T E"
                + "  •  C R E A T E  •  G R O W");

        subtitle.setFont(body(Font.PLAIN, 13));
        subtitle.setForeground(new Color(91, 99, 130));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel eyebrow = new JLabel(
                "R  V     U  N  I  V  E  R  S  I  T  Y");

        eyebrow.setFont(body(Font.PLAIN, 13));
        eyebrow.setForeground(NAVY_LIGHT);
        eyebrow.setAlignmentX(Component.LEFT_ALIGNMENT);

        labels.add(eyebrow);
        labels.add(title);
        labels.add(Box.createVerticalStrut(6));
        labels.add(subtitle);

        hero.add(labels, BorderLayout.CENTER);

        if (campus == null) {
            JLabel accent = new JLabel("More Than Events");
            accent.setFont(head(Font.ITALIC, 26));
            accent.setForeground(GOLD);
            hero.add(accent, BorderLayout.EAST);
        }

        return hero;
    }

    // ================= FORM CARD =================

    private JPanel sectionTitle(String emoji, String title) {
        JPanel panel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 4));

        panel.setOpaque(false);

        JLabel iconLabel = new JLabel(emoji);
        iconLabel.setFont(body(Font.PLAIN, 21));
        iconLabel.setForeground(NAVY);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(body(Font.BOLD, 19));
        titleLabel.setForeground(NAVY);

        panel.add(iconLabel);
        panel.add(titleLabel);

        return panel;
    }

    private JPanel buildFormCard() {
        RoundPanel card = new RoundPanel(26);

        card.setLayout(new BorderLayout(12, 16));
        card.setBorder(new EmptyBorder(12, 26, 24, 30));

        card.add(
                sectionTitle("📝", "Registration Details"),
                BorderLayout.NORTH);

        usnField = new HintField("Enter student USN");
        nameField = new HintField("Enter participant name");
        emailField = new HintField("Enter email address");

        courseBox = new JComboBox<>(new String[]{
            "BCA", "B.Tech", "B.Com", "BBA",
            "BA", "B.Sc", "MCA", "M.Tech", "Other"
        });

        semesterBox = new JComboBox<>(new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8"
        });

        eventBox = new JComboBox<>(new String[]{
            "Tech Fest", "Workshop", "Hackathon",
            "Cultural Fest", "Seminar", "Sports Meet",
            "Guest Lecture"
        });

        dateField = new HintField("YYYY-MM-DD");
        dateField.setText(
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        dateField.setToolTipText("Format: YYYY-MM-DD");

        JPanel fields = new JPanel(
                new GridLayout(4, 2, 44, 12));
        fields.setOpaque(false);

        fields.add(fieldGroup(
                "🆔", "Student USN", usnField));
        fields.add(fieldGroup(
                "📅", "Event Name", eventBox));
        fields.add(fieldGroup(
                "👤", "Participant Name", nameField));
        fields.add(fieldGroup(
                "🗓", "Registration Date", dateField));
        fields.add(fieldGroup(
                "✉", "Email Address", emailField));
        fields.add(fieldGroup(
                "📘", "Semester", semesterBox));
        fields.add(fieldGroup(
                "🎓", "Course", courseBox));

        JPanel filler = new JPanel();
        filler.setOpaque(false);
        fields.add(filler);

        card.add(fields, BorderLayout.CENTER);

        JPanel actions = new JPanel(
                new GridLayout(1, 4, 14, 0));
        actions.setOpaque(false);

        JButton add = new RoundButton(
                "＋", "Add", NAVY, Color.WHITE, null);
        JButton update = new RoundButton(
                "✎", "Update",
                new Color(190, 151, 91), Color.WHITE, null);
        JButton delete = new RoundButton(
                "🗑", "Delete",
                new Color(196, 94, 112), Color.WHITE, null);
        JButton clear = new RoundButton(
                "↻", "Clear",
                new Color(214, 213, 240), NAVY, null);

        actions.add(add);
        actions.add(update);
        actions.add(delete);
        actions.add(clear);

        add.addActionListener(e -> addRegistration());
        update.addActionListener(e -> updateRegistration());
        delete.addActionListener(e -> deleteRegistration());
        clear.addActionListener(e -> clearFields());

        card.add(actions, BorderLayout.SOUTH);

        return card;
    }

    private JPanel fieldGroup(
            String icon, String label, JComponent input) {

        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);

        JPanel left = new JPanel(new BorderLayout(12, 0));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(190, 40));

        left.add(
                circle(icon, 38, LAVENDER, null, NAVY, 16),
                BorderLayout.WEST);

        JLabel l = new JLabel(label);
        l.setFont(body(Font.PLAIN, 14));
        l.setForeground(TEXT);

        left.add(l, BorderLayout.CENTER);

        styleInput(input);

        p.add(left, BorderLayout.WEST);
        p.add(input, BorderLayout.CENTER);

        return p;
    }

    private void styleInput(JComponent c) {
        c.setFont(body(Font.PLAIN, 14));
        c.setForeground(TEXT);
        c.setBackground(FIELD);
        c.setBorder(new RoundBorder(LINE));
        c.setPreferredSize(new Dimension(150, 40));

        if (c instanceof JTextField) {
            ((JTextField) c).setCaretColor(TEXT);
        }
    }
    // ================= PARTICIPANTS TABLE =================

    private JPanel buildTableCard() {
        RoundPanel card = new RoundPanel(24);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(new EmptyBorder(18, 22, 22, 22));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        top.add(
                sectionTitle("👥", "Registered Participants"),
                BorderLayout.WEST);

        JPanel right = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 12, 4));
        right.setOpaque(false);

        countLabel = new JLabel("Total Participants: 0");
        countLabel.setFont(body(Font.PLAIN, 13));
        countLabel.setForeground(NAVY);

        JButton viewAll = new RoundButton(
                "▣", "View All", NAVY, Color.WHITE, null);

        viewAll.addActionListener(e -> loadRegistrations());

        right.add(countLabel);
        right.add(viewAll);

        top.add(right, BorderLayout.EAST);

        model = new DefaultTableModel(
                new Object[]{
                    "S.No", "USN", "Participant Name", "Email",
                    "Course", "Semester", "Event Name",
                    "Registration Date", "DB_ID"
                }, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(36);
        table.setFont(body(Font.PLAIN, 13));
        table.setForeground(TEXT);
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(LAVENDER);
        table.setSelectionForeground(NAVY);
        table.setGridColor(new Color(233, 231, 242));
        table.setShowVerticalLines(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        table.getTableHeader().setFont(
                body(Font.BOLD, 13));
        table.getTableHeader().setBackground(
                new Color(229, 231, 247));
        table.getTableHeader().setForeground(NAVY);
        table.getTableHeader().setReorderingAllowed(false);

        int[] widths = {
            55, 125, 150, 180, 90, 70, 125, 130, 0
        };

        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i)
                    .setPreferredWidth(widths[i]);
        }

        // Hide DB_ID visually but retain it for CRUD operations.
        table.removeColumn(
                table.getColumnModel().getColumn(8));

        DefaultTableCellRenderer center =
                new DefaultTableCellRenderer();

        center.setHorizontalAlignment(SwingConstants.CENTER);

        table.getColumnModel().getColumn(0)
                .setCellRenderer(center);
        table.getColumnModel().getColumn(4)
                .setCellRenderer(center);
        table.getColumnModel().getColumn(5)
                .setCellRenderer(center);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && table.getSelectedRow() >= 0) {
                populateFieldsFromSelectedRow();
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(
                new Color(225, 223, 239)));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(650, 250));

        card.add(top, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    private JPanel buildSearchCard() {
        RoundPanel card = new RoundPanel(24);
        card.setLayout(new BorderLayout(12, 12));
        card.setBorder(new EmptyBorder(18, 22, 22, 22));

        card.add(
                sectionTitle("🔍", "Find a Registration"),
                BorderLayout.NORTH);

        JPanel controls = new JPanel(
                new GridLayout(2, 1, 0, 10));
        controls.setOpaque(false);

        JPanel searchRow = new JPanel(
                new BorderLayout(10, 0));
        searchRow.setOpaque(false);

        searchField = new HintField(
                "Search by USN, participant name, email or event");

        styleInput(searchField);

        JButton searchButton = new RoundButton(
                "🔍", "Search", NAVY, Color.WHITE, null);

        JButton resetButton = new RoundButton(
                "↻", "Show All", PURPLE, Color.WHITE, null);

        JPanel searchButtons = new JPanel(
                new GridLayout(1, 2, 8, 0));
        searchButtons.setOpaque(false);
        searchButtons.add(searchButton);
        searchButtons.add(resetButton);

        searchRow.add(searchField, BorderLayout.CENTER);
        searchRow.add(searchButtons, BorderLayout.EAST);

        JPanel idRow = new JPanel(
                new BorderLayout(10, 0));
        idRow.setOpaque(false);

        idSearchField = new HintField(
                "Enter displayed S.No, e.g. 1");

        styleInput(idSearchField);

        JButton findIdButton = new RoundButton(
                "⌕", "Find by S.No", GOLD, Color.WHITE, null);

        idRow.add(idSearchField, BorderLayout.CENTER);
        idRow.add(findIdButton, BorderLayout.EAST);

        controls.add(searchRow);
        controls.add(idRow);

        card.add(controls, BorderLayout.CENTER);

        searchButton.addActionListener(
                e -> searchRegistrations());

        searchField.addActionListener(
                e -> searchRegistrations());

        resetButton.addActionListener(e -> {
            searchField.setText("");
            idSearchField.setText("");
            loadRegistrations();
        });

        findIdButton.addActionListener(
                e -> searchById());

        idSearchField.addActionListener(
                e -> searchById());

        return card;
    }

    // ================= DATABASE CONNECTION =================

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(
                URL, USER, PASSWORD);
    }

    private void showDbError(String title, SQLException ex) {
        JOptionPane.showMessageDialog(
                this,
                title + ":\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private boolean validateFields() {
        String usn = usnField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String date = dateField.getText().trim();

        if (usn.isEmpty()
                || name.isEmpty()
                || email.isEmpty()
                || date.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in USN, name, email and date.");

            return false;
        }

        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

            JOptionPane.showMessageDialog(
                    this, "Please enter a valid email address.");

            return false;
        }

        try {
            java.sql.Date.valueOf(date);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter the date in YYYY-MM-DD format.");

            return false;
        }

        return true;
    }

    // ================= ADD REGISTRATION =================

    private void addRegistration() {
        if (!validateFields()) {
            return;
        }

        String sql =
                "INSERT INTO registrations "
                + "(usn, participant_name, email, course, semester, "
                + "event_name, registration_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usnField.getText().trim());
            ps.setString(2, nameField.getText().trim());
            ps.setString(3, emailField.getText().trim());
            ps.setString(4,
                    courseBox.getSelectedItem().toString());
            ps.setInt(5,
                    Integer.parseInt(
                            semesterBox.getSelectedItem().toString()));
            ps.setString(6,
                    eventBox.getSelectedItem().toString());
            ps.setDate(7,
                    java.sql.Date.valueOf(dateField.getText().trim()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this, "Registration added successfully!");

            loadRegistrations();
            clearFields();

        } catch (SQLException ex) {
            showDbError("Could not add registration", ex);
        }
    }

    // ================= VIEW ALL REGISTRATIONS =================

    private void loadRegistrations() {
        String sql =
                "SELECT registration_id, usn, participant_name, email, "
                + "course, semester, event_name, registration_date "
                + "FROM registrations ORDER BY registration_id ASC";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            model.setRowCount(0);

            int serialNo = 1;

            while (rs.next()) {
                model.addRow(new Object[]{
                    serialNo++,
                    rs.getString("usn"),
                    rs.getString("participant_name"),
                    rs.getString("email"),
                    rs.getString("course"),
                    rs.getInt("semester"),
                    rs.getString("event_name"),
                    rs.getDate("registration_date"),
                    rs.getInt("registration_id")
                });
            }

            countLabel.setText(
                    "Total Participants: " + model.getRowCount());

        } catch (SQLException ex) {
            showDbError("Could not load registrations", ex);
        }
    }

    // ================= SELECTED ROW =================

    private int getSelectedDatabaseId() {
        int viewRow = table.getSelectedRow();

        if (viewRow < 0) {
            return -1;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);

        // The hidden database ID is model column 8.
        Object value = model.getValueAt(modelRow, 8);

        if (value == null) {
            return -1;
        }

        return Integer.parseInt(value.toString());
    }

    private void populateFieldsFromSelectedRow() {
        int viewRow = table.getSelectedRow();

        if (viewRow < 0) {
            return;
        }

        int row = table.convertRowIndexToModel(viewRow);

        usnField.setText(
                model.getValueAt(row, 1).toString());

        nameField.setText(
                model.getValueAt(row, 2).toString());

        emailField.setText(
                model.getValueAt(row, 3).toString());

        courseBox.setSelectedItem(
                model.getValueAt(row, 4).toString());

        semesterBox.setSelectedItem(
                model.getValueAt(row, 5).toString());

        eventBox.setSelectedItem(
                model.getValueAt(row, 6).toString());

        dateField.setText(
                model.getValueAt(row, 7).toString());
    }

    // ================= UPDATE REGISTRATION =================

    private void updateRegistration() {
        int databaseId = getSelectedDatabaseId();

        if (databaseId < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Select a participant from the table before updating.");

            return;
        }

        if (!validateFields()) {
            return;
        }

        String sql =
                "UPDATE registrations SET usn=?, participant_name=?, "
                + "email=?, course=?, semester=?, event_name=?, "
                + "registration_date=? WHERE registration_id=?";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usnField.getText().trim());
            ps.setString(2, nameField.getText().trim());
            ps.setString(3, emailField.getText().trim());
            ps.setString(4,
                    courseBox.getSelectedItem().toString());
            ps.setInt(5,
                    Integer.parseInt(
                            semesterBox.getSelectedItem().toString()));
            ps.setString(6,
                    eventBox.getSelectedItem().toString());
            ps.setDate(7,
                    java.sql.Date.valueOf(dateField.getText().trim()));
            ps.setInt(8, databaseId);

            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Registration updated successfully!");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No matching registration was found.");
            }

            loadRegistrations();
            clearFields();

        } catch (SQLException ex) {
            showDbError("Could not update registration", ex);
        }
    }

    // ================= DELETE REGISTRATION =================

    private void deleteRegistration() {
        int databaseId = getSelectedDatabaseId();

        if (databaseId < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Select a participant from the table first.");

            return;
        }

        int answer = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this registration?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
                "DELETE FROM registrations WHERE registration_id=?";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, databaseId);

            int deleted = ps.executeUpdate();

            if (deleted > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Registration deleted successfully!");

                clearFields();
                loadRegistrations();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Registration was not found in the database.");
            }

        } catch (SQLException ex) {
            showDbError("Could not delete registration", ex);
        }
    }
        // ================= SEARCH REGISTRATIONS =================

    private void searchRegistrations() {
        String term = searchField.getText().trim();

        if (term.isEmpty()) {
            loadRegistrations();
            return;
        }

        String sql =
                "SELECT registration_id, usn, participant_name, email, "
                + "course, semester, event_name, registration_date "
                + "FROM registrations "
                + "WHERE usn LIKE ? "
                + "OR participant_name LIKE ? "
                + "OR email LIKE ? "
                + "OR course LIKE ? "
                + "OR event_name LIKE ? "
                + "ORDER BY registration_id ASC";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String pattern = "%" + term + "%";

            for (int i = 1; i <= 5; i++) {
                ps.setString(i, pattern);
            }

            model.setRowCount(0);

            int serialNo = 1;

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        serialNo++,
                        rs.getString("usn"),
                        rs.getString("participant_name"),
                        rs.getString("email"),
                        rs.getString("course"),
                        rs.getInt("semester"),
                        rs.getString("event_name"),
                        rs.getDate("registration_date"),
                        rs.getInt("registration_id")
                    });
                }
            }

            countLabel.setText(
                    "Total Participants: " + model.getRowCount());

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "No matching registrations found.");
            }

        } catch (SQLException ex) {
            showDbError("Search failed", ex);
        }
    }

    // ================= SEARCH BY DISPLAYED S.NO =================

    private void searchById() {
        String text = idSearchField.getText().trim();

        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a registration S.No.");

            return;
        }

        int serialNo;

        try {
            serialNo = Integer.parseInt(text);

            if (serialNo < 1) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "S.No must be a positive number.");

            return;
        }

        /*
         * S.No is the position of a record in the ordered result.
         * It is NOT the actual registration_id stored in MySQL.
         */
        String sql =
                "SELECT registration_id, usn, participant_name, email, "
                + "course, semester, event_name, registration_date "
                + "FROM registrations "
                + "ORDER BY registration_id ASC";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            model.setRowCount(0);

            int currentSerial = 1;
            boolean found = false;

            while (rs.next()) {
                if (currentSerial == serialNo) {
                    model.addRow(new Object[]{
                        currentSerial,
                        rs.getString("usn"),
                        rs.getString("participant_name"),
                        rs.getString("email"),
                        rs.getString("course"),
                        rs.getInt("semester"),
                        rs.getString("event_name"),
                        rs.getDate("registration_date"),
                        rs.getInt("registration_id")
                    });

                    found = true;
                    break;
                }

                currentSerial++;
            }

            countLabel.setText(
                    "Total Participants: " + model.getRowCount());

            if (found) {
                table.setRowSelectionInterval(0, 0);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No registration found for S.No "
                                + serialNo + ".");
            }

        } catch (SQLException ex) {
            showDbError("Search by S.No failed", ex);
        }
    }

    // ================= CLEAR FORM =================

    private void clearFields() {
        usnField.setText("");
        nameField.setText("");
        emailField.setText("");

        dateField.setText(
                new SimpleDateFormat("yyyy-MM-dd")
                        .format(new Date()));

        courseBox.setSelectedIndex(0);
        semesterBox.setSelectedIndex(0);
        eventBox.setSelectedIndex(0);

        if (table != null) {
            table.clearSelection();
        }

        if (searchField != null) {
            searchField.setText("");
        }

        if (idSearchField != null) {
            idSearchField.setText("");
        }

        if (usnField != null) {
            usnField.requestFocusInWindow();
        }
    }

    // ================= SCROLL TO SECTION =================

    private void scrollTo(Component component) {
        if (component == null || pageScroll == null) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            Rectangle bounds =
                    SwingUtilities.convertRectangle(
                            component.getParent(),
                            component.getBounds(),
                            pageScroll.getViewport());

            pageScroll.getViewport()
                    .scrollRectToVisible(bounds);
        });
    }

    // ================= MAIN METHOD =================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Continue with the default appearance.
            }

            Registration_form app = new Registration_form();
            app.setVisible(true);
        });
    }
}