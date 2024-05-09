# Conceptual Design: ExpenseMaster

## Summary: 
Expense Master is a convenient and practical finance management app that empowers users to efficiently monitor and regulate their daily expenses. Its intuitive interface facilitates recording and organizing various expenditures with specifics like date, item count, unit prices, and even attached images of receipts. The app's analytical capabilities offer insightful visualizations on spending trends and enable users to create customized expense categories. Generating informative PDF reports helps in making educated choices about personal financing. By fostering better financial habits, promoting wise decision-making, and ensuring sustainable money management practices, Expense Master serves as a powerful tool for safeguarding one’s financial future.


### Key Features

- User Registration: Users can register with their contact information, including name, nickname, password, email, and profile image.
- Authentication: Users can log in to the application using their nickname and password.
- Add Expense Note: Users can add new expense notes, including details such as date, product, units, and price.
- View Expense History: Users can view their expense history, including total expenses and expenses grouped by category.
- Categorize Expenses: Users can create and manage expense categories to organize their expenses.
- Generate PDF Report: Users can generate PDF reports of their expenses for tracking and analysis.
- Update Profile Information: Users can update their profile information, except for their nickname.
- Delete Expense Note: Users can delete unnecessary expense notes.
- Delete Category: Users can delete unused expense categories.

### Use Case 


| **User Action**                                     | **System Response**                                                                                                      |
|-----------------------------------------------------|----------------------------------------------------------------------------------------------------------------          |
| User accesses the registration option.              | System displays a form for the user to enter their contact information (name, nickname, password, email, profile image). |
| User submits the required registration information.| System validates the data format, saves the user's contact information, and stores the registration date.                 |
| User successfully signs up.                        | System prompts the user to log in to use the application.                                                                 |
| User enters their nickname and password.           | System checks the credentials, prompts for correction if needed, and authenticates the user.                              |
| User adds an expense note, creates a new category, and adds another expense.| System validates the data format and saves the expense note with the provided information. It also creates the new expense category as per user input.                                                                                                                                          |
| User adds a screenshot of the invoice.             | System allows the user to upload an image of the receipt to be saved for warranty claims.                                 |
| User successfully adds the expense.                | System confirms the successful addition of the expense note and updates the user's expense history.                       |
| User navigates to view their monthly expenses.     | System displays the user's expense history, including total expenses by category, comparison to past months, and year-over-year comparison.                                                                                                                                                                      |
| User requests an annual expenses summary in PDF format.| System generates a PDF report presenting the evolution of the user's annual expenses.                                 |
| User updates their profile data (excluding nickname).| System validates the new data and applies updates to the user's profile information.                                    |
| User replaces the receipt image for a prior expense.| System enables modification of the expense to change the attached receipt/invoice photo.                                 |
| User deletes a single expense item.                | System removes the specific charge from the user's expense history.                                                       |
| User eliminates obsolete expense categories.       | System deletes the chosen categories, together with all associated expenses.                                              |
| User logs out of the application.                  | System removes the user from the logged-in state and updates the application to reflect the change.                       |


| **Object**                            | **Attributes**                                                         |**Actions**                                                      |
|---------------------------------------|------------------------------------------------------------------------|-----------------------------------------------------------------|
| User                                  | Name, Nickname, Password, Email, Profile image, Registration date	     |Add, Remove, Register, Authenticate, Update, Modify, View, Set   |


| **Object**                            | **Attributes**                                                         |**Actions**                                                      |
|---------------------------------------|------------------------------------------------------------------------|-----------------------------------------------------------------|
| Category                              | Name, Description                                                      | Create, Edit, Update, Delete, View                              |

| **Object** | **Attributes** | **Actions** |
|------------|----------------|-------------|
| Expense Note  | Date, Product, Units, Price, Receipt Image | Add, Update, Delete, View |



| **Object** | **Attributes** | **Actions** |
|------------|----------------|-------------|
| Report     | Title, Content, Date, Format | Create, Update, Delete, View |



