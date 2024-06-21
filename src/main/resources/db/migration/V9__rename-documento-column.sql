ALTER TABLE pacientes
CHANGE COLUMN documento documentoIdentidad varchar(14) NOT NULL UNIQUE;