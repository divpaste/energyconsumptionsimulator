# Energy Consumption Simulator

## Requirements
- **Java JDK 17+**  
- **NetBeans IDE**  
- **MySQL Server & Workbench**  

---

## Step 1: Clone the Repository
1. Open NetBeans → **Team → Git → Clone**  
2. Paste the repository URL: https://github.com/divpaste/energyconsumptionsimulator  
3. Select a folder to clone into → Click **Finish**  

---

## Step 2: Setting Up the Database
1. Open **MySQL Workbench** and login to your local instance.  
2. Go to **Server → Data Import**  
3. Select **Import from Self-contained File**  
4. Browse and select the `ecs.sql` file from your NetBeans project:  
   ```
   <ProjectPath>\energyconsumptionsimulator\src\main\resources\com\mycompany\ecs\ecs.sql
   ```  
5. Click **Start Import**  
6. The database `ecs` and table `user_appliances` with test accounts will be created automatically.  

---

## Step 3: Setting Up the Password
1. In NetBeans, open your project → `ECSFrame.java`  
2. Search for:  
```java
private static final String PASSWORD = "";
```  
3. Enter your **MySQL password** inside the quotes and save the file.  

---

## Step 4: Running the Program
1. In NetBeans, right-click **RegisterPage.java** → **Run File**  
2. You can **Register** a new user or **Login** using the temporary test accounts:  
```
Username: testuser1  Password: test123
Username: testuser2  Password: test123
Username: testuser3  Password: test123
Username: testuser4  Password: test123
```

---
