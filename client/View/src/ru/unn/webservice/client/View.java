package ru.unn.webservice.client;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;


public final class View {
    private IViewModel viewmodel;

    private JPanel mainPanel;
    private JPanel searchPanel;
    private JPanel authorizationPanel;
    private JPanel browserPanel;
    private JPanel userPanel;
    private JPanel userProfilePanel;
    private JPanel statusPanel;
    private JPanel startPanel;
    private JPanel inputPanel;
    private JPanel developerFunctionsPanel;
    private JPanel adminFunctionsPanel;
    private JPanel searchResultPanel;
    private JPanel algorithmPanel;

    private JTextField loginField;
    private JTextField searchField;
    private JTextField passwordField;
    private JTextField userNameField;
    private JTextField userTypeField;
    private JTextField balanceField;
    private JTextField statusLabel;
    private JTextField downloadsField;
    private JTextField purchasesField;

    private JButton searchButton;
    private JButton authorizationSubmitButton;
    private JButton authorizationBackButton;
    private JButton registerButton;
    private JButton loginButton;
    private JButton refillBalanceButton;
    private JButton logOutButton;
    private JButton addAlgorithmButton;
    private JButton getStatisticButton;
    private JButton buyButton;
    private JButton downloadButton;
    private JButton testAlgorithmButton;
    private JButton backButton;

    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JLabel userLabel;
    private JLabel userTypeLabel;
    private JLabel balanceLabel;
    private JLabel dateLabel;
    private JLabel downloadsLabel;
    private JLabel purchasesLabel;

    private JTextPane logPane;
    private JTextPane showAlgorithmField;

    private JScrollPane algorithmScrollPane;
    private JScrollPane logScrollPane;

    private JList searchResultList;

    private JCheckBox developerCheckBox;

    private boolean isRegisterMode;

    private View() {
        viewmodel = new Client();

        KeyListener keyListener = new KeyListener() {
            public void keyPressed(KeyEvent e) {
                bind();
                if (e.getKeyCode() == KeyEvent.VK_F1) {
                    JTextField ipField = new JTextField();
                    final JComponent[] inputs = new JComponent[]{
                            new JLabel("Enter ip:"), ipField
                    };

                    if (JOptionPane.showConfirmDialog(null, inputs, "Server IP change dialog", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                        if (ipField.getText().matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
                            try {
                                IViewModel newViewModel = new Client(ipField.getText());
                                viewmodel = newViewModel;
                                viewmodel.logout();
                                viewmodel.setStatus("Connected to " + ipField.getText());
                            } catch (Exception e1) {
                                viewmodel.setStatus("Connection refused");
                            }
                        }
                    }
                }
                backBind();
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        };

        mainPanel.addKeyListener(keyListener);
        mainPanel.setFocusable(true);

        showAlgorithmField.setContentType("text/html");
        algorithmScrollPane = new JScrollPane(showAlgorithmField);
        logScrollPane = new JScrollPane(logPane);
        algorithmPanel.add(algorithmScrollPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        algorithmPanel.add(logScrollPane, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));

        registerButton.addActionListener(e -> {
            startPanel.setVisible(false);
            inputPanel.setVisible(true);
            developerCheckBox.setVisible(true);
            isRegisterMode = true;
        });

        loginButton.addActionListener(e -> {
            startPanel.setVisible(false);
            inputPanel.setVisible(true);
            developerCheckBox.setVisible(false);
            isRegisterMode = false;
        });

        authorizationBackButton.addActionListener(e -> {
            startPanel.setVisible(true);
            inputPanel.setVisible(false);
        });

        authorizationSubmitButton.addActionListener(e -> {
            bind();
            if (isRegisterMode) {
                viewmodel.register();
            } else {
                viewmodel.authorize();
            }
            backBind();
        });

        refillBalanceButton.addActionListener(e -> {
            bind();
            JTextField sumField = new JTextField();
            final JComponent[] inputs = new JComponent[]{
                    new JLabel("Enter sum:"), sumField
            };

            if (JOptionPane.showConfirmDialog(null, inputs, "Balance refill dialog", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                viewmodel.refillBalance(sumField.getText());
            }

            backBind();
        });

        logOutButton.addActionListener(e -> {
            bind();
            viewmodel.logout();
            mainPanel.requestFocus();
            backBind();
        });

        searchButton.addActionListener(e -> {
            bind();
            viewmodel.search();
            viewmodel.setIsSearchResultPanelVisible(true);
            viewmodel.setIsAlgorithmPanelVisible(false);
            backBind();
        });

        addAlgorithmButton.addActionListener(e -> {
            bind();
            AddAlgorithmDialog dialog = new AddAlgorithmDialog(viewmodel);
            dialog.pack();
            dialog.setVisible(true);
            backBind();
        });

        searchResultList.addListSelectionListener(e -> {
            bind();
            viewmodel.setIsSearchResultPanelVisible(false);
            viewmodel.setIsAlgorithmPanelVisible(true);
            viewmodel.setSelectedAlgorithmIndex(searchResultList.getSelectedIndex());
            viewmodel.getSelectedAlgorithm();
            viewmodel.setLogField("");
            backBind();
        });

        getStatisticButton.addActionListener(e -> {
            bind();

            final JFrame f = new JFrame();
            f.setTitle("Choose start date");
            Date from = new DatePicker(f).setPickedDate();
            f.setTitle("Choose end date");
            Date to = new DatePicker(f).setPickedDate();

            viewmodel.setFrom(from);
            viewmodel.setTo(to);

            viewmodel.getStatistic();

            backBind();
        });

        backButton.addActionListener(e -> {
            bind();
            viewmodel.setIsSearchResultPanelVisible(true);
            viewmodel.setIsAlgorithmPanelVisible(false);
            viewmodel.setLogField("");
            backBind();
        });

        downloadButton.addActionListener(e -> {
            bind();
            viewmodel.downloadAlgorithm();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                viewmodel.saveAlgorithm(fileChooser.getSelectedFile());
            }
            backBind();
        });

        buyButton.addActionListener(e -> {
            bind();
            final JComponent[] inputs = new JComponent[]{
                    new JLabel("Do you really want to buy " + viewmodel.getAlgorithmToShow().name + " algorithm?")
            };

            if (JOptionPane.showConfirmDialog(null, inputs, "Purchase confirm dialog", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                viewmodel.buyAlgorithm();
            }

            backBind();
        });

        testAlgorithmButton.addActionListener(e -> {
            bind();
            final JComponent[] inputs = new JComponent[]{
                    new JLabel("<html>Do you want to add your own test data?<br> (If you say 'NO' algorithm will be tested with developer data)")
            };

            viewmodel.setUserData(null);
            if (JOptionPane.showConfirmDialog(null, inputs, "Add test data dialog", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "txt", "TXT");
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(filter);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    viewmodel.setUserData(fileChooser.getSelectedFile());
                    viewmodel.testAlgorithm();
                }
            } else {
                viewmodel.testAlgorithm();
            }


            backBind();
        });

        bind();
    }

    private View(final Client viewModel) {
        this.viewmodel = viewModel;
    }

    public static void main(final String[] args) {
        JFrame frame = new JFrame("Algorithm webservice client");


        frame.setContentPane(new View().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void bind() {
        viewmodel.setLogField(logPane.getText());
        viewmodel.setIsSearchResultPanelVisible(searchResultPanel.isVisible());
        viewmodel.setIsAlgorithmPanelVisible(algorithmPanel.isVisible());
        viewmodel.setSearchResultList(searchResultList.getModel());
        viewmodel.setSearchField(searchField.getText());
        viewmodel.setIsInputPanelVisible(inputPanel.isVisible());
        viewmodel.setIsStartPanelVisible(startPanel.isVisible());
        viewmodel.setLogin(loginField.getText());
        viewmodel.setPassword(passwordField.getText());
        viewmodel.setIsDeveloper(developerCheckBox.isSelected());
        viewmodel.setAuthorizationPanelVisible(authorizationPanel.isVisible());
        viewmodel.setIsUserProfilePanelVisible(userProfilePanel.isVisible());
        viewmodel.setStatus(statusLabel.getText());
        viewmodel.setIsDeveloperPanelVisible(developerFunctionsPanel.isVisible());
        viewmodel.setisAdminPanelVisible(adminFunctionsPanel.isVisible());
        viewmodel.setIsSearchPanelVisible(searchPanel.isVisible());
    }

    private void backBind() {
        viewmodel.updateUser();
        loginField.setText(viewmodel.getLogin());
        passwordField.setText(viewmodel.getPassword());
        searchResultPanel.setVisible(viewmodel.getIsSearchResultPanelVisible());
        algorithmPanel.setVisible(viewmodel.getIsAlgorithmPanelVisible());
        searchResultList.setModel(viewmodel.getSearchResultList());
        searchField.setText(viewmodel.getSearchField());
        startPanel.setVisible(viewmodel.getIsStartPanelVisible());
        inputPanel.setVisible(viewmodel.getIsInputPanelVisible());
        authorizationPanel.setVisible(viewmodel.getIsAuthorizationPanelVisible());
        userProfilePanel.setVisible(viewmodel.getIsUserProfilePanelVisible());
        userNameField.setText(viewmodel.getUserName());
        balanceField.setText(viewmodel.getUserBalance());
        userTypeField.setText(viewmodel.getUserType());
        statusLabel.setText(viewmodel.getStatus());
        developerFunctionsPanel.setVisible(viewmodel.getIsDeveloperPanelVisible());
        adminFunctionsPanel.setVisible(viewmodel.getIsAdminPanelVisible());
        dateLabel.setText(viewmodel.getStatisticDateLabel());
        downloadsField.setText(viewmodel.getDownloadsCount());
        purchasesField.setText(viewmodel.getPurchasesCount());
        showAlgorithmField.setText(viewmodel.getShowAlgorithmField());
        searchPanel.setVisible(viewmodel.getIsSearchPanelVisible());
        buyButton.setVisible(!viewmodel.getCanDownload());
        downloadButton.setVisible(viewmodel.getCanDownload());
        logPane.setText(viewmodel.getLogField());
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 5, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setMaximumSize(new Dimension(1920, 1080));
        mainPanel.setMinimumSize(new Dimension(10, 10));
        mainPanel.setPreferredSize(new Dimension(800, 600));
        mainPanel.setRequestFocusEnabled(false);
        browserPanel = new JPanel();
        browserPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        browserPanel.setFocusable(false);
        browserPanel.setVisible(true);
        mainPanel.add(browserPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        algorithmPanel = new JPanel();
        algorithmPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 1, new Insets(0, 0, 0, 0), -1, -1));
        algorithmPanel.setFocusable(false);
        algorithmPanel.setVisible(false);
        browserPanel.add(algorithmPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buyButton = new JButton();
        buyButton.setText("Buy");
        algorithmPanel.add(buyButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        testAlgorithmButton = new JButton();
        testAlgorithmButton.setText("Test Algorithm");
        algorithmPanel.add(testAlgorithmButton, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        algorithmPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 10), new Dimension(-1, 10), new Dimension(-1, 10), 0, false));
        downloadButton = new JButton();
        downloadButton.setText("Download");
        algorithmPanel.add(downloadButton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        algorithmPanel.add(backButton, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        algorithmPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        showAlgorithmField = new JTextPane();
        showAlgorithmField.setEditable(false);
        showAlgorithmField.setFocusable(false);
        algorithmPanel.add(showAlgorithmField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        logPane = new JTextPane();
        logPane.setEditable(false);
        logPane.setFocusable(false);
        algorithmPanel.add(logPane, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        searchResultPanel = new JPanel();
        searchResultPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        searchResultPanel.setFocusable(false);
        searchResultPanel.setVisible(true);
        browserPanel.add(searchResultPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        searchResultList = new JList();
        searchResultList.setFocusable(false);
        searchResultPanel.add(searchResultList, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        searchPanel = new JPanel();
        searchPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        searchPanel.setFocusable(false);
        searchPanel.setVisible(false);
        mainPanel.add(searchPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        searchField = new JTextField();
        searchPanel.add(searchField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        searchButton = new JButton();
        searchButton.setText("Search");
        searchPanel.add(searchButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userPanel = new JPanel();
        userPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        userPanel.setFocusable(false);
        userPanel.setVisible(true);
        mainPanel.add(userPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 4, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(300, -1), new Dimension(300, -1), new Dimension(300, -1), 0, false));
        authorizationPanel = new JPanel();
        authorizationPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        authorizationPanel.setFocusable(false);
        userPanel.add(authorizationPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(300, -1), new Dimension(300, -1), new Dimension(300, -1), 0, false));
        startPanel = new JPanel();
        startPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        startPanel.setFocusable(false);
        authorizationPanel.add(startPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(300, -1), new Dimension(300, -1), new Dimension(300, -1), 0, false));
        loginButton = new JButton();
        loginButton.setText("Log In");
        startPanel.add(loginButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        registerButton = new JButton();
        registerButton.setText("Register");
        startPanel.add(registerButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        inputPanel = new JPanel();
        inputPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        inputPanel.setFocusable(false);
        inputPanel.setVisible(false);
        authorizationPanel.add(inputPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(300, -1), new Dimension(300, -1), new Dimension(300, -1), 0, false));
        loginField = new JTextField();
        loginField.setText("");
        loginField.setToolTipText("");
        loginField.setVisible(true);
        inputPanel.add(loginField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordField = new JTextField();
        passwordField.setVisible(true);
        inputPanel.add(passwordField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        authorizationBackButton = new JButton();
        authorizationBackButton.setText("Back");
        authorizationBackButton.setVisible(true);
        inputPanel.add(authorizationBackButton, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        authorizationSubmitButton = new JButton();
        authorizationSubmitButton.setText("Submit");
        authorizationSubmitButton.setVisible(true);
        inputPanel.add(authorizationSubmitButton, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        developerCheckBox = new JCheckBox();
        developerCheckBox.setText("developer?");
        developerCheckBox.setVisible(true);
        inputPanel.add(developerCheckBox, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordLabel = new JLabel();
        passwordLabel.setFocusable(false);
        passwordLabel.setText("Password:");
        passwordLabel.setVisible(true);
        inputPanel.add(passwordLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loginLabel = new JLabel();
        loginLabel.setFocusable(false);
        loginLabel.setText("Login: ");
        loginLabel.setVisible(true);
        inputPanel.add(loginLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userProfilePanel = new JPanel();
        userProfilePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        userProfilePanel.setFocusable(false);
        userProfilePanel.setVisible(false);
        userPanel.add(userProfilePanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        userLabel = new JLabel();
        userLabel.setFocusable(false);
        userLabel.setText("User: ");
        userProfilePanel.add(userLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userNameField = new JTextField();
        userNameField.setEditable(false);
        userNameField.setEnabled(true);
        userNameField.setFocusable(false);
        userProfilePanel.add(userNameField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        userTypeLabel = new JLabel();
        userTypeLabel.setFocusable(false);
        userTypeLabel.setText("User Type:");
        userProfilePanel.add(userTypeLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userTypeField = new JTextField();
        userTypeField.setEditable(false);
        userTypeField.setFocusable(false);
        userProfilePanel.add(userTypeField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        balanceLabel = new JLabel();
        balanceLabel.setFocusable(false);
        balanceLabel.setText("Balance:");
        userProfilePanel.add(balanceLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        balanceField = new JTextField();
        balanceField.setEditable(false);
        balanceField.setFocusable(false);
        userProfilePanel.add(balanceField, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        refillBalanceButton = new JButton();
        refillBalanceButton.setText("Refill balance");
        userProfilePanel.add(refillBalanceButton, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logOutButton = new JButton();
        logOutButton.setText("Log Out");
        userProfilePanel.add(logOutButton, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        adminFunctionsPanel = new JPanel();
        adminFunctionsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        adminFunctionsPanel.setFocusable(false);
        adminFunctionsPanel.setVisible(false);
        userPanel.add(adminFunctionsPanel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        getStatisticButton = new JButton();
        getStatisticButton.setText("Get Statistic");
        adminFunctionsPanel.add(getStatisticButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dateLabel = new JLabel();
        dateLabel.setFocusable(false);
        dateLabel.setRequestFocusEnabled(true);
        dateLabel.setText("");
        adminFunctionsPanel.add(dateLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        downloadsLabel = new JLabel();
        downloadsLabel.setFocusable(false);
        downloadsLabel.setText("Downloads");
        adminFunctionsPanel.add(downloadsLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        downloadsField = new JTextField();
        downloadsField.setEditable(false);
        downloadsField.setFocusable(false);
        adminFunctionsPanel.add(downloadsField, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        purchasesField = new JTextField();
        purchasesField.setEditable(false);
        purchasesField.setFocusable(false);
        adminFunctionsPanel.add(purchasesField, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        purchasesLabel = new JLabel();
        purchasesLabel.setFocusable(false);
        purchasesLabel.setText("Purchases");
        adminFunctionsPanel.add(purchasesLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        developerFunctionsPanel = new JPanel();
        developerFunctionsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        developerFunctionsPanel.setFocusable(false);
        developerFunctionsPanel.setVisible(false);
        userPanel.add(developerFunctionsPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        addAlgorithmButton = new JButton();
        addAlgorithmButton.setText("Add Algorithm");
        developerFunctionsPanel.add(addAlgorithmButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        userPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        statusPanel = new JPanel();
        statusPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        statusPanel.setFocusable(false);
        mainPanel.add(statusPanel, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        statusLabel = new JTextField();
        statusLabel.setEditable(false);
        statusLabel.setFocusable(false);
        statusPanel.add(statusLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 10), new Dimension(-1, 10), new Dimension(-1, 10), 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(10, -1), new Dimension(10, -1), new Dimension(10, -1), 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer6, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 10), new Dimension(-1, 10), new Dimension(-1, 10), 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer7 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer7, new com.intellij.uiDesigner.core.GridConstraints(2, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(10, -1), new Dimension(10, -1), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer8 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer8, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(10, -1), new Dimension(10, -1), new Dimension(10, -1), 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
