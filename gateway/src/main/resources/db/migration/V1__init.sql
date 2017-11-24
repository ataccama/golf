CREATE TABLE solution (
	id uuid NOT NULL,
	email varchar(255) NOT NULL,
	task varchar(255) NOT NULL,
	language varchar(255) NOT NULL,
	code varchar(32768) NOT NULL,

	created timestamp NOT NULL,
	updated timestamp NOT NULL,

	processing_started timestamp NULL,
	processing_ended timestamp NULL,
	processing_result varchar(255) NOT NULL,
	processing_message varchar(32768) NULL,

	grading_started timestamp NULL,
	grading_ended timestamp NULL,
	grading_result varchar(255) NOT NULL,
	grading_score int4 NULL,
	grading_message varchar(32768) NULL,

	CONSTRAINT solution_pkey PRIMARY KEY (id)
)
WITH (
	OIDS=FALSE
) ;
