CREATE TABLE solution (
	id uuid NOT NULL,
	code text NOT NULL,
	created timestamp NOT NULL,
	email varchar(255) NOT NULL,
	grading_ended timestamp NULL,
	grading_started timestamp NULL,
	language varchar(255) NOT NULL,
	message text NULL,
	processing_ended timestamp NULL,
	processing_started timestamp NULL,
	"result" varchar(255) NOT NULL,
	score int4 NULL,
	task varchar(255) NOT NULL,
	updated timestamp NOT NULL,
	CONSTRAINT solution_pkey PRIMARY KEY (id)
)
WITH (
	OIDS=FALSE
) ;

