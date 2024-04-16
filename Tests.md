**Test 1: User Sign Up**

Steps:
1. User launches application.
2. User selects the button Signup.
3. User selects the Account type text box.
4. User enters account type by typing seller or customer.
5. User selects the username text box.
6. User enters username via the keyboard.
7. User selects the password text box.
8. User enters password via the keyboard.
9. User selects "OK" to register their account.
10. To verify signup is complete, a panel will show "Account created!"

Expected result: Application allow user to register a new username and password. Application checks if the username is
taken or not.

Test Status: Passed!


**Test 2: User Log In**

Steps:
1. User launches application.
2. User selects the button Login.
3. User selects the username text box.
4. User enters username via the keyboard.
5. User selects the password text box.
6. User enters password via the keyboard.
7. User selects "OK" to log in to their account.

Expected result: Application verifies the user's username and password.

Test Status: Passed!

**Test 3: Seller Menu**

Steps:

1. The user with a seller type selects "OK" to log into their account.
2. The seller selects the "Create Product" button.
3. The seller writes the name of product, store selling the product, description of product, quantity available for
   purchase, and price of product.
4. The seller selects the "OK" button directing back to the main seller menu.
5. The seller selects the "Edit Product" button.
6. The seller selects the "OK" button to proceed with edit.
7. The seller enters the store name that has the product and selects the "OK" button.
8. The seller selects the "OK" button.
9. The seller write the same product name and edits the description, price, and quantity.
10. The seller selects the "OK" button.
11. The seller selects the "OK" button directing back to the main seller menu.
12. The seller selects the "Delete Product" button.
13. The seller enters the store name that has the product they wish to delete.
14. The seller deletes the product they wish to delete.
15. The seller selects the "OK" button directing back to the main seller menu.
16. The seller selects the "View Sells" button and enters the store name they wish to view sells on.
17. The seller selects the "OK" button directing back to the main seller menu.
18. The seller selects the "Export Purchase History" button.
19. The seller enters the file path they wish to export the purchase history and selects the "OK" button.
20. The seller selects the "Import" button.
21. The seller enters the file path they wish to import and selects the "OK" button.
22. The seller selects the "View Dashboard" button.
23. The seller selects the "YES" button to sort their dashboard.
24. The seller selects the button directing back to the main seller menu.
25. The seller selects the "Quit" button.
26. The seller goes back to the main menu.
27. The seller selects exit.
28. The GUI will show "Goodbye!"

Expected result: The user that is a seller type will be directed into the seller menu. The
seller will create a new product by writing the name, store, description, quantity, and price
through "Create Product." The seller will be able to edit the product they wish to edit in
"Edit Product" and delete the product they wish to delete in "Delete Product." The seller will
be able to view the sells of their stores in "View Sells." The seller will be able to export
their purchase history in a text file in "Export Purchase History." The seller will be able to
import their purchase history in a text file in "Import." The seller will be able to view the
dashboard in any of the four sorting methods they want to see in "View Dashboard." The seller
can always go back to the Seller Menu after selecting a button. The seller leave the Seller Menu
by selecting "Quit." The seller will go back to the main menu and select exit to leave the application.
Only Client class will end, but Server class will always be running.

Test Status: Passed!

**Test 4: Customer Menu**

Steps:
1. The user with a customer type selects "OK" to log in to their account.
2. The customer selects the "View Market" button.
3. The customer selects "5. No" to view marketplace without any sorting method and clicks the "OK" button.
4. The customer types the number of what product they would like to purchase and clicks the "OK" button.
5. The customer types an integer of how many items they would like to purchase and clicks the "OK" button.
6. The customer selects the "OK" button directing back to the main customer menu.
7. The customer selects the "Search" button.
8. The customer searches the name of the product they wish to see, which is the first button and clicks the "OK" 
button after.
9. The customer selects the "OK" button directing back to the main customer menu.
10. The customer selects the "View Purchase History" button.
11. The customer selects the "OK" button directing back to the main customer menu.
12. The customer selects the "Export" button to view a file of purchase history.
13. The customer selects the "OK" button directing back to the main customer menu.
14. The customer selects the "View Dashboard" button.
15. The customer selects the "Quit" button.
16. The customer goes back to the main menu.
17. The customer selects exit.
18. The GUI will show "Goodbye!"

Expected result: The user that is a customer type will be directed into the customer menu.
The customer will view all products available in the marketplace through "View Market." The
customer will write the name of product they want to see through "Search." The customer will
view all of their purchase history in "View Purchase History" and be able to export their purchase
history in a file through "Export." The customer can view the dashboard in any of the four sorting
methods they want to see in "View Dashboard." The customer can always go back to the Customer Menu
after selecting a button. The customer leaves the Customer Menu by selecting "Quit." 
The customer will go back to the main menu and select exit to leave the application. Only Client class
will end, but Server class will always be running.

Test Status: Passed!