ALTER TABLE IF EXISTS bildverwaltung.ALBUM_PICTURE DROP CONSTRAINT IF EXISTS FK_ALBUM_PICTURE_alben_ID;
ALTER TABLE IF EXISTS bildverwaltung.ALBUM_PICTURE DROP CONSTRAINT IF EXISTS FK_ALBUM_PICTURE_pictures_ID;
DROP TABLE IF EXISTS bildverwaltung.ALBUM;
DROP TABLE IF EXISTS bildverwaltung.PICTURE;
DROP TABLE IF EXISTS bildverwaltung.RESOURCESTRING;
DROP TABLE IF EXISTS bildverwaltung.ALBUM_PICTURE;
DROP SCHEMA IF EXISTS bildverwaltung;