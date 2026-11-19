# ComputerCenterShop v1 JavaFX Starter

Student starter project for the RA2 backlog of module MP0486 - Data Access.

## Purpose

This variant is derived from `ComputerCenterShop_v1_javafx`, but it is not fully implemented. It compiles partially (without JavaFX) and retains the JavaFX/FXML interface, the model, and the service layer; however, the `ProductDaoImplJDBC` layer contains intentional gaps, requiring students to write or fix the actual JDBC code.

## Tasks for Students

| Issue | Pending Code |
| --- | --- |
| RA2-ISSUE-02 | Implement `connect()` using a URL, username, and system properties. |
| RA2-ISSUE-03 | Implement `initSchema()` with DDL and initial seed data. |
| RA2-ISSUE-04 | Implement `insertProduct()` using `PreparedStatement`. |
| RA2-ISSUE-05 | Implement `updateStock()` and availability logic based on stock levels. |
| RA2-ISSUE-06 | Implement `deleteProduct()` with appropriate safeguards. |
| RA2-ISSUE-07 | Implement queries, `ResultSet` handling, and `mapProduct()`. |
| RA2-ISSUE-08 | Ensure proper resource closure using `try-with-resources` and `disconnect()`. |
| RA2-ISSUE-09 | Implement transaction `commit` / `rollback` logic. |
| RA2-ISSUE-10 | Implement calls using `CallableStatement`. |
| RA2-ISSUE-11 | Improve error handling for connections and operations. |

## Execution Requirements

Prerequisites:

- JDK 25 LTS.
- Maven. Compile:

```bash
mvn compile
```

Run JavaFX:

```bash
mvn javafx:run
```

In the initial state, the application may display `RA2-ISSUE-xx pending` errors. These errors are intentional and indicate which issue needs to be resolved.

## RA2 Constraints

You must not introduce ORM, Hibernate, ObjectDB, MongoDB, external services, primary file-based persistence, or reusable components from RA6.
