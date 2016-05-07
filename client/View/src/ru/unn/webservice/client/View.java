package ru.unn.webservice.client;

import javax.swing.*;
import java.awt.event.*;

public final class View {
    private IViewModel viewModel;
    private JPanel mainPanel;
    private JPanel viewPanel;
    private JPanel settingsPanel;
    private JTextField tagTextField;
    private JButton searchButton;
    private JButton superUserButton;
    private JButton logOutButton;
    private JButton addAlgorithmButton;
    private JButton giveUsYourMoneyButton;
    private JButton authorizationButton;
    private JButton registerButton;
    private JList algorithmsList;
    private JLabel balanceLabel;
    private JLabel userTypeLabel;

    private View() {

    }

    private View(final IViewModel viewModel) {
        this.viewModel = viewModel;
        bind();

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backBind();
                viewModel.searchAlgorithm();
                bind();
            }
        });
    }

    private void bind() {
        searchButton.setEnabled(viewModel.isSearchButtonEnabled());
        algorithmsList.setListData(viewModel.getListAlgorithms().toArray());
        tagTextField.setText(viewModel.getTag());
    }

    private void backBind() {
        viewModel.setTag(tagTextField.getText());
    }

    public static void main(final String[] args) {
        JFrame frame = new JFrame("Algorithm webservice client");

        frame.setContentPane(new View().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
