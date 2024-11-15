--GET_ALL
SELECT * 
FROM note_blocks
ORDER BY note_id, block_order;

--GET_ALL_REFER
SELECT *
FROM note_blocks
WHERE note_id = ?
ORDER BY block_order;

--GET
SELECT *
FROM note_blocks
WHERE block_id = ?;

--CREATE_KEY
INSERT INTO note_blocks(note_id, header, block_type, block_order)
VALUES (?, ?, ?, ?);

--UPDATE_KEY
UPDATE note_blocks SET note_id = ?, header = ?, block_type = ?, block_order = ?
WHERE block_id = ?;

--DELETE
DELETE FROM note_blocks
WHERE block_id = ?;

--DELETE_ALL
DELETE FROM note_blocks
WHERE note_id = ?;

--END