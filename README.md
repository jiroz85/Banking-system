# 🏦 Banking Management System

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Swing](https://img.shields.io/badge/GUI-Java%20Swing-orange?style=for-the-badge)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![JDBC](https://img.shields.io/badge/Database%20Driver-JDBC-blue?style=for-the-badge)](https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)](LICENSE)

A desktop banking management application built in **Java** utilizing **Java Swing** for the interactive user interface and **MySQL** via **JDBC** for robust, persistent financial data management.

The system allows registered users to securely authenticate, inspect account balances, deposit funds, perform withdrawals with overdraft protection, execute peer-to-peer money transfers, and audit chronological transaction records.

---

## 📋 Table of Contents
- [✨ Key Features](#-key-features)
- [🏗️ System Architecture](#️-system-architecture)
- [🗄️ Database Design](#️-database-design)
- [🚀 Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Database Setup](#1-database-setup)
  - [Opening & Running the Project](#2-opening--running-the-project)
- [🔑 Demo Credentials](#-demo-credentials)
- [📁 Project Structure](#-project-structure)
- [🛡️ Security & Integrity](#️-security--integrity)
- [📄 License](#-license)
- [👤 Author](#-author)

---

## ✨ Key Features

- **🔐 User Authentication:** Secure credential verification checking usernames and passwords against persistent database records.
- **💰 Balance Inquiry:** Instant real-time calculation and display of the logged-in customer's current balance.
- **📥 Deposit Operations:** Seamless account crediting with automatic balance updates and corresponding ledger entry creation.
- **📤 Safe Withdrawals:** Real-time balance verification to prevent account overdrafts before dispensing funds.
- **🔄 Peer-to-Peer Fund Transfer:** Two-way atomic fund transfer between registered accounts:
  - Verifies sender account solvency.
  - Automatically deducts funds from the sender and credits the recipient.
  - Writes audit logs for both the sender (`Transfer Sent`) and receiver (`Transfer Received`).
- **📜 Transaction Ledger & History:** Comprehensive historical view of all deposits, withdrawals, and transfers with precise database timestamps.

---

## 🏗️ System Architecture

The application is structured into decoupled UI and Data Access layers:

```
+-------------------------------------------------------+
|                    Java Swing GUI                     |
|    (BankLoginForm.java  <--->  Dashboard.java)       |
+-------------------------------------------------------+
                           |
                           v  Method Calls
+-------------------------------------------------------+
|                 Data Access Layer                     |
|                   (Database.java)                     |
|  - Connection Management                              |
|  - PreparedStatements (SQL Injection Prevention)     |
+-------------------------------------------------------+
                           |
                           v  JDBC Driver
+-------------------------------------------------------+
|                 MySQL / MariaDB RDBMS                 |
|               (Database: `banking_db`)                |
|       [users]        [accounts]      [transactions]   |
+-------------------------------------------------------+
```

---

## 🗄️ Database Design

The system runs on a relational schema designed in MySQL (`banking_db`):

### 1. `users` Table
Stores user credentials for application sign-in.
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | `INT(11)` | PRIMARY KEY, AUTO_INCREMENT | Unique user identifier |
| `username` | `VARCHAR(50)` | NOT NULL, UNIQUE | User login name |
| `password` | `VARCHAR(50)` | NOT NULL | User account password |

### 2. `accounts` Table
Maintains financial balance records linked to user profiles.
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | `INT(11)` | PRIMARY KEY, AUTO_INCREMENT | Account identifier |
| `username` | `VARCHAR(50)` | NOT NULL | Associated username |
| `balance` | `DOUBLE` | DEFAULT 0 | Current account balance |

### 3. `transactions` Table
Maintains an immutable financial audit trail.
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | `INT(11)` | PRIMARY KEY, AUTO_INCREMENT | Transaction identifier |
| `username` | `VARCHAR(50)` | NULLABLE | Transacting account holder |
| `type` | `VARCHAR(20)` | NULLABLE | Transaction type (`Deposit`, `Withdraw`, `Transfer`) |
| `amount` | `DOUBLE` | NULLABLE | Transacted monetary amount |
| `recipient`| `VARCHAR(50)` | NULLABLE | Counterparty username (for transfers) |
| `timestamp`| `TIMESTAMP` | DEFAULT CURRENT_TIMESTAMP | Date and time of operation |

---

## 🚀 Getting Started

### Prerequisites
Make sure you have the following installed on your machine:
- **Java Development Kit (JDK):** Version 8, 11, 17 or higher
- **MySQL Server & phpMyAdmin:** (e.g. via [XAMPP](https://www.apachefriends.org/), [WampServer](https://www.wampserver.com/), or standalone MySQL)
- **IDE:** [Apache NetBeans](https://netbeans.apache.org/), IntelliJ IDEA, or Eclipse

---

### 1. Database Setup
1. Start your **MySQL** service (via XAMPP Control Panel or terminal).
2. Open your database administration tool (such as **phpMyAdmin** at `http://localhost/phpmyadmin`).
3. Create a new database named `banking_db`:
   ```sql
   CREATE DATABASE banking_db;
   ```
4. Import the provided schema file:
   - Click on `banking_db` in phpMyAdmin.
   - Go to the **Import** tab.
   - Choose the file [`banking_db.sql`](banking_db.sql) located at the root of this project.
   - Click **Go** / **Import**.

> [!NOTE]
> Database connection settings can be adjusted in [`Database.java`](BankingProject/src/bankingproject/Database.java):
> ```java
> private static final String URL = "jdbc:mysql://localhost:3306/banking_db";
> private static final String USER = "root";
> private static final String PASSWORD = "";
> ```

---

### 2. Opening & Running the Project

#### Using Apache NetBeans:
1. Open **Apache NetBeans**.
2. Select **File** -> **Open Project**.
3. Browse to this directory and select the `BankingProject` folder.
4. Ensure the MySQL JDBC connector library is added to project libraries:
   - Right click **Libraries** -> **Add JAR/Folder** -> Select `mysql-connector-j.jar` (or `mysql-connector-java.jar`).
5. Right-click [`BankLoginForm.java`](BankingProject/src/bankingproject/BankLoginForm.java) and select **Run File** (or press `Shift + F6`).

#### Using the Compiled JAR:
You can also launch the pre-packaged build directly:
```bash
cd BankingProject/dist
java -jar BankingProject.jar
```

---

## 🔑 Demo Credentials

The database dump includes pre-configured sample accounts for testing:

| Username | Password | Starting Balance |
|---|---|---|
| `jiro` | `1234` | $6,627.00 |
| `messi` | `abc123` | $12,000.00 |
| `sami` | `1111` | $3,000.00 |
| `linda` | `pass` | $8,000.00 |

---

## 📁 Project Structure

```
Banking-system/
├── banking_db.sql                 # Complete MySQL database export (tables & seed data)
├── README.md                      # Comprehensive project documentation
├── LICENSE                        # MIT License
└── BankingProject/                # NetBeans Java Project Root
    ├── build.xml                  # Ant build configuration
    ├── manifest.mf                # Jar manifest file
    ├── nbproject/                 # NetBeans project metadata
    ├── dist/                      # Compiled binaries and packaged JAR
    │   └── BankingProject.jar
    └── src/
        └── bankingproject/
            ├── BankLoginForm.java  # Authentication GUI window
            ├── Dashboard.java      # Main banking operations GUI
            ├── BankDashboard.java  # Alternative dashboard variant
            └── Database.java       # Database abstraction & transaction logic
```

---

## 🛡️ Security & Integrity

- **SQL Injection Defense:** All database queries utilize parameterized `PreparedStatement` queries rather than concatenated strings.
- **Overdraft Validation:** The withdrawal and transfer functions verify that `balance >= amount` before updating balances.
- **Resource Management:** Database connections and statements leverage `try-with-resources` blocks to ensure timely connection cleanup and prevent memory leaks.

---

## 📄 License
This project is licensed under the [MIT License](LICENSE) - see the LICENSE file for details.

---

## 👤 Author
**Jiregna Meseret**
- GitHub: [@jiroz85](https://github.com/jiroz85)
- Portfolio: [jiregna-portfolio.vercel.app](https://jiregna-portfolio.vercel.app)
