ALTER TABLE bildverwaltung.ALBUM_PICTURE DROP CONSTRAINT FK_ALBUM_PICTURE_alben_ID
ALTER TABLE bildverwaltung.ALBUM_PICTURE DROP CONSTRAINT FK_ALBUM_PICTURE_pictures_ID
DROP TABLE bildverwaltung.ALBUM
DROP TABLE bildverwaltung.PICTURE
DROP TABLE bildverwaltung.RESOURCESTRING
DROP TABLE bildverwaltung.ALBUM_PICTURE