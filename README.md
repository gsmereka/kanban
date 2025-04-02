<div id="top">

<!-- HEADER STYLE: CLASSIC -->
<div align="center">

<img src="./logo.png" width="30%" style="position: relative; top: 0; right: 0;" alt="Decola Tech 2025 Logo"/>

# KANBAN

<em></em>

<!-- BADGES -->
<img src="https://img.shields.io/github/license/gsmereka/kanban?style=flat-square&logo=opensourceinitiative&logoColor=white&color=FF4B4B" alt="license">
<img src="https://img.shields.io/github/last-commit/gsmereka/kanban?style=flat-square&logo=git&logoColor=white&color=FF4B4B" alt="last-commit">
<img src="https://img.shields.io/github/languages/top/gsmereka/kanban?style=flat-square&color=FF4B4B" alt="repo-top-language">
<img src="https://img.shields.io/github/languages/count/gsmereka/kanban?style=flat-square&color=FF4B4B" alt="repo-language-count">

<em>Built with the tools and technologies:</em>

<img src="https://img.shields.io/badge/XML-005FAD.svg?style=flat-square&logo=XML&logoColor=white" alt="XML">

</div>
<br>

---

## Table of Contents

- [Table of Contents](#table-of-contents)
- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Contributing](#contributing)
- [Acknowledgments](#acknowledgments)

---

## 🔨 Overview

This project is a continuation of the [**Board Project**](https://github.com/digitalinnovationone/board) developed during the **DecolaTech 2025** bootcamp. The following modifications have been implemented:
- 🔄 **Migrated the project from Gradle to Maven**  
- 🛠️ **Added support for multiple database connections**  
- 🧪 **Implemented specific configurations for test databases**  
- ✅ **Included service layer tests**  
- 📌 **Created new services**, such as `findAll()` and `deleteAll()`  
- 🎯 **Improved input handling** during menu navigation 

## 🎓 About DecolaTech 2025  

**DecolaTech 2025** is a bootcamp organized through a collaboration between [**DIO**](https://www.dio.me/) and [**Avanade**](https://www.avanade.com/). The program aims to equip participants with essential skills in **full-stack development** using **Spring Boot** and **Angular**, while also providing foundational knowledge in **Artificial Intelligence** and **Azure** for efficient deployment.  

Upon completion, participants have the opportunity to apply for **paid internships** at Avanade.  

---

## Features

### 📋 Board Management  
- ✅ Create a new board with a custom name and columns  
- ✅ Select an existing board for management  
- ✅ Delete boards stored in the MySQL database  

### 📌 Board Structure  
- ✅ Each board has at least 3 columns (**Initial**, **Final**, and **Cancellation**)  
- ✅ Support for multiple **"Pending"** columns  
- ✅ Automatic enforcement of column ordering rules  

### 🎴 Card Management  
- ✅ Create cards with **title, description, creation date, and block status**  
- ✅ Move cards while respecting column order  
- ✅ Cancel cards from any column (**except the final one**)  
- ✅ Block and unblock cards with justification   

### 🖥️ Command-Line Interface (CLI)  
- ✅ Interactive **menu-based CLI** for board and card management  
- ✅ Navigate through options with **easy-to-use commands**  
- ✅ Perform **all board and card operations** directly from the terminal

---

💡 *More features coming soon!* 🚀  

---

## Project Structure

```sh
└── kanban/
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    └── src
        ├── main
        └── test
```

---

## Getting Started

## 🛠️ Prerequisites

Make sure you have the following tools and environments set up:

### 1. **Java 17**
   - This project uses Java 17. Download the latest version from [AdoptOpenJDK](https://adoptopenjdk.net/).
   - Verify Java installation with:

     ```bash
     java -version
     ```

### 2. **Maven**
   - Maven is used to manage dependencies and build the project. Install it by following the instructions on [Maven’s official site](https://maven.apache.org/install.html).
   - Verify installation with:

     ```bash
     mvn -v
     ```

### 3. **MySQL**
   - MySQL is used for the database. Install it or use Docker for setup.

### 4. **Docker (Optional)**
   - Docker can be used to set up the database in containers. Follow [Docker’s installation guide](https://docs.docker.com/get-docker/).

---

## 🚀 Installation

Follow these steps to install and run the project locally:

### 1. **Clone the repository**

   Clone the repository to your local machine:

   ```bash
   git clone https://github.com/gsmereka/kanban
   ```

### 2. **Navigate to the project directory**

   Go to the project folder:

   ```bash
   cd kanban
   ```

### 3. **Database Setup**

#### **Using Docker for Database**

If MySQL is not installed locally, use Docker to set up the database.

**Production Database:**

```yaml
version: '3.8'
services:
  db:
    image: mysql:8.3.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_USER: board
      MYSQL_PASSWORD: board
      MYSQL_DATABASE: board
    ports:
      - "3306:3306"
```

**Test Database:**

```yaml
version: '3.8'
services:
  db:
    image: mysql:8.3.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_USER: KanbanTest
      MYSQL_PASSWORD: KanbanTest
      MYSQL_DATABASE: KanbanTest
    ports:
      - "9898:3306"
```

1. **Start Containers:**

   ```bash
   docker-compose up -d
   ```

2. **Check Containers:**

   ```bash
   docker ps
   ```

#### **Using MySQL Locally**

Alternatively, manually install MySQL and create two databases: `board` (production) and `KanbanTest` (test).

### 4. **Install Dependencies**

In the project directory, run:

```bash
mvn install
```

### 5. **Configure Properties (if needed)**

Edit the `application.properties` or `application.yml` file for database credentials or connection URLs.

### 6. **Run the Project**

Start the application with:

```bash
mvn exec:java
```

This will launch the app locally, and you can use the CLI to interact with boards and cards.

### Testing

Kanban uses the {__test_framework__} test framework. Run the test suite with:

**Using [maven](https://maven.apache.org/):**
```sh
mvn test
```

---

## Contributing

- **💬 [Join the Discussions](https://github.com/gsmereka/kanban/discussions)**: Share your insights, provide feedback, or ask questions.
- **🐛 [Report Issues](https://github.com/gsmereka/kanban/issues)**: Submit bugs found or log feature requests for the `kanban` project.
- **💡 [Submit Pull Requests](https://github.com/gsmereka/kanban/blob/main/CONTRIBUTING.md)**: Review open PRs, and submit your own PRs.

<details closed>
<summary>Contributing Guidelines</summary>

1. **Fork the Repository**: Start by forking the project repository to your github account.
2. **Clone Locally**: Clone the forked repository to your local machine using a git client.
   ```sh
   git clone https://github.com/gsmereka/kanban
   ```
3. **Create a New Branch**: Always work on a new branch, giving it a descriptive name.
   ```sh
   git checkout -b new-feature-x
   ```
4. **Make Your Changes**: Develop and test your changes locally.
5. **Commit Your Changes**: Commit with a clear message describing your updates.
   ```sh
   git commit -m 'Implemented new feature x.'
   ```
6. **Push to github**: Push the changes to your forked repository.
   ```sh
   git push origin new-feature-x
   ```
7. **Submit a Pull Request**: Create a PR against the original project repository. Clearly describe the changes and their motivations.
8. **Review**: Once your PR is reviewed and approved, it will be merged into the main branch. Congratulations on your contribution!
</details>

<details closed>
<summary>Contributor Graph</summary>
<br>
<p align="left">
   <a href="https://github.com{/gsmereka/kanban/}graphs/contributors">
      <img src="https://contrib.rocks/image?repo=gsmereka/kanban">
   </a>
</p>
</details>

---

## Acknowledgments

- [DIO](https://www.dio.me/)
- [Avanade](https://www.avanade.com/)
- [Board Project](https://github.com/digitalinnovationone/board)

<div align="right">

[![][back-to-top]](#top)

</div>


[back-to-top]: https://img.shields.io/badge/-BACK_TO_TOP-151515?style=flat-square


---
