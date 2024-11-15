--GET_ALL
SELECT * 
FROM sharenotes
ORDER BY note_id, receiver;

--GET_ALL_REFER
SELECT *
FROM sharenotes
WHERE receiver = ?
ORDER BY note_id;

--GET
SELECT *
FROM sharenotes
WHERE note_id = ? AND receiver = ?;

--CREATE
INSERT INTO sharenotes(note_id, receiver, share_type) 
VALUES (?, ?, ?);

--UPDATE
UPDATE sharenotes SET share_type = ?
WHERE note_id = ? AND receiver = ?;

--DELETE
DELETE FROM sharenotes
WHERE note_id = ? AND receiver = ?;

--DELETE_ALL
DELETE FROM sharenotes
WHERE note_id = ?;

--END