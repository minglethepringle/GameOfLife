import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * MINGLE LI
 */
public class LifePanel extends JPanel implements ActionListener, MouseListener {
    private Timer timer;
    private World world;
    public static int width = 800;
    public static int height = 800;
    public static int rows = 100, cols = 100;
    public PresetParser parser;
    public JComboBox preset;
    private boolean placingPreset = false;

    public LifePanel(int w, int h){
        setSize(w, h);

        world = new World(rows,cols);

        parser = new PresetParser("presets/");

        timer = new Timer(100, this);
        timer.start();

        addMouseListener(this);

        buildGUI(this);
    }

    public void buildGUI(JPanel panel) {
        JButton start_stop = new JButton("Start/Stop");
        start_stop.setBounds(0, 5, 100, 25);
        start_stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(timer.isRunning()) {
                    timer.stop();
                } else {
                    timer.start();
                }
            }
        });
        panel.add(start_stop);

        JButton reset = new JButton("Reset");
        reset.setBounds(110, 5, 100, 25);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                world.resetWorld();
                repaint();
            }
        });
        panel.add(reset);

        JButton clear = new JButton("Clear");
        clear.setBounds(220, 5, 100, 25);
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                world.clearWorld();
                repaint();
            }
        });
        panel.add(clear);

        JLabel sliderLabel = new JLabel("Speed");
        sliderLabel.setBounds(340, 5, 50, 25);
        panel.add(sliderLabel);

        JSlider speed = new JSlider(1, 20, 10);
        speed.setBounds(380, 5, 100, 25);
        speed.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int s = speed.getValue();
                timer.setDelay(1000 / s);
            }
        });
        panel.add(speed);

        preset = new JComboBox(parser.getPresetList());
        preset.setBounds(500, 5, 190, 25);
        panel.add(preset);

        JButton load = new JButton("Load");
        load.setBounds(700, 5, 90, 25);
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                if(placingPreset) {
                    // cancel placing
                    placingPreset = false;
                    load.setText("Load");
                } else {
                    // activate placing
                    placingPreset = true;
                    load.setText("Cancel");
                }
            }
        });
        panel.add(load);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        world.drawWorld(g2);

        // GUI
        g2.setColor(Color.white);
        g2.fillRect(0, 0, width, 40);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        world.nextGeneration();
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override

    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getY() > 25 && timer.isRunning()) timer.stop();

        int c = e.getX() / World.pixelInterval; // remember that X value = columns in a 2D array
        int r = e.getY() / World.pixelInterval; // remember that Y value = rows in a 2D array

        if(placingPreset) {
            try {
                parser.parsePreset(world, r, c, (String) preset.getSelectedItem());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else {
            world.drawIn(r, c);
        }

        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Life of Mingle");
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height + 24));

        JPanel panel = new LifePanel(width, height);
        panel.setFocusable(true);
        panel.grabFocus();
        panel.setLayout(null);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}