import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.io.IOException;

public class Client implements Runnable {
    public static void main(String[] args) {
        Client client = new Client();
        Thread one = new Thread(client);
        one.start();
        try {
            one.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Socket socket = new Socket("localhost", 4232);

            ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());

            boolean firstCheck = true; // boolean to check user
            boolean firstSeller = true; // boolean to check seller
            boolean firstCustomer = true; // boolean to check customer

            int loginMenu = 0;

            do {
                ArrayList<String> loginInput = new ArrayList<>();
                String[] buttons = {"Login", "Signup", "Exit"};
                loginMenu = JOptionPane.showOptionDialog(null,
                        "Please select a choice!", "Login/Registration",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, buttons[0]);

                // login = 0, sign up = 1, exit = 2, x = -1
                if (loginMenu == 1) {
                    // sign up = 1
                    loginInput.add("signup");
                    writer.writeObject(loginInput);
                    writer.flush();

                    boolean signupCheck = false;

                    do {
                        JTextField userName = new JTextField();
                        JTextField passWord = new JTextField();
                        JTextField accountType = new JTextField();
                        ArrayList<String> signupInfo = new ArrayList<>();
                        Object[] fields = {"Account type (seller/customer)", accountType,
                                "Username: ", userName, "Password", passWord};

                        int signupButton = JOptionPane.showConfirmDialog(null,
                                fields, "Sign up", JOptionPane.OK_CANCEL_OPTION);

                        // ok = 0, cancel = 2, x = -1
                        if (signupButton == 2 || signupButton == -1) {
                            signupCheck = false;
                            signupInfo.add("exit");
                            writer.writeObject(signupInfo);

                        } else {
                            if (userName != null || passWord != null || accountType != null) {
                                if (userName.getText().equals("") || passWord.getText().equals("")
                                        || accountType.getText().equals("")) {
                                    // see if any empty blocks
                                    JOptionPane.showMessageDialog(null,
                                            "Please fill in all blanks!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    signupCheck = true;

                                } else if (!accountType.getText().equals("seller") &&
                                        !accountType.getText().equals("customer")) {
                                    // see if account type is valid
                                    JOptionPane.showMessageDialog(null,
                                            "Account type could only be seller or customer!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    signupCheck = true;

                                } else if (!userName.getText().equals("") && !passWord.getText().equals("")
                                        && !accountType.getText().equals("")) {
                                    signupInfo.add("signup");
                                    signupInfo.add(userName.getText());
                                    signupInfo.add(passWord.getText());
                                    signupInfo.add(accountType.getText());

                                    // write username, password, account type to server
                                    writer.writeObject(signupInfo);
                                    writer.flush();

                                    boolean isValid = reader.readBoolean();

                                    if (isValid) {
                                        // username is valid
                                        signupCheck = false;
                                        firstCheck = true;
                                        JOptionPane.showMessageDialog(null,
                                                "Account created!",
                                                "Sign up", JOptionPane.PLAIN_MESSAGE);
                                    } else {
                                        //username has been taken
                                        signupCheck = true;

                                        JOptionPane.showMessageDialog(null,
                                                "Username has been taken!",
                                                "Error", JOptionPane.ERROR_MESSAGE);
                                    }

                                }
                            } else {
                                JOptionPane.showMessageDialog(null,
                                        "Please fill in all blanks!",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                signupCheck = true;

                            }

                        }
                    } while (signupCheck);

                } else if (loginMenu == 2) {
                    // exit = 2
                    loginInput.add("exit");
                    firstCheck = false;

                    writer.writeObject(loginInput);
                    writer.flush();

                    JOptionPane.showMessageDialog(null, "Goodbye!",
                            "Exit", JOptionPane.INFORMATION_MESSAGE);

                } else if (loginMenu == -1) {
                    // x = -1
                    loginInput.add("exit");
                    firstCheck = false;

                    writer.writeObject(loginInput);
                    writer.flush();

                    JOptionPane.showMessageDialog(null, "Goodbye!",
                            "Exit", JOptionPane.INFORMATION_MESSAGE);

                } else if (loginMenu == 0) {
                    // login = 1
                    loginInput.add("login");

                    writer.writeObject(loginInput);
                    writer.flush();
                    boolean loginCheck = false;

                    do {
                        JTextField userName = new JTextField();
                        JTextField passWord = new JTextField();
                        Object[] fields = {"Username:", userName, "Password:", passWord};
                        ArrayList<String> loginInfo = new ArrayList<>();
                        String checkLogin;

                        int login = JOptionPane.showConfirmDialog(null,
                                fields, "Log in", JOptionPane.OK_CANCEL_OPTION);


                        // ok = 0, cancel = 2, x = -1
                        if (login == 2 || login == -1) {
                            loginCheck = false;
                            loginInfo.add("exit");
                            writer.writeObject(loginInfo);
                            writer.flush();

                        } else {
                            loginInfo.add("login");
                            loginInfo.add(userName.getText());
                            loginInfo.add(passWord.getText());

                            // write login username & password
                            writer.writeObject(loginInfo);
                            writer.flush();

                            checkLogin = (String) reader.readObject();

                            if (checkLogin.equals("Invalid username and/or password!")) {
                                JOptionPane.showMessageDialog(null,
                                        checkLogin, "Error", JOptionPane.ERROR_MESSAGE);
                                loginCheck = true;

                            } else if (checkLogin.equals("seller")) {
                                JOptionPane.showMessageDialog(null, "Logged In!",
                                        "Log in", JOptionPane.INFORMATION_MESSAGE);

                                boolean sellerCheck = false;

                                sellerMenu:
                                do {
                                    ButtonGroup sellerGroup = new ButtonGroup();

                                    JRadioButton createProduct = new JRadioButton("Create Product");
                                    JRadioButton editProduct = new JRadioButton("Edit Product");
                                    JRadioButton deleteProduct = new JRadioButton("Delete Product");
                                    JRadioButton viewSells = new JRadioButton("View Sells");
                                    JRadioButton export = new JRadioButton("Export Purchase History");
                                    JRadioButton importButton = new JRadioButton("Import");
                                    JRadioButton viewDashboard = new JRadioButton("View Dashboard");
                                    JRadioButton quit = new JRadioButton("Quit");

                                    sellerGroup.add(createProduct);
                                    sellerGroup.add(editProduct);
                                    sellerGroup.add(deleteProduct);
                                    sellerGroup.add(viewSells);
                                    sellerGroup.add(export);
                                    sellerGroup.add(importButton);
                                    sellerGroup.add(viewDashboard);
                                    sellerGroup.add(quit);

                                    Object[] fieldsOne = {createProduct, editProduct, deleteProduct, viewSells, export,
                                            importButton, viewDashboard, quit};

                                    int sellerMenu = JOptionPane.showConfirmDialog(null,
                                            fieldsOne, "Seller Menu", JOptionPane.OK_CANCEL_OPTION);

                                    ArrayList<String> sellerInput = new ArrayList<>();

                                    //  cancel = 2, x = -1, others = 0
                                    if (sellerMenu == 2 || sellerMenu == -1 || quit.isSelected()) {
                                        sellerCheck = false;
                                        firstCheck = true;
                                        sellerInput.add("exit");
                                        writer.writeObject(sellerInput);
                                        writer.flush();

                                    } else {
                                        // check user's choice
                                        if (createProduct.isSelected()) {
                                            // create Product
                                            sellerInput.add("createProduct");
                                            writer.writeObject(sellerInput);
                                            writer.flush();

                                            // Ask for product info
                                            String productName = JOptionPane.showInputDialog(
                                                    null, "Please enter the " +
                                                            "product name.", "Create Product",
                                                    JOptionPane.QUESTION_MESSAGE);
                                            String storeName = JOptionPane.showInputDialog(
                                                    null, "Please enter the " +
                                                            "store name.", "Create Product",
                                                    JOptionPane.QUESTION_MESSAGE);
                                            String description = JOptionPane.showInputDialog(
                                                    null, "Please enter the " +
                                                            "product description.", "Create Product",
                                                    JOptionPane.QUESTION_MESSAGE);
                                            String quantityString = JOptionPane.showInputDialog(
                                                    null, "Please enter " +
                                                            "the quantity of the product.", "Create Product",
                                                    JOptionPane.QUESTION_MESSAGE);
                                            String priceString = JOptionPane.showInputDialog(
                                                    null, "Please enter the " +
                                                            "price of the product.", "Create Product",
                                                    JOptionPane.QUESTION_MESSAGE);
                                            String[] productInfo = {productName, storeName, description,
                                                    quantityString, priceString};
                                            writer.writeObject(productInfo);
                                            writer.flush();
                                            boolean actionQuit = reader.readBoolean();
                                            if (actionQuit) {
                                                continue;
                                            }
                                            boolean invalid = reader.readBoolean();
                                            if (invalid) {
                                                JOptionPane.showMessageDialog(null,
                                                        "Invalid input! Failed to create " +
                                                                "product.", "Create Product",
                                                        JOptionPane.ERROR_MESSAGE);
                                            } else {
                                                JOptionPane.showMessageDialog(null,
                                                        "Product created!", "Create " +
                                                                "Product", JOptionPane.INFORMATION_MESSAGE);
                                            }

                                            sellerCheck = true;

                                        } else if (editProduct.isSelected()) {
                                            // edit Product
                                            sellerInput.add("editProduct");
                                            writer.writeObject(sellerInput);
                                            writer.flush();

                                            // Check if the seller has a store.
                                            String storeNames = (String) reader.readObject();
                                            // If seller does not have a store, return to the seller menu.
                                            if (storeNames == null) {
                                                JOptionPane.showMessageDialog(null,
                                                        "You don't have a store yet.",
                                                        "Edit Product", JOptionPane.ERROR_MESSAGE);
                                                continue sellerMenu;
                                            } else {
                                                JOptionPane.showMessageDialog(null, storeNames,
                                                        "Your Stores",
                                                        JOptionPane.INFORMATION_MESSAGE);
                                            }

                                            // Ask for storeName and check its existence.
                                            String storeInfo;
                                            do {
                                                // Send storeName.
                                                String storeName = JOptionPane.showInputDialog(null,
                                                        "Please enter " +
                                                                "the store name.", "Edit Product",
                                                        JOptionPane.QUESTION_MESSAGE);
                                                writer.writeObject(storeName);
                                                writer.flush();
                                                if (storeName == null) { // user wants to quit
                                                    continue sellerMenu;
                                                }
                                                // Receive storeInfo.
                                                storeInfo = (String) reader.readObject();
                                                if (storeInfo != null) { // store exist, display store info
                                                    JOptionPane.showMessageDialog(null, storeInfo,
                                                            "Store Info",
                                                            JOptionPane.INFORMATION_MESSAGE);
                                                } else { // store not exist
                                                    JOptionPane.showMessageDialog(null,
                                                            "Store not exist!", "Store " +
                                                                    "Info", JOptionPane.ERROR_MESSAGE);
                                                    JOptionPane.showMessageDialog(null,
                                                            storeNames, "Your Stores",
                                                            JOptionPane.INFORMATION_MESSAGE);
                                                }
                                            } while (storeInfo == null);

                                            boolean modified = false;
                                            // Ask for productName, description, quantity, price.
                                            do {
                                                // Send productName.
                                                String productName = JOptionPane.showInputDialog(
                                                        null, "Please enter" +
                                                                " the product name.", "Edit Product",
                                                        JOptionPane.QUESTION_MESSAGE);
                                                String description = JOptionPane.showInputDialog(
                                                        null, "Please enter " +
                                                                "the product description.", "Create Product",
                                                        JOptionPane.QUESTION_MESSAGE);
                                                String quantityString = JOptionPane.showInputDialog(
                                                        null, "Please " +
                                                                "enter the quantity of the product.",
                                                        "Create Product",
                                                        JOptionPane.QUESTION_MESSAGE);
                                                String priceString = JOptionPane.showInputDialog(
                                                        null, "Please enter " +
                                                                "the price of the product.", "Create Product",
                                                        JOptionPane.QUESTION_MESSAGE);
                                                String[] productInfo = {productName, description, quantityString,
                                                        priceString};
                                                writer.writeObject(productInfo);
                                                writer.flush();
                                                boolean actionQuit = reader.readBoolean();
                                                if (actionQuit) {
                                                    continue sellerMenu;
                                                }
                                                boolean invalid = reader.readBoolean();
                                                if (invalid) {
                                                    JOptionPane.showMessageDialog(null,
                                                            "Invalid input! Failed to " +
                                                                    "edit product.", "Edit Product",
                                                            JOptionPane.ERROR_MESSAGE);
                                                } else {
                                                    modified = reader.readBoolean();
                                                    if (modified) {
                                                        JOptionPane.showMessageDialog(null,
                                                                "Product Edited!", "Edit " +
                                                                        "Product", JOptionPane.INFORMATION_MESSAGE);
                                                    } else {
                                                        JOptionPane.showMessageDialog(null,
                                                                "Failed to edit product. " +
                                                                        "Product not exist!", "Edit Product",
                                                                JOptionPane.ERROR_MESSAGE);
                                                        JOptionPane.showMessageDialog(null,
                                                                storeInfo, "Store Info",
                                                                JOptionPane.INFORMATION_MESSAGE);
                                                    }
                                                }
                                            } while (!modified);

                                            sellerCheck = true;

                                        } else if (deleteProduct.isSelected()) {
                                            // delete Product
                                            sellerInput.add("deleteProduct");
                                            writer.writeObject(sellerInput);
                                            writer.flush();

                                            // Check if the seller has a store.
                                            String storeNames = (String) reader.readObject();
                                            // If seller does not have a store, return to the seller menu.
                                            if (storeNames == null) {
                                                JOptionPane.showMessageDialog(null,
                                                        "You don't have a store yet.",
                                                        "Delete Product", JOptionPane.ERROR_MESSAGE);
                                                continue sellerMenu;
                                            } else {
                                                JOptionPane.showMessageDialog(null,
                                                        storeNames, "Your Stores",
                                                        JOptionPane.INFORMATION_MESSAGE);
                                            }

                                            // Ask for storeName and check its existence.
                                            String storeInfo;
                                            do {
                                                // Send storeName.
                                                String storeName = JOptionPane.showInputDialog(
                                                        null, "Please enter " +
                                                                "the store name.", "Delete Product",
                                                        JOptionPane.QUESTION_MESSAGE);
                                                writer.writeObject(storeName);
                                                writer.flush();
                                                if (storeName == null) { // user wants to quit
                                                    continue sellerMenu;
                                                }
                                                // Receive storeInfo.
                                                storeInfo = (String) reader.readObject();
                                                if (storeInfo != null) { // store exist, display store info
                                                    boolean empty = reader.readBoolean();
                                                    if (empty) {
                                                        JOptionPane.showMessageDialog(null,
                                                                "Store empty! Cannot " +
                                                                        "delete a product from it.",
                                                                "Delete Product",
                                                                JOptionPane.ERROR_MESSAGE);
                                                        storeInfo = null;
                                                    } else {
                                                        JOptionPane.showMessageDialog(null,
                                                                storeInfo, "Store Info",
                                                                JOptionPane.INFORMATION_MESSAGE);
                                                    }
                                                } else { // store not exist
                                                    JOptionPane.showMessageDialog(null,
                                                            "Store not exist!", "Store " +
                                                                    "Info", JOptionPane.ERROR_MESSAGE);
                                                    JOptionPane.showMessageDialog(null,
                                                            storeNames, "Your Stores",
                                                            JOptionPane.INFORMATION_MESSAGE);
                                                }
                                            } while (storeInfo == null);

                                            boolean deleted = false;
                                            // Ask for productName.
                                            do {
                                                // Send productName.
                                                String productName = JOptionPane.showInputDialog(
                                                        null, "Please enter" +
                                                                " the product name.", "Delete Product",
                                                        JOptionPane.QUESTION_MESSAGE);
                                                writer.writeObject(productName);
                                                writer.flush();
                                                boolean actionQuit = reader.readBoolean();
                                                if (actionQuit) {
                                                    continue sellerMenu;
                                                }
                                                // Delete the product.
                                                deleted = reader.readBoolean();
                                                if (deleted) {
                                                    JOptionPane.showMessageDialog(null,
                                                            "Product deleted!", "Delete " +
                                                                    "Product", JOptionPane.INFORMATION_MESSAGE);
                                                } else {
                                                    JOptionPane.showMessageDialog(null,
                                                            "Failed to delete product. " +
                                                                    "Product not exist!", "Delete Product",
                                                            JOptionPane.ERROR_MESSAGE);
                                                    JOptionPane.showMessageDialog(null,
                                                            storeInfo, "Store Info",
                                                            JOptionPane.INFORMATION_MESSAGE);
                                                }
                                            } while (!deleted);

                                            sellerCheck = true;

                                        } else if (viewSells.isSelected()) {
                                            // view sells
                                            sellerInput.add("viewSells");
                                            writer.writeObject(sellerInput);
                                            writer.flush();

                                            // Display all stores the seller has (currently or in the past).
                                            String storeNames = (String) reader.readObject();
                                            if (storeNames == null) {
                                                JOptionPane.showMessageDialog(null,
                                                        "No history! No store has been " +
                                                                "created.", "View Sales",
                                                        JOptionPane.ERROR_MESSAGE);
                                                continue sellerMenu;
                                            } else {
                                                JOptionPane.showMessageDialog(null,
                                                        storeNames, "Your Stores",
                                                        JOptionPane.INFORMATION_MESSAGE);
                                            }
                                            String sales = "";
                                            do {
                                                // Send storeName.
                                                String storeName = JOptionPane.showInputDialog(
                                                        null, "Please enter " +
                                                                "the store name.", "View Sales",
                                                        JOptionPane.QUESTION_MESSAGE);
                                                writer.writeObject(storeName);
                                                writer.flush();
                                                // User can quit to the sellerMenu.
                                                if (storeName == null) {
                                                    continue sellerMenu;
                                                }
                                                // Receive sales.
                                                sales = (String) reader.readObject();
                                                if (sales.equals("")) {
                                                    JOptionPane.showMessageDialog(null,
                                                            "Store not found! Please " +
                                                                    "enter a valid store name.", "View Sales",
                                                            JOptionPane.ERROR_MESSAGE);
                                                    JOptionPane.showMessageDialog(null,
                                                            storeNames, "Your Stores",
                                                            JOptionPane.INFORMATION_MESSAGE);
                                                } else {
                                                    JOptionPane.showMessageDialog(null,
                                                            sales, "View Sales",
                                                            JOptionPane.INFORMATION_MESSAGE);
                                                }
                                            } while (sales.equals(""));


                                            sellerCheck = true;

                                        } else if (export.isSelected()) {
                                            // export
                                            sellerInput.add("export");
                                            writer.writeObject(sellerInput);
                                            writer.flush();

                                            boolean successful = false;
                                            do {
                                                // Send the path.
                                                String path = JOptionPane.showInputDialog(null,
                                                        "Please enter the " +
                                                                "file path.", "Export to CSV",
                                                        JOptionPane.QUESTION_MESSAGE);
                                                writer.writeObject(path);
                                                writer.flush();
                                                if (path == null) { // user wants to quit
                                                    continue sellerMenu;
                                                }
                                                // Receive whether successful.
                                                successful = reader.readBoolean();
                                                if (!successful) {
                                                    JOptionPane.showMessageDialog(null,
                                                            "Failed to export. Please " +
                                                                    "try again.", "Export to CSV",
                                                            JOptionPane.ERROR_MESSAGE);
                                                }
                                            } while (!successful);

                                            sellerCheck = true;

                                        } else if (importButton.isSelected()) {
                                            // import
                                            sellerInput.add("import");
                                            writer.writeObject(sellerInput);
                                            writer.flush();

                                            int successful = 0;
                                            do {
                                                // Send the path.
                                                String path = JOptionPane.showInputDialog(null,
                                                        "Please enter the " +
                                                                "file path.", "Import to CSV",
                                                        JOptionPane.QUESTION_MESSAGE);
                                                writer.writeObject(path);
                                                writer.flush();
                                                if (path == null) { // user wants to quit
                                                    continue sellerMenu;
                                                }
                                                // Receive whether successful.
                                                successful = (int) reader.readObject();
                                                if (successful == -1) {
                                                    JOptionPane.showMessageDialog(null,
                                                            "Invalid format when " +
                                                                    "importing csv!. Please try again.",
                                                            "Import to " +
                                                                    "CSV",
                                                            JOptionPane.ERROR_MESSAGE);
                                                } else if (successful == -2) {
                                                    JOptionPane.showMessageDialog(null,
                                                            "Invalid file name or file " +
                                                                    "not found! Please try again.",
                                                            "Import to CSV",
                                                            JOptionPane.ERROR_MESSAGE);
                                                }
                                            } while (successful != 1);

                                            sellerCheck = true;

                                        } else if (viewDashboard.isSelected()) {
                                            // view dashboard
                                            sellerInput.add("viewDashboard");
                                            writer.writeObject(sellerInput);
                                            writer.flush();

                                            // Send if sort in reverse order.
                                            int reverse = JOptionPane.showConfirmDialog(null,
                                                    "Do you want the " +
                                                            "records sorted from high to low " +
                                                            "(default from low to high)?",
                                                    "View Dashboard", JOptionPane.YES_NO_OPTION);
                                            writer.writeBoolean(reverse == JOptionPane.YES_OPTION);
                                            writer.flush();
                                            // Receive dashboard.
                                            String dashboard = (String) reader.readObject();
                                            JOptionPane.showMessageDialog(null, dashboard,
                                                    "View Dashboard", JOptionPane.INFORMATION_MESSAGE);

                                            sellerCheck = true;

                                        }
                                    }
                                    firstCheck = true;
                                } while (sellerCheck);

                            } else if (checkLogin.equals("customer")) {
                                JOptionPane.showMessageDialog(null, "Logged In!",
                                        "Log in", JOptionPane.INFORMATION_MESSAGE);

                                boolean customerCheck = false;

                                do {
                                    ButtonGroup customerGroup = new ButtonGroup();

                                    JRadioButton viewMarket = new JRadioButton("View Market");
                                    JRadioButton search = new JRadioButton("Search");
                                    JRadioButton viewPurchaseHistory = new JRadioButton("View Purchase History");
                                    JRadioButton export = new JRadioButton("Export");
                                    JRadioButton viewDashboard = new JRadioButton("View Dashboard");
                                    JRadioButton quit = new JRadioButton("Quit");

                                    customerGroup.add(viewMarket);
                                    customerGroup.add(search);
                                    customerGroup.add(viewPurchaseHistory);
                                    customerGroup.add(export);
                                    customerGroup.add(viewDashboard);
                                    customerGroup.add(quit);

                                    Object[] fieldsOne = {viewMarket, search, viewPurchaseHistory, export,
                                            viewDashboard, quit};

                                    int sellerMenu = JOptionPane.showConfirmDialog(null,
                                            fieldsOne, "Customer Menu", JOptionPane.OK_CANCEL_OPTION);

                                    ArrayList<String> customerInput = new ArrayList<>();

                                    //  cancel = 2, x = -1, others = 0
                                    if (sellerMenu == 2 || sellerMenu == -1 || quit.isSelected()) {
                                        customerCheck = false;
                                        firstCheck = true;
                                        customerInput.add("exit");
                                        writer.writeObject(customerInput);
                                        writer.flush();

                                    } else {
                                        // check user's choice
                                        if (viewMarket.isSelected()) {
                                            // view market
                                            customerInput.add("viewMarket");
                                            writer.writeObject(customerInput);
                                            writer.flush();

                                            // 检查是否存在任意商品
                                            boolean hasProduct = reader.readBoolean();

                                            if (hasProduct) {
                                                ButtonGroup sortGroup = new ButtonGroup();

                                                JRadioButton quantityHtoL =
                                                        new JRadioButton("1. Quantity High - Low");
                                                JRadioButton quantityLtoH =
                                                        new JRadioButton("2. Quantity Low - High");
                                                JRadioButton priceHtoL = new JRadioButton("3. Price High - Low");
                                                JRadioButton priceLtoH = new JRadioButton("4. Price Low - High");
                                                JRadioButton no = new JRadioButton("5. No");

                                                sortGroup.add(quantityHtoL);
                                                sortGroup.add(quantityLtoH);
                                                sortGroup.add(priceHtoL);
                                                sortGroup.add(priceLtoH);
                                                sortGroup.add(no);


                                                Object[] fieldsView = {"Do you want to sort?",
                                                        quantityHtoL, quantityLtoH, priceHtoL, priceLtoH, no};

                                                int sort = JOptionPane.showConfirmDialog(null,
                                                        fieldsView, "Market", JOptionPane.OK_CANCEL_OPTION);

                                                ArrayList<String> view = new ArrayList<>();


                                                if (sort == -1 || sort == 2) {
                                                    view.add("quit");
                                                    writer.writeObject(view);
                                                    writer.flush();
                                                } else if (quantityHtoL.isSelected()) {
                                                    view.add("quantityHtoL");
                                                    writer.writeObject(view);
                                                    writer.flush();
                                                } else if (quantityLtoH.isSelected()) {
                                                    view.add("quantityLtoH");
                                                    writer.writeObject(view);
                                                    writer.flush();
                                                } else if (priceHtoL.isSelected()) {
                                                    view.add("priceHtoL");
                                                    writer.writeObject(view);
                                                    writer.flush();
                                                } else if (priceLtoH.isSelected()) {
                                                    view.add("priceLtoH");
                                                    writer.writeObject(view);
                                                    writer.flush();
                                                } else if (no.isSelected()) {
                                                    view.add("no");
                                                    writer.writeObject(view);
                                                    writer.flush();
                                                }
                                                // buttonSelected, totalProducts, how many products
                                                view = (ArrayList<String>) reader.readObject(); // 3 elements

                                                String showProduct = view.get(1);


                                                boolean checkProduct = false;
                                                do {

                                                    try {
                                                        String product = JOptionPane.showInputDialog(
                                                                null,
                                                                "Type a number to select a product:\n"
                                                                        + showProduct, "Market Place",
                                                                JOptionPane.QUESTION_MESSAGE);

                                                        if (product != null) {
                                                            int index = Integer.parseInt(product);
                                                            if (index > 0 && index <= Integer.parseInt(view.get(2))) {
                                                                // see if input is valid

                                                                // four elements
                                                                // buttonSelected, totalProducts,
                                                                // how many products, chosenProduct
                                                                view.add(product);
                                                                writer.writeObject(view);
                                                                writer.flush();

                                                                // receive description from server
                                                                view = (ArrayList<String>) reader.readObject();


                                                                boolean checkPurchase = false;

                                                                do {
                                                                    String purchase = JOptionPane.showInputDialog(
                                                                            null, view.get(4) +
                                                                                    "\n\nHow many do you " +
                                                                                    "want to buy?",
                                                                            "Product Detail",
                                                                            JOptionPane.QUESTION_MESSAGE);

                                                                    try {

                                                                        if (purchase != null) {

                                                                            int number = Integer.parseInt(purchase);
                                                                            // write purchase quantity
                                                                            writer.writeInt(number);
                                                                            writer.flush();

                                                                            boolean quantityCheck =
                                                                                    reader.readBoolean();

                                                                            if (!quantityCheck) {

                                                                                JOptionPane.showMessageDialog(
                                                                                        null,
                                                                                        "Invalid input! " +
                                                                                                "Please try again.",
                                                                                        "Error",
                                                                                        JOptionPane.ERROR_MESSAGE);
                                                                                checkPurchase = false;
                                                                            } else {
                                                                                JOptionPane.showMessageDialog(
                                                                                        null,
                                                                                        "Item Purchased!",
                                                                                        "Market",
                                                                                        JOptionPane.PLAIN_MESSAGE);
                                                                                checkPurchase = false;
                                                                            }
                                                                        } else {
                                                                            checkPurchase = false;
                                                                            view.add("back");
                                                                            writer.writeObject(view);
                                                                            writer.flush();
                                                                        }

                                                                    } catch (NumberFormatException e) {
                                                                        JOptionPane.showMessageDialog(
                                                                                null,
                                                                                "Invalid input! " +
                                                                                        "Please try again.",
                                                                                "Error",
                                                                                JOptionPane.ERROR_MESSAGE);
                                                                        checkPurchase = true;
                                                                    }

                                                                } while (checkPurchase);
                                                                checkProduct = false;
                                                            } else {
                                                                // if input not a valid number
                                                                JOptionPane.showMessageDialog(null,
                                                                        "Invalid input! Please try again.",
                                                                        "Error", JOptionPane.ERROR_MESSAGE);

                                                                view.add("back");
                                                                writer.writeObject(view);
                                                                writer.flush();

                                                                checkProduct = false;
                                                            }
                                                        } else {
                                                            // back to customer menu
                                                            checkProduct = false;
                                                            view.add("back");
                                                            writer.writeObject(view);
                                                            writer.flush();
                                                        }
                                                    } catch (NumberFormatException e) {
                                                        // if input is not a number
                                                        JOptionPane.showMessageDialog(null,
                                                                "Invalid input! Please try again.",
                                                                "Error", JOptionPane.ERROR_MESSAGE);
                                                        checkProduct = true;
                                                    }
                                                } while (checkProduct);
                                                // }

                                            } else {

                                                // if there is no product in market
                                                JOptionPane.showMessageDialog(null,
                                                        "No product, please return!",
                                                        "Market Error!", JOptionPane.ERROR_MESSAGE);
                                            }

                                            customerCheck = true;

                                        } else if (search.isSelected()) {
                                            // search
                                            customerInput.add("search");

                                            writer.writeObject(customerInput);
                                            writer.flush();

                                            ButtonGroup searchGroup = new ButtonGroup();

                                            JRadioButton name = new JRadioButton("1. By product's name");
                                            JRadioButton store = new JRadioButton("2. By store's name");
                                            JRadioButton description = new JRadioButton("3. By description");

                                            searchGroup.add(name);
                                            searchGroup.add(store);
                                            searchGroup.add(description);

                                            ArrayList<String> searchList = new ArrayList<>();

                                            Object[] searchFields = {name, store, description};

                                            int searchMenu = JOptionPane.showConfirmDialog(null,
                                                    searchFields, "Search Menu", JOptionPane.OK_CANCEL_OPTION);


                                            if (searchMenu == 2 || searchMenu == -1) {
                                                searchList.add("quit");
                                                writer.writeObject(searchList);
                                                writer.flush();
                                            } else if (name.isSelected()) {
                                                // search by name
                                                String purchase = JOptionPane.showInputDialog(
                                                        null, " Please input a product's name",
                                                        "Search Menu", JOptionPane.QUESTION_MESSAGE);

                                                searchList.add("name");
                                                searchList.add(purchase);
                                                writer.writeObject(searchList);
                                                writer.flush();

                                                // receive the result from server
                                                searchList = (ArrayList<String>) reader.readObject();

                                                if (searchList.get(2).equals("Item not Found!")) {
                                                    JOptionPane.showMessageDialog(null,
                                                            "Item not found!",
                                                            "Search Menu!", JOptionPane.PLAIN_MESSAGE);
                                                } else {
                                                    JOptionPane.showMessageDialog(null,
                                                            searchList.get(2),
                                                            "Search Menu!", JOptionPane.PLAIN_MESSAGE);
                                                }


                                            } else if (store.isSelected()) {
                                                // search by store name
                                                String purchase = JOptionPane.showInputDialog(
                                                        null, " Please input a store's name",
                                                        "Search Menu", JOptionPane.QUESTION_MESSAGE);

                                                searchList.add("store");
                                                searchList.add(purchase);
                                                writer.writeObject(searchList);
                                                writer.flush();

                                                // receive the result from server
                                                searchList = (ArrayList<String>) reader.readObject();

                                                if (searchList.get(2).equals("No such store Found!")) {
                                                    JOptionPane.showMessageDialog(null,
                                                            "Store not found!",
                                                            "Search Menu!", JOptionPane.PLAIN_MESSAGE);
                                                } else {
                                                    JOptionPane.showMessageDialog(null,
                                                            searchList.get(2),
                                                            "Search Menu!", JOptionPane.PLAIN_MESSAGE);
                                                }

                                            } else if (description.isSelected()) {
                                                // search by description
                                                String purchase = JOptionPane.showInputDialog(
                                                        null, " Please input description",
                                                        "Search Menu", JOptionPane.QUESTION_MESSAGE);

                                                searchList.add("description");
                                                searchList.add(purchase);
                                                writer.writeObject(searchList);
                                                writer.flush();

                                                // receive the result from server
                                                searchList = (ArrayList<String>) reader.readObject();

                                                if (searchList.get(2).equals("No such store Found!")) {
                                                    JOptionPane.showMessageDialog(null,
                                                            "Item not Found!",
                                                            "Search Menu", JOptionPane.PLAIN_MESSAGE);
                                                } else {
                                                    JOptionPane.showMessageDialog(null,
                                                            searchList.get(2),
                                                            "Search Menu", JOptionPane.PLAIN_MESSAGE);
                                                }
                                            }

                                            customerCheck = true;

                                        } else if (viewPurchaseHistory.isSelected()) {
                                            // delete Product
                                            customerInput.add("viewPurchaseHistory");
                                            writer.writeObject(customerInput);
                                            writer.flush();

                                            ArrayList<String> viewHistory = (ArrayList<String>) reader.readObject();

                                            if (viewHistory.get(0).equals("") || viewHistory.get(0) == null) {
                                                // no purchase history
                                                JOptionPane.showMessageDialog(null,
                                                        "You haven't purchased any item!",
                                                        "View Purchase History", JOptionPane.ERROR_MESSAGE);
                                            } else {
                                                //display purchase history
                                                JOptionPane.showMessageDialog(null,
                                                        viewHistory.get(0),
                                                        "View Purchase History", JOptionPane.PLAIN_MESSAGE);
                                            }
                                            customerCheck = true;

                                        } else if (export.isSelected()) {
                                            // export
                                            customerInput.add("export");
                                            writer.writeObject(customerInput);
                                            writer.flush();

                                            ArrayList<String> viewHistory = (ArrayList<String>) reader.readObject();

                                            if (viewHistory.get(0).equals("") || viewHistory.get(0) == null) {
                                                // no purchase history
                                                JOptionPane.showMessageDialog(null,
                                                        "You haven't purchased any item!",
                                                        "View Purchase History", JOptionPane.ERROR_MESSAGE);
                                                writer.writeBoolean(false);
                                            } else {
                                                //display purchase history
                                                JOptionPane.showMessageDialog(null,
                                                        "CSV file created!",
                                                        "View Purchase History", JOptionPane.PLAIN_MESSAGE);
                                                writer.writeBoolean(true);
                                            }

                                            customerCheck = true;

                                        } else if (viewDashboard.isSelected()) {
                                            // view dashboard
                                            customerInput.add("viewDashboard");
                                            writer.writeObject(customerInput);
                                            writer.flush();

                                            int option = JOptionPane.showConfirmDialog(null,
                                                    "Do you want to sort in reverse order? " +
                                                            "(Default low to high)", "Dashboard",
                                                    JOptionPane.YES_NO_OPTION);

                                            ArrayList<String> dashBoard = new ArrayList<>();

                                            // yes = 0, no = 1, x = -1
                                            if (option == 0) {
                                                dashBoard.add("true");
                                                writer.writeObject(dashBoard);
                                                writer.flush();
                                            } else if (option == 1) {
                                                dashBoard.add("false");
                                                writer.writeObject(dashBoard);
                                                writer.flush();
                                            } else if (option == -1) {
                                                dashBoard.add("quit");
                                                writer.writeObject(dashBoard);
                                                writer.flush();
                                            }

                                            dashBoard = (ArrayList<String>) reader.readObject();
                                            if (!dashBoard.get(1).equals("no")) {
                                                JOptionPane.showMessageDialog(null,
                                                        dashBoard.get(1),
                                                        "Dash Board", JOptionPane.PLAIN_MESSAGE);
                                            }
                                            customerCheck = true;

                                        }
                                    }
                                    firstCheck = true;
                                } while (customerCheck);
                            }
                        }

                    } while (loginCheck);
                }

            } while (firstCheck);

            writer.close();
            reader.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
