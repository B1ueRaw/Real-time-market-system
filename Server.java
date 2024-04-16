import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server implements Runnable {
    Socket socket; // creating socket object

    public Server(Socket socket) {
        this.socket = socket;
    }

    /**
     * Writes the username, password, and type the user inputted in the Sign Up GUI into a
     * 'users.txt' text files.
     *
     * @param username username of user
     * @param password password of user
     * @param type     if the user is a customer or seller
     */
    public static void signUp(String username, String password, String type) {
        try {
            FileOutputStream fos = new FileOutputStream("users.txt", true);
            PrintWriter pw = new PrintWriter(fos);
            pw.println(username);
            pw.println(password);
            pw.println(type);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sees if the username user inputs in the Sign Up GUI is the same as any of the usernames
     * already present in the 'users.txt' text file.
     *
     * @param username checks if username is taken or not
     * @return A boolean called exist to show if username exists or not
     */
    public static boolean checkUsername(String username) {
        boolean exist = false;
        File user = new File("users.txt");
        int i = 1;

        try (BufferedReader bfr = new BufferedReader(new FileReader(user))) {
            String line = bfr.readLine();

            if (line != null) {
                if (line.equals(username)) {
                    exist = true;
                }
            }

            while (line != null) {
                line = bfr.readLine();

                // check if this username be taken or not
                if (line != null) {

                    if (line.equals(username) && i % 3 == 0) {
                        exist = true;
                    }
                    i++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return exist;
    }

    /**
     * Checks if the username and password match the information stored in 'users.txt'
     * text file. If it does not, then an error is shown in the Client class showing that
     * the username & password are not valid.
     *
     * @param username username inputted in the login GUI
     * @param password password inputted in the login GUI
     * @return A type(customer/seller) to direct them to their respective menus when login is valid.
     */
    public static String checkLogin(String username, String password) {
        String type = "Invalid username and/or password!";
        File user = new File("users.txt");

        // read each line of user txt file
        try (BufferedReader bfr = new BufferedReader(new FileReader(user))) {
            String line = bfr.readLine();

            while (line != null) {
                String checkName = "";
                String checkPassword = "";
                String accountType = "";

                // input username to match
                if (line != null) {
                    checkName = line;
                }
                line = bfr.readLine();

                // input password to match
                if (line != null) {
                    checkPassword = line;
                }
                line = bfr.readLine();

                // input account type to return
                if (line != null) {
                    accountType = line;
                }
                line = bfr.readLine();

                // check if match
                if (checkName.equals(username) && checkPassword.equals(password)) {
                    type = accountType;
                    return type;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return type;
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4232);

            // accept socket and start a new thread
            while (true) {
                Socket socket = serverSocket.accept();
                Server server = new Server(socket);
                new Thread(server).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        boolean firstCheck = true;
        ObjectInputStream reader;
        ObjectOutputStream writer;

        try {
            // socket input and output objects
            reader = new ObjectInputStream(socket.getInputStream());
            writer = new ObjectOutputStream(socket.getOutputStream());

            do {
                ArrayList<String> loginInput = (ArrayList<String>) reader.readObject();

                // if client wrote signup
                if (loginInput.get(0).equals("signup")) {

                    boolean signCheck = false;

                    do {
                        ArrayList<String> signupInfo = (ArrayList<String>) reader.readObject();
                        boolean exist;
                        if (signupInfo.get(0).equals("exit")) {
                            signCheck = false;
                            firstCheck = true;

                            // if client wrote login
                        } else if (signupInfo.get(0).equals("signup")) {
                            // account type, username, password inputted
                            // check if username is valid
                            // signupInfo [signup, username, password, customer]

                            exist = checkUsername(signupInfo.get(1));

                            if (exist) {
                                // username has been taken
                                signCheck = true;
                                firstCheck = false;
                                writer.writeBoolean(false);
                                writer.flush();


                            } else {
                                // username is valid
                                signUp(signupInfo.get(1), signupInfo.get(2), signupInfo.get(3));

                                signCheck = false;
                                firstCheck = true;
                                writer.writeBoolean(true);
                                writer.flush();


                            }
                        }

                    } while (signCheck);

                } else if (loginInput.get(0).equals("login")) {
                    boolean loginCheck = false;
                    String isValid;
                    do {
                        ArrayList<String> loginInfo = (ArrayList<String>) reader.readObject();


                        if (loginInfo.get(0).equals("exit")) {
                            loginCheck = false;
                            firstCheck = true;
                        } else if (loginInfo.get(0).equals("login")) {
                            // [login, username, password]
                            // (String) isValid: Invalid username and/or password! || account type
                            isValid = checkLogin(loginInfo.get(1), loginInfo.get(2));

                            writer.writeObject(isValid);
                            writer.flush();

                            if (isValid.equals("seller")) {
                                loginCheck = false;
                                boolean sellerCheck = false;
                                // Create a seller object
                                Seller seller = new Seller(loginInfo.get(1));

                                sellerMenu:
                                do {
                                    ArrayList<String> sellerInput = (ArrayList<String>) reader.readObject();
                                    if (sellerInput.get(0).equals("exit")) {

                                        sellerCheck = false;

                                    } else if (sellerInput.get(0).equals("createProduct")) {

                                        String[] productInfo = (String[]) reader.readObject();
                                        // Check if any of the info is null (i.e. the user wants to quit)
                                        for (String info : productInfo) {
                                            if (info == null) {
                                                writer.writeBoolean(true);
                                                writer.flush();
                                                continue sellerMenu;
                                            }
                                        }
                                        writer.writeBoolean(false);
                                        writer.flush();
                                        try {
                                            int quantity = Integer.parseInt(productInfo[3]);
                                            double price = Double.parseDouble(productInfo[4]);
                                            seller.createProduct(productInfo[0], productInfo[1], productInfo[2],
                                                    quantity, price, false);
                                            writer.writeBoolean(false);
                                            writer.flush();
                                        } catch (NumberFormatException formatException) {
                                            writer.writeBoolean(true);
                                            writer.flush();
                                        }

                                        // create product

                                        sellerCheck = true;

                                    } else if (sellerInput.get(0).equals("editProduct")) {

                                        // Check if the seller owns a store. If the seller does, display all storeNames.
                                        String storeNames = seller.haveStore();
                                        writer.writeObject(storeNames);
                                        writer.flush();

                                        Store store;
                                        do {
                                            // Receive storeName.
                                            String storeName = (String) reader.readObject();
                                            store = seller.getStore(storeName);
                                            if (storeName == null) { // user wants to quit
                                                continue sellerMenu;
                                            }
                                            // Send information of the store.
                                            if (store == null) {
                                                writer.writeObject(null);
                                            } else {
                                                writer.writeObject(store.toString());
                                            }
                                            writer.flush();
                                        } while (store == null);

                                        boolean modified = false;
                                        do {
                                            // Receive productName, description, quantity, price.
                                            String[] productInfo = (String[]) reader.readObject();
                                            // Check if any of the info is null (i.e. the user wants to quit)
                                            for (String info : productInfo) {
                                                if (info == null) {
                                                    writer.writeBoolean(true);
                                                    writer.flush();
                                                    continue sellerMenu;
                                                }
                                            }
                                            writer.writeBoolean(false);
                                            writer.flush();
                                            try {
                                                int quantity = Integer.parseInt(productInfo[2]);
                                                double price = Double.parseDouble(productInfo[3]);
                                                store.modifyProduct(productInfo[0], productInfo[1], quantity, price);
                                                writer.writeBoolean(false); // invalid = false
                                                writer.flush();
                                                // Edit the product.
                                                modified = store.modifyProduct(productInfo[0], productInfo[1],
                                                        quantity, price);
                                                writer.writeBoolean(modified);
                                                writer.flush();
                                            } catch (NumberFormatException formatException) {
                                                writer.writeBoolean(true); // invalid = true
                                                writer.flush();
                                            }
                                        } while (!modified);

                                        // edit Product

                                        sellerCheck = true;

                                    } else if (sellerInput.get(0).equals("deleteProduct")) {

                                        // Check if the seller owns a store. If the seller does, display all storeNames.
                                        String storeNames = seller.haveStore();
                                        writer.writeObject(storeNames);
                                        writer.flush();

                                        Store store;
                                        do {
                                            // Receive storeName.
                                            String storeName = (String) reader.readObject();
                                            store = seller.getStore(storeName);
                                            if (storeName == null) { // user wants to quit
                                                continue sellerMenu;
                                            }
                                            // Send information of the store.
                                            if (store == null) { // store not exist
                                                writer.writeObject(null);
                                                writer.flush();
                                            } else { // store exist
                                                // Send storeInfo to client.
                                                writer.writeObject(store.toString());
                                                writer.flush();
                                                // Check if the store is empty.
                                                boolean empty = store.isEmpty();
                                                writer.writeBoolean(empty);
                                                writer.flush();
                                                if (empty) {
                                                    store = null;
                                                }
                                            }
                                        } while (store == null);

                                        boolean deleted = false;
                                        do {
                                            // Receive productName.
                                            String productName = (String) reader.readObject();
                                            // Check if productName is null (i.e. the user wants to quit)
                                            if (productName == null) {
                                                writer.writeBoolean(true);
                                                writer.flush();
                                                continue sellerMenu;
                                            }
                                            writer.writeBoolean(false);
                                            writer.flush();
                                            // Delete the product.
                                            deleted = store.deleteProduct(productName);
                                            writer.writeBoolean(deleted);
                                            writer.flush();
                                        } while (!deleted);

                                        // delete Product

                                        sellerCheck = true;

                                    } else if (sellerInput.get(0).equals("viewSells")) {

                                        // Display all stores the seller has (currently or in the past).
                                        File totalStores = new File(loginInfo.get(1) + "_totalStores.txt");
                                        if (!totalStores.exists()) {
                                            writer.writeObject(null);
                                        } else {
                                            StringBuilder builder = new StringBuilder();
                                            try (BufferedReader localReader =
                                                         new BufferedReader(new FileReader(totalStores))) {
                                                String line;
                                                while ((line = localReader.readLine()) != null) {
                                                    builder.append(line);
                                                    builder.append('\n');
                                                }
                                            }
                                            writer.writeObject(builder.toString());
                                        }
                                        writer.flush();
                                        // Receive the storeName.
                                        // Display sales.
                                        String sales = "";
                                        do {
                                            // Receive storeName.
                                            String storeName = (String) reader.readObject();
                                            // Send sales.
                                            sales = seller.viewSales(storeName);
                                            writer.writeObject(sales);
                                            writer.flush();
                                        } while (sales.equals(""));

                                        // view Sells
                                        sellerCheck = true;

                                    } else if (sellerInput.get(0).equals("export")) {

                                        boolean successful = false;
                                        do {
                                            // Receive path.
                                            String path = (String) reader.readObject();
                                            if (path == null) {
                                                continue sellerMenu;
                                            }
                                            successful = seller.exportCSV(path);
                                            // Send whether exported successfully.
                                            writer.writeBoolean(successful);
                                            writer.flush();
                                        } while (!successful);

                                        // export
                                        sellerCheck = true;

                                    } else if (sellerInput.get(0).equals("import")) {

                                        int successful = 0;
                                        do {
                                            // Receive path.
                                            String path = (String) reader.readObject();
                                            if (path == null) {
                                                continue sellerMenu;
                                            }
                                            successful = seller.importCSV(path);
                                            // Send whether imported successfully.
                                            writer.writeObject(successful);
                                            writer.flush();
                                        } while (successful != 1);

                                        // import
                                        sellerCheck = true;

                                    } else if (sellerInput.get(0).equals("viewDashboard")) {

                                        String dashboard = "";
                                        do {
                                            // Receive whether sort in reverse order.
                                            boolean reverse = reader.readBoolean();
                                            // Display dashboard.
                                            dashboard = seller.createDashboard(reverse);
                                            writer.writeObject(dashboard);
                                            writer.flush();
                                        } while (dashboard.equals(""));

                                        // viewDashboard
                                        sellerCheck = true;

                                    }
                                } while (sellerCheck);

                            } else if ((isValid.equals("customer"))) {
                                loginCheck = false;
                                boolean customerCheck = false;
                                do {
                                    Customer customer = new Customer(loginInfo.get(1));
                                    ArrayList<String> customerInput = (ArrayList<String>) reader.readObject();
                                    if (customerInput.get(0).equals("exit")) {
                                        customerCheck = false;

                                    } else if (customerInput.get(0).equals("viewMarket")) {
                                        // view Market

                                        if (customer.countProductNumber() > 0) {


                                            writer.writeBoolean(true);
                                            writer.flush();

                                            ArrayList<String> view = (ArrayList<String>) reader.readObject();
                                            ArrayList<String> sort = new ArrayList<String>();

                                            if (view.get(0).equals("quantityHtoL")) {
                                                customer.sortMarketByQuantity(true);
                                            } else if (view.get(0).equals("quantityLtoH")) {
                                                customer.sortMarketByQuantity(false);
                                            } else if (view.get(0).equals("priceHtoL")) {
                                                customer.sortMarketByPrice(true);
                                            } else if (view.get(0).equals("priceLtoH")) {
                                                customer.sortMarketByPrice(false);
                                            } else if (view.get(0).equals("no")) {

                                            }

                                            view.add(customer.showDefaultList());
                                            view.add(String.valueOf(customer.countProductNumber())); // 3 elements
                                            writer.writeObject(view);
                                            writer.flush();

                                            boolean checkProduct = false;
                                            do {

                                                view = (ArrayList<String>) reader.readObject();
                                                if (view.get(3).equals("back")) {
                                                    checkProduct = false;

                                                } else if (view.get(3).equals("true")) {

                                                    checkProduct = false;
                                                } else {
                                                    // write description to client
                                                    view.add(customer.showProductInfo(
                                                            Integer.parseInt(view.get(3))));
                                                    writer.writeObject(view);
                                                    writer.flush();

                                                    boolean validQuantity = false;

                                                    do {
                                                        int quantity = reader.readInt();
                                                        boolean checkQuantity =
                                                                customer.checkQuantity((Integer.parseInt(
                                                                        view.get(3))), quantity);
                                                        writer.writeBoolean(checkQuantity);
                                                        writer.flush();
                                                        if (checkQuantity) {
                                                            customer.purchaseItem((Integer.parseInt(view.get(3)))
                                                                    , quantity);
                                                            validQuantity = false;
                                                        } else {
                                                            validQuantity = false;
                                                        }

                                                    } while (validQuantity);

                                                    checkProduct = false;
                                                }
                                            } while (checkProduct);


                                            //}

                                        } else {
                                            writer.writeBoolean(false);
                                            writer.flush();
                                        }

                                        customerCheck = true;
                                    } else if (customerInput.get(0).equals("search")) {
                                        // search
                                        ArrayList<String> search = (ArrayList<String>) reader.readObject();

                                        if (search.get(0).equals("name")) {
                                            // search by name and write to client
                                            search.add(customer.searchByProductName(search.get(1))); // index: 2
                                            writer.writeObject(search);
                                            writer.flush();
                                        } else if (search.get(0).equals("store")) {
                                            // search by store and write to client
                                            search.add(customer.searchByStoreName(search.get(1))); // index: 2
                                            writer.writeObject(search);
                                            writer.flush();
                                        } else if (search.get(0).equals("description")) {
                                            // search by description and write to client
                                            search.add(customer.searchByDescription(search.get(1))); // index: 2
                                            writer.writeObject(search);
                                            writer.flush();
                                        }

                                        customerCheck = true;

                                    } else if (customerInput.get(0).equals("viewPurchaseHistory")) {
                                        // view Purchase History

                                        ArrayList<String> viewHistory = new ArrayList<>();
                                        viewHistory.add(customer.exportHistory(true, true));
                                        writer.writeObject(viewHistory);
                                        writer.flush();


                                        customerCheck = true;

                                    } else if (customerInput.get(0).equals("export")) {
                                        // export and write back to client

                                        ArrayList<String> export = new ArrayList<>();
                                        export.add(customer.exportHistory(true, true));
                                        writer.writeObject(export);
                                        writer.flush();

                                        boolean hasPurchased = reader.readBoolean();
                                        customer.exportHistory(true, true);

                                        customerCheck = true;

                                    } else if (customerInput.get(0).equals("viewDashboard")) {
                                        // view dashboard


                                        ArrayList<String> dashBoard = (ArrayList<String>) reader.readObject();

                                        if (dashBoard.get(0).equals("true")) {
                                            dashBoard.add(customer.dashBoard(true));
                                            writer.writeObject(dashBoard);
                                            writer.flush();
                                        } else if (dashBoard.get(0).equals("false")) {
                                            dashBoard.add(customer.dashBoard(false));
                                            writer.writeObject(dashBoard);
                                            writer.flush();
                                        } else if (dashBoard.get(0).equals("quit")) {
                                            dashBoard.add("no");
                                            writer.writeObject(dashBoard);
                                            writer.flush();
                                        }

                                        customerCheck = true;
                                    }
                                } while (customerCheck);

                            } else {
                                loginCheck = true;
                            }
                        }

                    } while (loginCheck);

                }

            } while (firstCheck);
            // closing writer, reader, and socket
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
