# --- tweets テーブルの作成

# --- !Ups
CREATE TABLE IF NOT EXISTS tweets (
  id int(11) NOT NULL AUTO_INCREMENT,
  origin_id bigint(255) NOT NULL,
  user_screen_name varchar(255) NOT NULL,
  text varchar(255) NOT NULL,
  retweet_count int(11) NOT NULL,
  favorite_count int(11) NOT NULL,
  lang varchar(255) NOT NULL,
  created_date_time datetime(6) NOT NULL,
  PRIMARY KEY(id)
) ENGINE InnoDB AUTO_INCREMENT 1 DEFAULT CHARSET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci;

# --- !Downs
DROP TABLE IF EXISTS tweets;
