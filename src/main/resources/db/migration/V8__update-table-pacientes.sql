ALTER TABLE pacientes
ADD COLUMN documento varchar(14) NOT NULL UNIQUE;

ALTER TABLE pacientes
DROP COLUMN urbanización;

ALTER TABLE pacientes
DROP COLUMN codigoPostal;

ALTER TABLE pacientes
DROP COLUMN provincia;
