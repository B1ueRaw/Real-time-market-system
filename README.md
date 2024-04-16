# Real-time market system

Project Members: Sohee Kim, Junyi Wang, Weicheng Wang, Alec DiBiase

## DESCRIPTION
A simple GUI program implements a marketplace application with two types of users: customer and seller.
## SUBMISSIONS
Sohee Kim - Submitted all the code on Vocareum<br/>
Sohee Kim - Submitted the report to Brightspace<br/>
Junyi Wang - Submitted the presentation to Brightspace<br/>
## INSTRUCTION

### Customer.java
This class includes the methods that could be called by Server.java to display customer and market information in Client.java

#### Fields
| Name | Type | Modifiers | Description |
|  -------------------- | ----------- | ---------------- |  --------------  | 
| name | String | private | username of this customer |

#### Constructor
| Parameters | Modifier | Description |
|  -------------------- | ----------- | ---------------- |
| String name | public | Instantiate the class fields to their associated parameters. |

#### Methods
| Access ModifierReturn | Return Type | Constructor Name | Input Parameters |          Description         |
|  -------------------- | ----------- | ---------------- |  --------------  | ---------------------------- |
| public | String | getName | none | Get the name of this customer |
| public | String | exportHistory | boolean view, boolean export | Read a customer's purchase history and export it |
| public | String | searchByProductName | String productName | Search the market list by a product name |
| public | String | searchByStoreName | String storeName | Search the market list by a store name |
| public | String | searchByDescription | String description | Search the market list by description |
| public | String | showDefaultList | none | Show the market list |
| public | String | showProductInfo | int number | Show information of a product |
| public | String | dashBoard | boolean reverse | To view store and seller information |
| public | int | countProductNumber | none | Return the current number of products  |
| public | boolean | checkQuantity | int number, int quantity | Check if a customer's input quantity to purchase is valid |
| public | void | sortMarketByPrice | boolean reverse | Sort the market list by price, if true from high to low, vice versa |
| public | void | sortMarketByQuantity | boolean reverse | Sort the market list by quantity, if true from high to low, vice versa |
| public | void | purchaseItem | int number, int quantity | Purchase chosen product with quantity |



### Seller.java 
Seller Description. 

#### Fields
| Name | Type | Modifiers | Description |
|  -------------------- | ----------- | ---------------- |  --------------  | 
| name | String | private | name of seller |
| stores | ArrayList | private | arrayList of all the stores seller owns |
| fileName | String | private | name of file |
| finalQuantity | int | private | final quantity in int

#### Constructor
| Parameters | Modifier | Description                                                                                                                                              |
|  -------------------- | ----------- |----------------------------------------------------------------------------------------------------------------------------------------------------------|
| String name | public | Instantiate the class fields to their associated parameters. Checks if the seller has logged in before, reconstruct the products and stores he/she owns. |

#### Methods
| Access ModifierReturn | Return Type | Constructor Name | Input Parameters |          Description         |
|  -------------------- | ----------- | ---------------- |  --------------  | ---------------------------- |
| public | String | getName | none | returns name of seller|
| public | void | setName | String name | sets the name of seller and generates a new txt file of seller. |
| public | ArrayList<Store> | getStores | none | returns arraylist of stores. |
| public | void | setStores | ArrayList<Store> Stores | generates new txt file of stores. |
| public | String | getFileName | none | returns fileName. |
| public | int | getFinalQuantity | none | returns finalQuantity. |
| public | void | addStore | String storeName | Adds the store. |
| public | void | deleteStore | String storeName | Deletes the store. |
| public | void | modifyStore | String oldStorename, String newStoreName | Modifies the store. |
| public | void | createProduct | String productName, String storeName, String description, int quantity, double price, boolean recreate | Creates a new product for the seller's name. |
| public | String | viewSales | String storeName | Views all the sales the seller has made. |
| public | boolean | exportCSV | String path | Exports products of stores seller owns. |
| public | int | importCSV | String path | Imports products of stores seller owns. |
| public | String | createDashboard | boolean reverseOrder | Creates dashboard of statistics for each of the store seller owns. |
| public | String | haveStore | none | Checks if the seller has a store. |
| public | String | toString | none | Used for toString format in other methods. |
| pubilc | void | toFile | none | Creates a txt file for the seller.
| public | void | deleteFile | none | Deletes the original txt file for the seller.

### Store.java
Store Description

#### Fields
| Name | Type | Modifiers | Description |
|  -------------------- | ----------- | ---------------- |  --------------  | 
| name | String | private | name of this store |
| fileName | String | private | the path to access txt files |
| products | ArrayList<Product> | private | arraylist of all products in this store |

#### Constructor
| Parameters | Modifier | Description |
|  -------------------- | ----------- | ---------------- |
| String name, String sellerName | public | Instantiate the class fields to their associated parameters and Write to sellerName_totalStores.txt when a new store created |

#### Methods
| Access ModifierReturn | Return Type | Constructor Name | Input Parameters |          Description         |
|  -------------------- | ----------- | ---------------- |  --------------  | ---------------------------- |
| public | ArrayList<Product> | getProducts | none | Get products belong to this store |
| public | String | getName | none | Get the name of this store |
| public | String | setName | String name | Set a name to this store |
| public | String | showProductInfo | int number | Show information of a product |
| public | void | addProduct | String name, String description, int quantity, double price, boolean recreate | Add products |
| public | void | modifyProduct | String productName, String description, int quantity, double price | Modify products |
| public | void | deleteProduct | String productName | Delete products |
| public | void | toFile | none | Write to fileName |
| public | boolean | deleteFile | none | Delete original file  |
| public | boolean | equals | Object obj | Check if a customer's input quantity to purchase is valid |
| public | boolean | isEmpty | none | Checks if there is any products in the store |
| public | String | toString | none | Method that is used for toString format in modify and delete product |

### Product.java
Pruduct Description

#### Fields
| Name | Type | Modifiers | Description |
|  -------------------- | ----------- | ---------------- |  --------------  | 
| name | String | private | name of this store |
| storeName | String | private | name of the store that this product belongs to |
| description | String | private | product description |
| quantity | int | private | product quantity |
| price | double | private | product price |
| fileName | String | private | file path |

#### Constructor
| Parameters | Modifier | Description |
|  -------------------- | ----------- | ---------------- |
| String name, String storeName, String description, int quantity, double price | public | Instantiate the class fields to their associated parameters and Write to a history file contains all products sold in the store |

#### Methods
| Access ModifierReturn | Return Type | Constructor Name | Input Parameters |          Description         |
|  -------------------- | ----------- | ---------------- |  --------------  | ---------------------------- |
| public | String | getName | none | Get name of this product |
| public | String | getStoreName | none | Get name of the store this product belongs to |
| public | String | getDescription | none | Get description |
| public | void | setDescription | String description | Set description |
| public | int | getQuantity | none | Get quantity |
| public | void | setQuantity | int Quantity | Set quantity |
| public | double | getPrice | none | Get price |
| public | void | setQuantity | double price | Set price |
| public | boolean | equals | Object obj | Whether the object given as a parameter is equal to this product |
| public | String | toString | none | a string representation of the product, with ", " separating each field |
| public | void | toFile | none | Create a txt file for the product |
| public | boolean | deleteFile | none | Delete the original txt file for the product  |
  
### Client.java 
A public class that contains all the GUI for this program. This class implement Runnable.
#### Methods
| Access ModifierReturn | Return Type | Constructor Name | Input Parameters |          Description         |
|  -------------------- | ----------- | ---------------- |  --------------  | ---------------------------- |
| public static | void | main | String[] args | The main method to run this program |
| public | void | run | none | Run method to implement multithreading and concurrency. |

### Server.java 
This class is to connect with with the client and transfer data to each other while running at the same time. This class implements Runnable.
#### Fields
| Name | Type | Modifiers | Description |
|  -------------------- | ----------- | ---------------- |  --------------  | 
| Socket | Socket | none | A network socket is a software structure within a network node of a computer network that serves as an endpoint for sending and receiving data across the network |

#### Constructor
| Parameters | Modifier | Description |
|  -------------------- | ----------- | ---------------- |
| Socket socket | public | Instantiate the class fields to their associated parameters. |
#### Methods
| Access ModifierReturn | Return Type | Constructor Name | Input Parameters |          Description         |
|  -------------------- | ----------- | ---------------- |  --------------  | ---------------------------- |
| public static | void | main | String[] args | The main method to run this program |
| public | void | run | none | this method contains all the data transfer of this program |
  
### Testing
#### How To Compile and Run:
1. Run Server class first and then Client class.
2. None of the files in the GitHub will need to be downloaded in order for the project to work since they are all created in the program. However, the 'users.txt' file is shown in GitHub for testing purposes.
3. Create extra Client classes if having more than one Client is needed for grading.
4. The ideal way to test our program is through an IDE (we used IntelliJ).
5. Important: Please use the GitHub version, not the Vocareum.

#### Testcase

