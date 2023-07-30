DROP TABLE IF EXISTS books CASCADE;
DROP TABLE IF EXISTS authors CASCADE;

CREATE TABLE authors
(
    id   bigint PRIMARY KEY,
    name varchar(255) not null
);

CREATE TABLE books
(
    id                  bigint PRIMARY KEY,
    title               varchar(511) not null,
    publisher           varchar(255) not null,
    publishing_year     varchar(4)   not null,
    review              text         null,
    text_search         tsvector     not null default 'empty',
    text_search_version varchar(30)  not null default CURRENT_TIMESTAMP
);

CREATE TABLE authors_books
(
    author_id bigint not null,
    book_id   bigint not null,

    FOREIGN KEY (author_id) REFERENCES authors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE INDEX books_search_idx ON books USING GIN (text_search);

CREATE OR REPLACE FUNCTION websearch_to_wildcard_tsquery(text_query text)
    RETURNS tsquery AS
'
    DECLARE
        query_splits   text[];
        split          text;
        new_text_query text := '''';
    BEGIN
        SELECT regexp_split_to_array(d::text, ''\s+'')
        INTO query_splits
        FROM websearch_to_tsquery(''simple'', text_query) d;

        FOREACH split IN ARRAY query_splits
            LOOP
                CASE
                    WHEN split = ''|'' OR split = ''&'' OR split = ''!'' OR split = ''!('' OR split = ''<->''
                        THEN new_text_query := new_text_query || split || '' '';
                    ELSE new_text_query := new_text_query || split || '':* '';
                    END CASE;
            END LOOP;
        RETURN to_tsquery(''simple'', new_text_query);
    END;
' LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_books_text_search_func()
    RETURNS trigger AS
'
    DECLARE
        author_description      text;
        book_description        text;
        text_search_description tsvector;
    BEGIN
        SELECT string_agg(''author '' || a.name, '' ''),
               2
        INTO author_description
        FROM authors a
                 LEFT JOIN authors_books ab on a.id = ab.author_id
        WHERE ab.book_id = NEW.id
        GROUP BY 2;

        SELECT (''title '' || NEW.title || '' publisher '' || NEW.publisher ||
                '' publishing year '' || NEW.publishing_year || '' '' || coalesce(NEW.review, ''''))
        INTO book_description;

        SELECT to_tsvector(''simple'', coalesce(author_description, '''') || '' '' || book_description)
        INTO text_search_description;

        NEW.text_search := text_search_description;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER update_books_text_search_trigger
    BEFORE INSERT OR UPDATE
    ON books
    FOR EACH ROW
EXECUTE PROCEDURE update_books_text_search_func();

CREATE OR REPLACE FUNCTION authors_update_books_func()
    RETURNS trigger AS
'
    DECLARE
        book_ids_text text;
        book_ids      bigint[];
    BEGIN
        SELECT string_agg(ab.book_id::text, '' ''),
               2
        INTO book_ids_text
        FROM authors_books ab
        WHERE ab.author_id = NEW.id
        GROUP BY 2;

        SELECT regexp_split_to_array(book_ids_text, ''\s+'')
        INTO book_ids;

        UPDATE books b
        SET text_search_version = CURRENT_TIMESTAMP
        WHERE id = ANY (book_ids);

        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER authors_update_books_trigger
    AFTER INSERT OR UPDATE
    ON authors
    FOR EACH ROW
EXECUTE PROCEDURE authors_update_books_func();

CREATE OR REPLACE FUNCTION authors_books_update_books_func()
    RETURNS trigger AS
'
    BEGIN
        UPDATE books
        SET text_search_version = CURRENT_TIMESTAMP
        WHERE id = NEW.book_id;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER authors_books_update_books_trigger
    AFTER INSERT OR UPDATE
    ON authors_books
    FOR EACH ROW
EXECUTE PROCEDURE authors_books_update_books_func();
