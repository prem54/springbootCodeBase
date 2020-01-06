INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');

INSERT INTO users(email, name, password, username) VALUES('admin@test.com', 'admin', 'test1234', 'admin');
INSERT INTO users(email, name, password, username) VALUES('user@test.com', 'user', 'test1234', 'user');

INSERT INTO user_roles(user_id, role_id) VALUES(1, 2);
INSERT INTO user_roles(user_id, role_id) VALUES(2, 1);

INSERT INTO tbl_employeedetails(emp_id, department_name, email_id, emp_name) VALUES(1, 'IT', 'emp1@test.com', 'emp1');
INSERT INTO tbl_employeedetails(emp_id, department_name, email_id, emp_name) VALUES(2, 'Sales', 'emp2@test.com', 'emp2');
INSERT INTO tbl_employeedetails(emp_id, department_name, email_id, emp_name) VALUES(3, 'HR', 'emp3@test.com', 'emp3');
INSERT INTO tbl_employeedetails(emp_id, department_name, email_id, emp_name) VALUES(4, 'Sales', 'emp4@test.com', 'emp4');
INSERT INTO tbl_employeedetails(emp_id, department_name, email_id, emp_name) VALUES(5, 'IT', 'emp5@test.com', 'emp5');