
CREATE USER 'billing'@'localhost' IDENTIFIED BY 'billing123';

GRANT ALL PRIVILEGES ON billing.* TO billing@localhost;

FLUSH PRIVILEGES;
