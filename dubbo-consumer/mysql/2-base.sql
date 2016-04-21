drop procedure if exists table_base;
create procedure `table_base`()
begin
	declare done int default 0;
	declare a varchar(50);
	declare rs cursor for select `TABLE_NAME` from information_schema.`TABLES` where `TABLE_SCHEMA`='test';
	declare continue handler for sqlstate '02000' set done=1;
	open rs;
	repeat fetch rs into a;
	
    IF NOT done THEN
	set @s=concat('alter table ', a, ' add column create_at timestamp default current_timestamp'
		, ', add column deleted bit default 0'
		, ', add column version int(11) default 1;');
	prepare stmt from @s;
	execute stmt;
    END IF;

	until done end repeat;
	close rs;
end
;
call table_base();
drop procedure if exists table_base;