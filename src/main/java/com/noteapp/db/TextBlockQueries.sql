--GET_ALL
SELECT block_id, editor, content
FROM text_blocks;

--GET_ALL_REFER
SELECT block_id, editor, content
FROM text_blocks
WHERE block_id = ?;

--GET
SELECT block_id, editor, content
FROM text_blocks
WHERE block_id = ? AND editor = ?;

--CREATE
INSERT INTO text_blocks(block_id, editor, content)
VALUES (?, ?, ?);

--UPDATE
UPDATE text_blocks SET content = ?
WHERE block_id = ? AND editor = ?;

--DELETE
DELETE FROM text_blocks
WHERE block_id = ? AND editor = ?;

--DELETE_ALL
DELETE FROM text_blocks
WHERE block_id = ?;

--END