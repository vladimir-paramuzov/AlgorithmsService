package ru.unn.webservice.client;

import javax.swing.*;
import java.awt.event.*;

public final class View {
    private JPanel mainPanel;
    private JButton registerButton;

    private View() {

    }

    private View(final IViewModel viewModel) {

    }
    public static void main(final String[] args) {
        JFrame frame = new JFrame("Algorithm webservice client");

        frame.setContentPane(new View().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
