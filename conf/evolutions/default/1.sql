# articles テーブルの作成

# --- !Ups
CREATE TABLE IF NOT EXISTS articles(
  id int(11) NOT NULL AUTO_INCREMENT,
  title varchar(255) NOT NULL,
  url varchar(255) NOT NULL,
  PRIMARY KEY(id),
  UNIQUE INDEX index_on_url (url)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

# --- !Downs
DROP TABLE IF EXISTS articles;
