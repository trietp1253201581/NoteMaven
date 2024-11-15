--GET_ALL
SELECT * 
FROM notes
ORDER BY note_id;

--GET_ALL_REFER
SELECT *
FROM notes
WHERE author = ?
ORDER BY last_modified_date, header;

--GET
SELECT *
FROM notes
WHERE note_id = ?;

--CREATE
INSERT INTO notes(author, header, last_modified_date)
VALUES (?, ?, ?);

--UPDATE
UPDATE notes SET author = ?, header = ?, last_modified_date = ?
WHERE note_id = ?;

--DELETE
DELETE FROM notes
WHERE note_id = ?;

--DELETE_ALL
DELETE FROM notes
WHERE author = ?;

--END