--GET_ALL
SELECT block_id, editor, survey_map
FROM survey_blocks;

--GET_ALL_REFER
SELECT block_id, editor, survey_map
FROM survey_blocks
WHERE block_id = ?;

--GET
SELECT block_id, editor, survey_map
FROM survey_blocks
WHERE block_id = ? AND editor = ?;

--CREATE
INSERT INTO survey_blocks(block_id, editor, survey_map)
VALUES(?, ?, ?);

--UPDATE
UPDATE survey_blocks SET survey_map = ?
WHERE block_id = ? AND editor = ?;

--DELETE
DELETE FROM survey_blocks 
WHERE block_id = ? AND editor = ?;

--DELETE_ALL
DELETE FROM survey_blocks
WHERE block_id = ?;

--END