--GET_ALL
SELECT filter FROM note_filters 
ORDER BY note_id, filter;

--GET_ALL_REFER
SELECT filter FROM note_filters 
WHERE note_id = ?
ORDER BY filter;

--GET
SELECT filter FROM note_filters
WHERE note_id = ? AND filter = ?;

--CREATE_KEY
INSERT INTO note_filters(note_id, filter)
VALUES (?, ?);

--DELETE
DELETE FROM note_filters 
WHERE note_id = ? AND filter = ?;

--DELETE_ALL
DELETE FROM note_filters 
WHERE note_id = ?;

--END