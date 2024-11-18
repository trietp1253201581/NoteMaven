--GET_ALL
SELECT * 
FROM sharenotes
ORDER BY note_id, editor;

--GET_ALL_REFER
SELECT *
FROM sharenotes
WHERE editor = ?
ORDER BY note_id;

--GET
SELECT *
FROM sharenotes
WHERE note_id = ? AND editor = ?;

--CREATE
INSERT INTO sharenotes(note_id, editor, share_type) 
VALUES (?, ?, ?);

--UPDATE
UPDATE sharenotes SET share_type = ?
WHERE note_id = ? AND editor = ?;

--DELETE
DELETE FROM sharenotes
WHERE note_id = ? AND editor = ?;

--DELETE_ALL
DELETE FROM sharenotes
WHERE note_id = ?;

--END