DROP DATABASE IF EXISTS alphascopedb;

CREATE DATABASE alphascopedb
    DEFAULT CHARACTER SET utf8mb4;

USE
alphascopedb;

CREATE TABLE employee (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE status (
    status_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE project (
    project_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    start_date DATE,
    end_date DATE,
    hour_price DECIMAL (10,2),
    employee_id INT NOT NULL,
    parent_project_id INT,
    status_id INT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee (employee_id)
        ON DELETE RESTRICT,
    FOREIGN KEY (parent_project_id) REFERENCES project (project_id)
        ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES status (status_id)
);

CREATE TABLE task (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    start_date DATE,
    end_date DATE,
    estimated_hours DECIMAL (5,2),
    actual_hours DECIMAL (5,2),
    project_id INT,
    parent_task_id INT,
    FOREIGN KEY (project_id) REFERENCES project (project_id)
        ON DELETE CASCADE,
    FOREIGN KEY (parent_task_id) REFERENCES task (task_id)
        ON DELETE CASCADE
);

CREATE TABLE competence (
    competence_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE task_competence (
    task_id INT NOT NULL,
    competence_id INT NOT NULL,
    PRIMARY KEY (task_id, competence_id),
    FOREIGN KEY (task_id) REFERENCES task (task_id)
        ON DELETE CASCADE,
    FOREIGN KEY (competence_id) REFERENCES competence (competence_id)
        ON DELETE RESTRICT
);

CREATE TABLE task_employee (
    task_id INT NOT NULL,
    employee_id INT NOT NULL,
    PRIMARY KEY (task_id, employee_id),
    FOREIGN KEY (task_id) REFERENCES task (task_id)
        ON DELETE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employee (employee_id)
        ON DELETE RESTRICT
);

CREATE TABLE employee_competence (
    employee_id INT NOT NULL,
    competence_id INT NOT NULL,
    PRIMARY KEY (employee_id, competence_id),
    FOREIGN KEY (employee_id) REFERENCES employee (employee_id)
        ON DELETE CASCADE,
    FOREIGN KEY (competence_id) REFERENCES competence (competence_id)
        ON DELETE RESTRICT
);

