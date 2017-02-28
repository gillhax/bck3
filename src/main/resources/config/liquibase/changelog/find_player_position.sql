CREATE FUNCTION find_player_position (player_id BIGINT)
RETURNS BIGINT
READS SQL DATA
BEGIN
RETURN (select sub.position from (SELECT (@row_number:=@row_number + 1) AS position, id FROM player, (SELECT @row_number:=0) AS t order by score desc) as sub where sub.id = player_id);
END
GO
