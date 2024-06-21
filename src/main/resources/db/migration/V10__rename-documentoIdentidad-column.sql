ALTER TABLE pacientes
CHANGE COLUMN documentoIdentidad documento_identidad varchar(14) NOT NULL UNIQUE;